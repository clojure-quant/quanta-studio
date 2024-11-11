(ns quanta.dali.plot.vega
  (:require
   [tablecloth.api :as tc]
   ))



(defn- select-one [ds x col]
  (-> ds
      (tc/select-columns [x col])
      (tc/add-column :name col)
      (tc/rename-columns {col :y
                          x :x})))

(defn ds-stacked
  "produces a dataset with [:x :y :name] columns
   useful for vega-plots that colorize depending on
   the series name"
  [ds x cols]
  (let [ds-seq (map #(select-one ds x %) cols)]
    (apply tc/concat ds-seq)))

