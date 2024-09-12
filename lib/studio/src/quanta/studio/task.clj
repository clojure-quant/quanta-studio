(ns quanta.studio.task
  (:require
   [de.otto.nom.core :as nom]
   [taoensso.timbre :as log :refer [tracef debug debugf info infof warn error errorf]]
   [modular.ws.core :refer [send-all!]]
   [quanta.alert :refer [alert? alert-data? alert->telegram-message]]
   [telegram.pubsub :as tpubsub]
   [quanta.viz.plot.anomaly :as plot]))

;; result fns 

(defn process-viz-result [websocket telegram template-id task-id result]
  (try
    (let [error? (nom/anomaly? result)
          result (if error?
                   (plot/anomaly result)
                   result)]
      (info "process viz-result template-id: " template-id " task-id: " task-id (if error? " anomaly!" " success"))

      (when (alert? result)
        (let [msg (alert->telegram-message result)]
          (warn "sending telegram-trade-signal: " msg)
          (tpubsub/publish telegram "trade-signal" msg)))

      (when (alert-data? result)
        (let [msg (alert->telegram-message result)]
          (warn "sending telegram-signal-debug: " msg)
          (tpubsub/publish telegram "trade-signal-debug" msg)))

      (send-all! websocket [:interact/subscription {:task-id task-id :result result}]))
    (catch Exception ex
      (error "process-result exception: " ex)))
  ; make sure we never retrun something. result ends up in a javelin cell
  nil)