(ns dev.bollinger
  (:require
   [tick.core :as t]
   [tablecloth.api :as tc]
   [ta.indicator.band :as band]
   [quanta.dag.core :as dag]
   [quanta.dag.env.bars :refer [get-trailing-bars]]
   [quanta.dag.algo.spec :as spec]
   [quanta.dag.algo.create :as create]
   [ta.import.provider.bybit.ds :as bybit]
   ))

(defn bollinger-calc [opts dt]
  {:bollinger-opts opts
   :dt dt}
   (->> (get-trailing-bars opts dt)
       ;(band/add-bollinger {:n 2 :k 3.0})
       
       ))

(defn bollinger-signal [opts d m]
  (vector d m))

(def bollinger-algo
  [{:asset "BTCUSDT"}
   :day {:calendar [:forex :d]
         :algo  bollinger-calc
         :trailing-n 20
         :atr-n 10
         :atr-m 0.6}
   :min {:calendar [:forex :m]
         :algo bollinger-calc
         :trailing-n 20}
   :signal {:formula [:day :min]
            :algo bollinger-signal
            :carry-n 2}])

(spec/spec->ops bollinger-algo)

(def bar-db (bybit/create-import-bybit))

(def dag-bollinger 
   (create/create-dag-snapshot 
    {:log-dir ".data/"
     :env {#'quanta.dag.env.bars/*bar-db* bar-db}}
    bollinger-algo
    (t/instant)))

(dag/start-log-cell dag-bollinger :day)
(dag/start-log-cell dag-bollinger :min)

(def dag-bollinger-rt
  (create/create-dag-live
   {:log-dir ".data/"
    :env {#'quanta.dag.env.bars/*bar-db* bar-db}}
   bollinger-algo))

(dag/start-log-cell dag-bollinger-rt :min)
