(ns quanta.algo.demo.live
  (:require
    [missionary.core :as m]
    [quanta.algo.dag :as dag]
    [quanta.algo.mode.live.scheduler :refer [get-calendar-flow]]))

(m/? (->> (get-calendar-flow [:forex :m])
          (m/eduction
           (remove dag/is-no-val?))
          (m/eduction
           (take 2))
          (m/reduce conj)))

(def dag-rt
  (-> (dag/create-dag {:log-dir ".data/"})
      (dag/add-constant-cell :asset "QQQ")
      (dag/add-cell :dt (get-calendar-flow [:forex :m]))
      (dag/add-formula-cell :quote (fn [asset dt]
                                     {:asset asset
                                      :dt dt
                                      :price (rand 100)}) [:asset :dt])))


(dag/start-log-cell dag-rt :dt)
(dag/start-log-cell dag-rt :quote)
(dag/start-log-cell dag-rt :asset)

(dag/stop-log-cell dag-rt :dt)
(dag/stop-log-cell dag-rt :quote)
(dag/stop-log-cell dag-rt :asset)

dag-rt