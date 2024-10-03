(ns dev.algo-multi-start
  (:require
   [tick.core :as t]
   [quanta.dag.core :as dag]
   [quanta.algo.core :as create]
   [dev.algo-multi :refer [multi-algo]]))

;; SNAPSHOT ************************************************************

(def multi
  (create/create-dag-snapshot
   {:log-dir ".data/"
    :env {}}
   multi-algo
   (t/instant)))

(dag/cell-ids multi)
;; => ([:crypto :d] :day [:crypto :m] :min :signal)

;; this gets written to the logfile of the dag.
(dag/start-log-cell multi [:crypto :m])
(dag/start-log-cell multi [:crypto :d])
(dag/start-log-cell multi :day)
(dag/start-log-cell multi :min)
(dag/start-log-cell multi :signal)

; for a snapshot calculation, the task calculation is so fast,
; that most likely all tasks are already terminated, so the 
; next 3 froms will not do anything.
(dag/running-tasks multi)
(dag/stop! multi [:crypto :m])
(dag/stop-all! multi)

;; LIVE ****************************************************************

(def multi-rt
  (create/create-dag-live
   {:log-dir ".data/"
    :env {}}
   multi-algo))

; you just start whatever you want to log.
; you can start :signal only, which automatically will calculate
; all the cells in the dag, but will only log signal.
(dag/start-log-cell multi-rt [:crypto :m])
(dag/start-log-cell multi-rt [:crypto :d])
(dag/start-log-cell multi-rt :day)
(dag/start-log-cell multi-rt :min)
(dag/start-log-cell multi-rt :signal)

(dag/running-tasks multi-rt)
(dag/stop-all! multi-rt)





