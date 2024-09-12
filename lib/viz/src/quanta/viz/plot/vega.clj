(ns quanta.viz.plot.vega
  (:require
   [tablecloth.api :as tc]
   [tech.v3.dataset :as tds]))

(defn convert-data [bar-algo-ds columns]
  (->> (tc/select-columns bar-algo-ds columns)
       (tds/mapseq-reader)
       (into [])))

(defn vega
  "returns a plot specification {:render-fn :spec :data}. 
   plot shows a tml/dataset as a vega-chart 
   vega-spec must follow vega-spec format."
  [{:keys [cols spec] :as vega-spec} bar-algo-ds]
  (when bar-algo-ds
    ^{:render-fn 'ta.viz.renderfn/render-spec} ; needed for notebooks
    {:render-fn 'ta.viz.renderfn.vega/vega-lite
     :data {:values (convert-data bar-algo-ds cols)}
     :spec spec}))

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

