(ns ta.viz.trade.nav-chart
  (:require
   [quanta.viz.plot.vega :as plot]))

(defn nav-chart [roundtrip-ds]
  (let [cols [:exit-date :cum-ret-volume]
        spec {:width "700"
              :height "550"
              :description "NAV"
              :mark "line"
              :encoding  {:y {:field :cum-ret-volume
                              :type "quantitative"
                              :color "blue"}
                          :x {:type "temporal"
                              :field :exit-date}}}]
    (plot/vega
     {:spec spec
      :cols cols}
     roundtrip-ds)))