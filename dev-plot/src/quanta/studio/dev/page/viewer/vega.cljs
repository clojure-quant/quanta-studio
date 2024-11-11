(ns quanta.studio.dev.page.viewer.vega
  (:require
   [rtable.viewer.vega :refer [vegalite]]))

(def spec
  {:width "500" ;"100%"
   :height "200" ;"100%"
   :description "NAV Plot"
   :mark "line"
   :encoding  {;:x "ordinal" ;{:field "index" :type "quantitative"}
               :x {:field "index"
                   :type "ordinal"}
                         ;:x date-axes
               :y {:field "nav", :type "quantitative"}
                         ;:color "blue"
               }})

(def data
  [{:nav 100.0 :index 1}
   {:nav 120.0 :index 2}
   {:nav 150.0 :index 3}
   {:nav 120.0 :index 4}
   {:nav 140.0 :index 5}
   {:nav 150.0 :index 6}
   {:nav 160.0 :index 7}
   {:nav 150.0 :index 8}])

(def data2
  {:table [{:a "A" :b 28} {:a "B" :b 55} {:a "C" :b 43} {:a "D" :b 91}
           {:a "E" :b 81} {:a "F" :b 53} {:a "G" :b 19} {:a "H" :b 87}
           {:a "I" :b 52} {:a "J" :b 127}]})

(def bar2
  {;:$schema "https://vega.github.io/schema/vega-lite/v4.json"
   :description "A simple bar chart with embedded data."
   :mark {:type "bar"
          ;:tooltip true
          :tooltip {:content "data"}}
   :encoding {:x {:field "a" :type "ordinal"}
              :y {:field "b" :type "quantitative"}}
   :data {:name "table"}})

(defn page [{:keys [_route-params _query-params _handler] :as _route}]
  [:div.h-screen.w-screen.bg-red-500
   [vegalite (assoc spec :data {:values data})]
   [vegalite {:spec bar2 :data data2}]])