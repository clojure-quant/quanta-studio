(ns quanta.viz.render.trade.core
  (:require
   [container :refer [tab]]
   [quanta.viz.render.rtable :refer [rtable]]
   [quanta.viz.render.vega :refer [vega-lite]]
   [quanta.viz.render.trade.metrics :refer [metrics-view]]))

(defn roundtrip-stats-ui [{:keys [style class intraday?]
                           :or {intraday? false
                                style {:height "600px"
                                       :width "800px"}
                                class "bg-red-500"}
                           :as spec}
                          {:keys [metrics chart rt] :as data}]
  (println "rountrip table spec: " (:spec rt))
  (with-meta
    [tab {:class class
          :style style}
     "metrics"
     [metrics-view metrics]
     "chart"
     [vega-lite (:spec chart) (:data chart)]
     "roundtrips"
     [rtable (:spec rt) (:data rt)]]
    {:R true}))
