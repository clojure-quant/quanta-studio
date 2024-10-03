(ns quanta.studio.template.task.core
  (:require
   [taoensso.timbre :refer [info warn error]]
   [de.otto.nom.core :as nom]
   [nano-id.core :refer [nano-id]]
   [quanta.studio.template.task.admin :refer [start-task stop-task summarize-task]]
   [quanta.studio.template.task.push :refer [process-viz-result]]))

(defn calculate-template
  "this runs a viz-task once and returns the viz-result.
   output is guaranteed to be always viz-spec format, so
   possible anomalies are converted to viz-spec"
  ; calculate-template is required by bruteforce optimizer.
  ; otherwise the template gets loaded and loaded and loaded again.
  ([this template mode dt]
   (calculate-template this template mode dt (nano-id 6)))
  ([{:keys [bar-db] :as this} template mode dt task-id]
   (let [env (create-env-javelin bar-db)
         {:keys [viz-result] :as task} (start-task env template mode task-id log-viz-result)
         window-or-dt (or dt (t/instant))
         _ (warn "end-dt: " window-or-dt)
         _ (warn "template full " template)
         model (algo-env/get-model env)
         result (if (nom/anomaly? task)
                  task
                  (do  (fire-backtest-events model window-or-dt)
                       @viz-result))]

     (if (nom/anomaly? result)
       (plot/anomaly result)
       result))))

(defn start-template
  "starts new algo via the web ui.
   this creates a viz-task once and then starts pushing all results to the websocket.
   returns task-id or nom/anomaly"
  ([this template mode]
   (start-template this template mode (nano-id 6)))
  ([{:keys [env-live subscriptions-a websocket telegram] :as this} template mode task-id]
   (info "start template-id: " (:id template) "mode: " mode)
   (if env-live
     (let [result-fn (partial process-viz-result websocket telegram)
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

(defn task-included? [{:keys [error mode]
                       :or {error :*
                            mode :*} :as filter-options}
                      {:keys [error?] :as task-summary}]
  (let [task-mode (:mode task-summary)]
    (and
     (case error
       :* true
       :only-error error?
       :only-valid (not error?)
       true)
     (case mode
       :* true
       (= mode task-mode)))))

(defn task-summary
  [{:keys [subscriptions-a] :as this} & [options]]
  (let [tasks (vals @subscriptions-a)
        options (or options {})
        task-summaries (map summarize-task tasks)]
    (filter #(task-included? options %) task-summaries)))