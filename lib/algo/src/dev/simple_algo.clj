(ns dev.simple-algo
   (:require
    [quanta.dag.core :as dag]
    [quanta.algo.dag.spec :refer [spec->ops]]
    [quanta.algo.options :refer [apply-options]]))


(defn calc-simple [opts dt]
  {:result dt
   :opts opts})

(def algo-simple 
  {:calendar [:crypto :m]
   :algo  calc-simple
   :x 3
   :y :b
   :z nil})

(spec->ops algo-simple)

(apply-options algo-simple {[:x] 2
                            [:z] 5
                            })
;; => {:calendar [:forex :m], 
;;     :algo #function[dev.simple-algo/calc-simple], 
;;     :x 2, 
;;     :y :b, 
;;     :z 5}

