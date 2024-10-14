(ns quanta.viz.render.trade.core
  (:require
   [container :refer [tab]]
   [quanta.viz.render.table.rtable :refer [rtable]]
   [quanta.viz.render.vega :refer [vega-lite]]
   [quanta.viz.render.trade.metrics :refer [metrics-view]]
   [quanta.viz.render.trade.roundtrips :refer [roundtrips-cheetah]]
   [quanta.viz.render.transit :refer [loading-ui]]))

(defn roundtrip-stats-ui [{:keys [style class intraday?]
                           :or {intraday? false
                                style {:height "100%" ;:height "600px"
                                       :width "100%" ; :width "800px"
                                       }
                                class "bg-blue-100"}
                           :as spec}
                          {:keys [metrics chart roundtrip-ds] :as data}]
  ;(println "rountrip table spec: " (:spec rt) " data: " data)
  (with-meta
    [tab {:class class
          :style style}
     "metrics"
     [metrics-view metrics]
     ;"chart"
     ;[vega-lite (:spec chart) (:data chart)]
     "roundtrips"
     [roundtrips-cheetah roundtrip-ds]
     ;[rtable (:spec rt) (:data rt)]
     ]
    {:R true}))

(defn roundtrip-stats-ui-ds [opts data]
  ;{:render-fn quanta.viz.render.trade.core/roundtrip-stats-ui-ds, 
  ; :data {:id I-JjQ, 
  ;        :url /r/ds/I-JjQ.transit-json, 
  ;        :filename ./data/public/ds/I-JjQ.transit-json}, 
  ; :spec {}} 
  [loading-ui opts data roundtrip-stats-ui])