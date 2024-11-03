(ns dev.trailing
  (:require
   [missionary.core :as m]
   [tick.core :as t]
   [quanta.bar.env]
   [modular.system]))

(def bar-engine (modular.system/system :bar-engine))

bar-engine

(def env {:bar-db bar-engine})

(m/?
 (quanta.bar.env/get-bars
  env
  {:trailing-n 100
   :calendar [:crypto :d]
   :import :bybit-parallel
   :asset "BTCUSDT"}
  {:start  (t/instant "2024-05-01T00:00:00Z")
   :end (t/instant "2024-07-01T00:00:00Z")}))

(m/?
 (quanta.bar.env/get-trailing-bars
  env
  {:trailing-n 100
   :calendar [:crypto :d]
   :import :bybit-parallel
   :asset "BTCUSDT"}
  (t/instant)))


