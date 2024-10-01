(ns quanta.algo.demo
  (:require
   [quanta.algo.dag :as dag]
   [quanta.algo.dag.util :as util]))

(def model
  (-> (dag/create-dag {:log-dir ".data/"})
      (dag/add-constant-cell :a 2)
      (dag/add-constant-cell :b 3)
      (dag/add-constant-cell :c 5)
      (dag/add-formula-cell :d + [:a :b])
      (dag/add-formula-cell :e * [:c :d])
      (dag/add-formula-cell :f * [:e :d :a :b])
      ))

(dag/get-current-value model :a)
(dag/get-current-value model :b)
(dag/get-current-value model :d)
(dag/get-current-value model :e)
(dag/get-current-value model :f)





