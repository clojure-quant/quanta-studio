(ns quanta.viz.render.trade.roundtrip-chart
  (:require
   [tech.v3.dataset :as tmlds]
   [ui.vega :refer [vegalite]]))

(def bar
  {;:$schema "https://vega.github.io/schema/vega-lite/v4.json"
   :description "A simple bar chart with embedded data."
   :height "800"
   :width "1200"
   :vconcat [{:height 500 ; js/null  ; Allows the line chart to take all available space
              :width "1200" ; Makes the line chart full width
              :mark {:type "line"
                     :interpolate "step-after"
                     ;:tooltip true
                     :tooltip {:content "data"}}
              :encoding {:x {:field "exit-date" :type "temporal"}
                         :y {:field "equity" :type "quantitative"}}}
             {:height "100"
              :width "1200" ; Makes the line chart full width
              :mark {:type "bar"
                     :tooltip {:content "data"}}
              :encoding {:x {:field "exit-date" :type "temporal"}
                         :y {:field "drawdown-prct" :type "quantitative"}}}]

   :data {:name "table"}})

(defn transform [ds]
  (->> (tmlds/rows ds)
       (into [])))

(defn roundtrip-chart [ds]
  (fn []
    (if ds
      [vegalite {:spec bar :data {:table (transform ds)}}]
      [:p "loading data.."])))