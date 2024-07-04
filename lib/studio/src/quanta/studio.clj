(ns quanta.studio
  (:require
   [de.otto.nom.core :as nom]
   [taoensso.timbre :refer [info warn error]]
   [nano-id.core :refer [nano-id]]
   [extension :as ext]
   [clj-service.core :refer [expose-functions]]
   [ta.helper.date :refer [now]]
   [ta.viz.error :refer [error-render-spec]]
   [ta.algo.env :refer [create-env-javelin]]
   [ta.algo.env.protocol :as algo-env]
   [quanta.model.backtest :refer [fire-backtest-events]]
   [quanta.template :as qtempl]
   [quanta.template.task :refer [start-task stop-task]]
   [quanta.template.db :as template-db]
   [quanta.studio.publish :refer [push-viz-result]]))

(defn- requiring-resolve-safe [template-symbol]
  (try
    (requiring-resolve template-symbol)
    (catch Exception ex
      (error "could not resolve template symbol: " template-symbol " ex: " ex)
      nil)))

(defn add-template [this template-symbol]
  (if-let [template-var (requiring-resolve-safe template-symbol)]
    (let [template-val (var-get template-var)]
      (template-db/add this template-val))
    (throw (ex-info "quanta-template could not be resolved"
                    {:template template-symbol}))))

(defn- add-templates
  "adds templates from extensions"
  [this exts]
  (let [template-symbols (ext/get-extensions-for exts :quanta/template concat [] [])]
    (info "adding templates: " template-symbols)
    (doall (map #(add-template this %) template-symbols))))

(defn start-studio [{:keys [exts clj role bar-db env-live websocket]}]
  (info "starting quanta-studio..")
  (let [this {:bar-db bar-db
              :templates (atom {})
              :subscriptions-a (atom {})
              :env-live env-live
              :websocket websocket}]
    (add-templates this exts)
    (if clj
      (do
        (info "starting quanta-studio clj-services..")
        (expose-functions clj
                          {:name "quanta-studio"
                           :symbols [; template
                                     'quanta.template.db/available-templates
                                     'quanta.studio/get-options
                                     ; backtest
                                     'quanta.studio/backtest
                                     ; task
                                     'quanta.studio/start
                                     'quanta.studio/stop
                                     'quanta.studio/current-task-result
                                     'quanta.studio/task-summary]
                           :permission role
                           :fixed-args [this]}))
      (warn "quanta-studio starting without clj-services, perhaps you want to pass :clj key"))
    (info "quanta-studio running!")
    this))

(defn log-viz-result [template-id task-id result]
  (info "viz-result template-id: " template-id " task-id: " task-id (if (nom/anomaly? result) " anomaly!" " success"))
      ; make sure we never retrun something. result ends up in a javelin cell
  nil)

(defn load-with-options [this template-id options]
  (let [template (template-db/load-template this template-id)
        template (qtempl/apply-options template options)]
    (info "template " template-id " options: " (:algo template))
    ;(warn "full template: " template)
    template))

(defn get-options
  "returns the options (what a user can edit) for a template-id"
  ; exposed at start of studio
  [this template-id]
  (info "getting options for template: " template-id)
  (-> (template-db/load-template this template-id)
      (qtempl/get-options)))

(defn backtest
  "this runs a viz-task once and returns the viz-result.
   output is guaranteed to be always viz-spec format, so
   possible anomalies are converted to viz-spec"
  ([this template-id template-options mode]
   (backtest this template-id template-options mode (nano-id 6)))
  ([{:keys [bar-db] :as this} template-id template-options mode task-id]
   (info "backtest template:" template-id "mode: " mode)
   (let [template (load-with-options this template-id template-options)
         env (create-env-javelin bar-db)
         {:keys [viz-result] :as task} (start-task env template mode task-id log-viz-result)
         window-or-dt (now)
         model (algo-env/get-model env)
         result (if (nom/anomaly? task)
                  task
                  (do  (fire-backtest-events model window-or-dt)
                       @viz-result))]

     (if (nom/anomaly? result)
       (error-render-spec result)
       result))))

(defn start-template
  "starts new algo via the web ui.
   this creates a viz-task once and then starts pushing all results to the websocket.
   returns task-id or nom/anomaly"
  ([this template mode]
   (start-template this template mode (nano-id 6)))
  ([{:keys [env-live subscriptions-a websocket] :as this} template mode task-id]
   (info "start template:" (:id template) "mode: " mode)
   (if env-live
     (let [result-fn (partial push-viz-result websocket)
           {:keys [task-id] :as task} (start-task env-live template mode task-id result-fn)]
       (if (nom/anomaly? task)
         task
         (do (swap! subscriptions-a assoc task-id task)
             task-id)))
     (nom/fail ::start-template {:message "cannot start :env-live is nil."}))))

(defn start
  "starts new algo via the web ui.
   this creates a viz-task once and then starts pushing all results to the websocket.
   returns task-id or nom/anomaly"
  ([this template-id template-options mode]
   (start this template-id template-options mode (nano-id 6)))
  ([this template-id template-options mode task-id]
   (info "start template:" template-id "mode: " mode)
   (let [template (load-with-options this template-id template-options)]
     (if template
       (start-template this template mode task-id)
       (nom/fail ::start-template {:message "cannot start template, template not found."})))))

(defn start-variations
  "starts template variations with the same mode"
  [this template-id mode variation-spec]
  (info "starting template: " template-id " mode: " mode " variations: " variation-spec)
  (let [template  (template-db/load-template this template-id)
        template-seq (qtempl/create-template-variations template variation-spec)]
    (doall (map #(start-template this % mode) template-seq))))

(defn stop [{:keys [env-live subscriptions-a] :as this} task-id]
  (if-let [m (get @subscriptions-a task-id)]
    (do (info "stopping task-id: " task-id)
        (stop-task env-live m)
            ; done!
        (swap! subscriptions-a dissoc task-id)
        :success)
    (do (error "cannot stop unknown task-id: " task-id)
        :error)))

(defn current-task-result [{:keys [subscriptions-a] :as this} task-id]
  (if-let [m (get @subscriptions-a task-id)]
    (let [{:keys [viz-result]} m]
      @viz-result)
    (nom/fail ::subscribe {:message "task not found"})))

; task lists

(defn- summarize-task [algo-option-keys {:keys [task-id template start-dt] :as task}]
  (let [{:keys [id algo]} template]
    {:task-id task-id
     :start-dt start-dt
     :template-id id
     :algo (if algo-option-keys
             (select-keys algo algo-option-keys)
             ; if no specific keys are provide
             ; we want to remove data that is not helpful to the user.
             (dissoc algo :type :import :algo))}))

(defn task-summary
  [{:keys [subscriptions-a] :as this} & [algo-option-keys]]
  (let [tasks (vals @subscriptions-a)]
    (map #(summarize-task algo-option-keys %) tasks)))


