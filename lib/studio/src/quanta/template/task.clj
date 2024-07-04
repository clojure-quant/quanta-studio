(ns quanta.template.task
  (:require
   [de.otto.nom.core :as nom]
   [taoensso.timbre :as log :refer [tracef debug debugf info infof warn error errorf]]
   [ta.helper.date :refer [now]]
   [ta.algo.env.protocol :as algo-env]
   [ta.algo.error-report :refer [save-error-report]]
   [ta.algo.compile :refer [compile-symbol]]
   [quanta.model.protocol :as p]))

(defn create-viz-fn [{:keys [id] :as template} mode]
  ;(info "create-viz-fn: " viz)
  (let [{:keys [viz viz-options]} (get template mode)
        viz-fn (compile-symbol viz)]
    (if (nom/anomaly? viz-fn)
      viz-fn
      (fn [result]
        (if (nom/anomaly? result)
          result
          (try
            (info "calculating visualization:" id " .. ")
            ;(warn "result: " result)
            (let [r (if viz-options
                      (viz-fn viz-options result)
                      (viz-fn result))]
              (debug "calculating visualization:" id " DONE!")
              r)
            (catch Exception ex
              (let [filename (save-error-report "viz" template ex)]
                (error "algo-viz " id " exception. details: " filename)
                (nom/fail ::algo-calc {:message "algo viz exception!"
                                       :filename filename
                                       :location :visualize})))))))))

(defn start-task [env {:keys [id algo key] :as template} mode task-id result-fn]
  (let [algo-results-a (algo-env/add-algo env algo)
        viz-fn (create-viz-fn template mode)
        err (or (when (nom/anomaly? algo-results-a) algo-results-a)
                (when (nom/anomaly? viz-fn) viz-fn))]
    (if err
      (let [filename (save-error-report (str "create-algo-viz-task" id mode) err (:ex err))]
        (error "create-algo-viz-task" id algo mode " error! details: " filename)
        err)
      (let [algo-result-a (if key (key algo-results-a)
                              algo-results-a)
            model (algo-env/get-model env)
            viz-result-a (p/formula-cell model viz-fn [algo-result-a])
            pusher-a (p/formula-cell model #(result-fn id task-id %) [viz-result-a])]
          ;_ (info "algo-result-a: " algo-result-a)
        {:start-dt (now)
         :task-id task-id
         :template template
         :algo-result algo-result-a
         :viz-result viz-result-a
         :pusher pusher-a}))))

(defn stop-task [env {:keys [template algo-result viz-result pusher] :as task}]
  (let [model (algo-env/get-model env)]
    (p/destroy-cell model pusher)
    (p/destroy-cell model viz-result)
    (p/destroy-cell model algo-result)))