(ns quanta.dali.viewer.trade.backtest
  (:require
   [container :refer [tab]]
   [quanta.dali.viewer.trade.metrics :refer [metrics-view]]
   [quanta.dali.viewer.trade.roundtrip-table :refer [roundtrips-cheetah]]
   [quanta.dali.viewer.trade.roundtrip-chart :refer [roundtrip-chart]]))

(defn backtest-ui [{:keys [style class intraday?
                           metrics #_opts #_chart roundtrip-ds]
                    :or {intraday? false
                         style {:height "100%" ;:height "600px"
                                :width "100%" ; :width "800px"
                                }
                         class "bg-blue-100"}
                    :as opts}]
  [tab {:class class
        :style style}
   "metrics"
   [metrics-view metrics #_(assoc metrics :opts opts)]
   "chart"
   [roundtrip-chart roundtrip-ds]
   "roundtrips"
   [roundtrips-cheetah roundtrip-ds]])
