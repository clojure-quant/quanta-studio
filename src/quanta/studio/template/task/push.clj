(ns quanta.studio.template.task.push
  (:require
   [de.otto.nom.core :as nom]
   [taoensso.timbre :as log :refer [tracef debug debugf info infof warn error errorf]]
   [modular.ws.core :refer [send-all!]]
   [quanta.dali.plot :as plot]))

;; result fns 

(def ^:dynamic *websocket* nil)

(defn process-viz-result [template-id task-id result]
  (try
    (let [error? (nom/anomaly? result)
          result (if error?
                   (plot/anomaly result)
                   result)]
      (info "process viz-result template-id: " template-id " task-id: " task-id (if error? " anomaly!" " success"))

      (send-all! *websocket* [:interact/subscription {:task-id task-id :result result}]))
    (catch Exception ex
      (error "process-result exception: " ex)))
  ; make sure we never retrun something. result ends up in a javelin cell
  nil)