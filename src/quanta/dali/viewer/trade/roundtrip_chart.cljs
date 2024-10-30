(ns quanta.dali.viewer.trade.roundtrip-chart
  (:require
   [tech.v3.dataset :as tds]
   [ui.vega :refer [vegalite]]))

(def bar
  {;:$schema "https://vega.github.io/schema/vega-lite/v4.json"
   :description "A simple bar chart with embedded data."
   :height "800"
   :width "1200"
   :transform [{:calculate "-datum.drawdown_prct" :as "drawdownprct2"}
    ;{"filter": "datum.b2 > 60"}
               ]

   :vconcat [{:height 500 ; js/null  ; Allows the line chart to take all available space
              :width "1200" ; Makes the line chart full width
              :mark {:type "line"
                     :interpolate "step-after"
                     ;:tooltip true
                     :tooltip {:content "data"}}
              :encoding {:x {:field "exit-date" :type "temporal"}
                         :y {:field "equity"
                             :type "quantitative"
                             :domain false}}}
             {:height "100"
              :width "1200" ; Makes the line chart full width
              :mark {:type "bar"
                     :tooltip {:content "data"}}
              :encoding {:x {:field "exit-date" :type "temporal"
                             :title "trade-pl"}
                         :y {:field "pl" :type "quantitative"
                             :title "trade-pl"}}}
             {:height "100"
              :width "1200" ; Makes the line chart full width
              :mark {:type "line"
                     :interpolate "step-after"
                     :tooltip {:content "data"}}
              :encoding {:x {:field "exit-date" :type "temporal"
                             :title "drawdown prct"}
                         :y {:field "drawdown-prct" :type "quantitative"
                             :title "drawdown prct"
                             :scale {; "domain": [-1, 4], 
                                     ;:reverse true ; this crashes.
                                     }
                             ; todo: drawdown should be shown inverse.
                             }}}]

   :data {:name "table"}})

(defn transform [ds]
  (->> (tds/rows ds)
       (into [])))

(defn roundtrip-chart [ds]
  (if ds
    [vegalite {:spec bar :data {:table (transform ds)}}]
    [:p "loading data.."]))