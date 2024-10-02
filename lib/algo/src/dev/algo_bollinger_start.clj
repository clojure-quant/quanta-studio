(ns dev.algo-bollinger-start
  (:require
   [tick.core :as t]
   [quanta.dag.core :as dag]
   [quanta.algo.env.bars]
   [quanta.algo.create :refer [create-dag-live create-dag-snapshot]]
   [ta.import.provider.bybit.ds :as bybit]
   [ta.db.bars.protocol :as b]
   [ta.calendar.core :refer [trailing-window]]
   [dev.algo-bollinger :refer [bollinger-algo]]
   ))


(def bar-db (bybit/create-import-bybit))

(def bollinger
  (create-dag-snapshot
   {:log-dir ".data/"
    :env {#'quanta.algo.env.bars/*bar-db* bar-db}}
   bollinger-algo
   (t/instant)))

(dag/start-log-cell (:dag bollinger) :day)
(dag/start-log-cell (:dag bollinger) :min)

(def bollinger-rt
  (create-dag-live
   {:log-dir ".data/"
    :env {#'quanta.algo.env.bars/*bar-db* bar-db}}
   bollinger-algo))

(dag/cell-ids (:dag bollinger-rt))
;; => ([:crypto :d] :day [:crypto :m] :min :signal)


(dag/start-log-cell (:dag bollinger-rt) [:crypto :d])
(dag/start-log-cell (:dag bollinger-rt) [:crypto :m])
(dag/stop-log-cell (:dag bollinger-rt) [:crypto :m])

(dag/start-log-cell (:dag bollinger-rt) :day)
; undefined continuous flow.

(dag/start-log-cell (:dag bollinger-rt) :min)
; undefined continuous flow.




(dag/cell-ids (:dag bollinger-rt))
;; => ([:crypto :d] :day [:crypto :m] :min :signal)

(dag/get-current-valid-value (:dag bollinger-rt) [:crypto :m])
(dag/get-current-valid-value (:dag bollinger-rt) :min)



(with-bindings [quanta.algo.env.bars/*bar-db* bar-db]
 (quanta.algo.env.bars/get-trailing-bars 
  {:asset "BTCUSDT"
   :calendar [:forex :m]
   :trailing-n 10}
   (t/instant)))

(with-bindings [quanta.algo.env.bars/*bar-db* bar-db]
  (quanta.algo.env.bars/get-trailing-bars
   {:asset "BTCUSDT"
    :calendar [:forex :m]
    :trailing-n 10}
   (t/zoned-date-time "2024-10-02T00:29Z[UTC]")))

(trailing-window [:crypto :m] 2)
;; => (#time/zoned-date-time "2024-10-02T00:31Z[UTC]"
;;     #time/zoned-date-time "2024-10-02T00:30Z[UTC]")


(trailing-window [:crypto :m] 2
                 (t/zoned-date-time "2024-10-02T00:29Z[UTC]")
                 )
;; => (#time/zoned-date-time "2024-10-02T00:29Z[UTC]"
;;     #time/zoned-date-time "2024-10-02T00:28Z[UTC]")

(trailing-window [:crypto :m] 2
                 (t/instant "2024-10-02T00:29:00Z"))
;; => (#time/zoned-date-time "2024-10-02T00:29Z[UTC]" 
;;     #time/zoned-date-time "2024-10-02T00:28Z[UTC]")

(-> (t/zoned-date-time "2024-10-02T00:29Z[UTC]")
    (t/instant)
 )
;; => #time/ 

 

