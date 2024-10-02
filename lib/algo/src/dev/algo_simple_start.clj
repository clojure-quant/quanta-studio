(ns dev.algo-simple-start
  (:require
   [tick.core :as t]
   [quanta.dag.core :as dag]
   [quanta.algo.create :as create]
   [dev.algo-simple :refer [simple-algo]]))

;; SNAPSHOT ************************************************************

(def simple
  (create/create-dag-snapshot
   {:log-dir ".data/"
    :env {}}
   simple-algo
   (t/instant)))

(dag/cell-ids simple)
;; => ([:crypto :m] :algo)

;; when the algo-spec does only specify ONE algo, then
;; the algo result cell is called :algo


;; this gets written to the logfile of the dag.
(dag/start-log-cell simple [:crypto :m])
(dag/start-log-cell simple :algo)
(dag/start-log-cell simple :xxx)

;; LIVE ****************************************************************

(def simple-rt
  (create/create-dag-live
   {:log-dir ".data/"
    :env {}}
   simple-algo))


(dag/start-log-cell simple-rt :algo)

