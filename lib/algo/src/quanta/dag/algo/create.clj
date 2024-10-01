(ns quanta.dag.algo.create
  (:require
   [quanta.dag.core :as dag]
   [quanta.dag.trace :refer [write-edn-raw]]
   [quanta.dag.algo.calendar.core :as cal]
   [quanta.dag.algo.spec :as spec]))

(defn- add-cell [d time-fn [cell-id {:keys [calendar formula
                                            algo-fn opts]}]]
  (let [algo-fn-with-opts (partial algo-fn opts)]
    (cond
      calendar
      (do (dag/add-cell d calendar (time-fn calendar))
          (dag/add-formula-cell d cell-id algo-fn-with-opts [calendar]))

      formula
      (dag/add-formula-cell d cell-id algo-fn-with-opts formula))))

(defn- add-cells [d time-fn cell-spec]
  (doall (map #(add-cell d time-fn %) cell-spec)))

(defn create-dag-live [dag-env algo-spec]
  (let [time-fn cal/live-calendar
        cell-spec (spec/spec->ops algo-spec)
        d (dag/create-dag dag-env)]
    (write-edn-raw (:logger d) "mode: live\r\nalgo-spec:" cell-spec)
    (add-cells d time-fn cell-spec)
    d))

(defn create-dag-snapshot [dag-env algo-spec dt]
  (let [time-fn (cal/calculate-calendar dt)
        cell-spec (spec/spec->ops algo-spec)
        d (dag/create-dag dag-env)]
    (write-edn-raw (:logger d) "mode: snapshot\r\nalgo-spec:" cell-spec)
    (add-cells d time-fn cell-spec)
    d))