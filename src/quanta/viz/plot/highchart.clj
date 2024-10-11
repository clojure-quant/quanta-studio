(ns quanta.viz.plot.highchart
  (:require
   [tick.core :as t]
   [tablecloth.api :as tc]
   [quanta.viz.chart-spec :refer [chart-pane-spec? chart-cols]]
   [quanta.viz.plot.highchart.data :refer [convert-data]]
   [quanta.viz.plot.highchart.spec :refer [highchart-spec]]))

(defn highstock
  "returns a plot specification {:render-fn :spec :data}. 
   The ui shows a highchart from bar-algo-ds (tml/ds)
   with extra specified columns plotted with a specified style/position.
   spec must follow chart-pane format."
  [spec bar-algo-ds]
  (let [chart-spec (or (:chart spec) {})
        pane-spec (:charts spec)]
    (assert (chart-pane-spec? pane-spec) "please comply with chart-pane-spec")
    ^{:render-fn 'quanta.viz.render.core/render-spec} ; needed for notebooks
    {:render-fn 'quanta.viz.render.highcharts/highstock
     :data (-> bar-algo-ds
               (tc/select-columns (chart-cols pane-spec))
               (convert-data pane-spec))
     :spec (highchart-spec chart-spec pane-spec)}))
