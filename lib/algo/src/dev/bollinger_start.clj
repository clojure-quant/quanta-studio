(ns dev.bollinger-start
  (:require
   [tick.core :as t]
   [quanta.dag.core :as dag]
   [quanta.dag.env.bars]
   [quanta.dag.algo.create :as create]
   [ta.import.provider.bybit.ds :as bybit]
   [dev.bollinger-algo :refer [bollinger-algo]]))

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
