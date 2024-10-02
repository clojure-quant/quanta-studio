(ns dev.simple-start
  (:require
   [tick.core :as t]
   [quanta.dag.core :as dag]
   [quanta.algo.create :as create]
   [dev.simple-algo :refer [algo-simple]]))

(def simple
  (create/create-dag-snapshot
   {:log-dir ".data/"
    :env {}}
   algo-simple
   (t/instant)))

(dag/cell-ids (:dag simple))
;; => ([:forex :m] :algo)

;; this gets written to the logfile of the dag.
(dag/start-log-cell (:dag simple) [:crypto :m])
(dag/start-log-cell (:dag simple) :algo)
(dag/start-log-cell (:dag simple) :xxx)

(def simple-rt
  (create/create-dag-live
   {:log-dir ".data/"
    :env {}}
   algo-simple))


(dag/start-log-cell (:dag simple-rt) :algo)

