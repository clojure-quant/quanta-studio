(ns quanta.dali.plot.vega
  (:require
   [tablecloth.api :as tc]
   [tech.v3.dataset :as tds]
   [dali.spec :refer [create-dali-spec]]))

(defn convert-data [bar-algo-ds columns]
  (->> (tc/select-columns bar-algo-ds columns)
       (tds/mapseq-reader)
       (into [])))

(defn vega [{:keys [cols spec] :as opts} ds]
  (create-dali-spec
   {:viewer-fn 'ui.vega/vegalite
    :data (assoc spec
                 :data {:values (convert-data ds cols)})}))

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

