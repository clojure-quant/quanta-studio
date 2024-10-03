(ns quanta.studio.template.task.admin
  (:require
   [de.otto.nom.core :as nom]
   [taoensso.timbre :as log :refer [tracef debug debugf info infof warn error errorf]]
   [ta.helper.date :refer [now]]
   [ta.algo.env.protocol :as algo-env]
   [ta.algo.error-report :refer [save-error-report]]
   [quanta.model.protocol :as p]
   [quanta.template.viz :refer [create-viz-fn get-viz-mode-input]]))

(defn start-task [env {:keys [id algo] :as template} mode task-id result-fn]
  (let [;_ (println 1)
        ;algo (assoc algo :task-id task-id) ; adds task-id as an option, this is used for logging.
        ;_ (println 2)
        algo-results-a (algo-env/add-algo env algo)
        ;_ (println 3)
        viz-fn (create-viz-fn template mode task-id)
        ;_ (println 4)
        err (or (when (nom/anomaly? algo-results-a) algo-results-a)
                (when (nom/anomaly? viz-fn) viz-fn))
        ;_ (println 5)
        ]
    (if err
      (let [filename (save-error-report (str "create-algo-viz-task" id mode) err (:ex err))]
        (error "create-algo-viz-task" id algo mode " error! details: " filename)
        err)
      (let [algo-result-a (get-viz-mode-input template mode algo-results-a)
            model (algo-env/get-model env)
            viz-result-a (p/formula-cell model viz-fn [algo-result-a])
            pusher-a (p/formula-cell model #(result-fn id task-id %) [viz-result-a])]
          ;_ (info "algo-result-a: " algo-result-a)
        {:start-dt (now)
         :task-id task-id
         :mode mode
         :template template
         :algo-result algo-result-a
         :viz-result viz-result-a
         :pusher pusher-a}))))

(defn stop-task [env {:keys [template algo-result viz-result pusher] :as task}]
  (let [model (algo-env/get-model env)]
    (p/destroy-cell model pusher)
    (p/destroy-cell model viz-result)
    (p/destroy-cell model algo-result)))

(defn error? [{:keys [viz-result] :as task}]
  (or (nil? viz-result)
      (nom/anomaly? @viz-result)))

(defn summarize-task [{:keys [task-id template start-dt mode] :as task}]
  (let [algo-option-keys nil
        {:keys [id algo]} template]
    {:task-id task-id
     :start-dt start-dt
     :template-id id
     :mode mode
     :error?  (if (error? task) true false)
     :algo (if algo-option-keys
             (select-keys algo algo-option-keys)
             ; if no specific keys are provide
             ; we want to remove data that is not helpful to the user.
             (dissoc algo :type :import :algo))}))