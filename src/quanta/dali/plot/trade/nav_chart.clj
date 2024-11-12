(ns quanta.dali.plot.trade.nav-chart
  (:require
   [rtable.plot.vega :as plot]))

(defn nav-chart [env roundtrip-ds]
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
    (plot/vegalite-ds
     env
     {:spec spec
      :cols cols}
     roundtrip-ds)))