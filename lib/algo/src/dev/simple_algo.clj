(ns dev.simple-algo
   (:require
    [quanta.dag.core :as dag]
    [quanta.dag.algo.spec :as spec]
    [quanta.dag.algo.spec-options :refer [apply-options]]
    ))

(def algo-simple 
  {:calendar [:forex :d]
   :algo  identity
   :bardb :nippy
   :asset "EUR/USD"
   :trailing-n 2000
   ; algo specific parameters
   :atr-m 0.75
   :atr-n 10})



(apply-options algo-simple {[:atr-m] 2})

