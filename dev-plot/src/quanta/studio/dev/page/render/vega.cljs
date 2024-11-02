(ns quanta.studio.dev.page.render.vega
  (:require
   [ui.vega :refer [vegalite]]))

(def spec
  {:box :sm
   :width "500" ;"100%"
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

(defn page [{:keys [_route-params _query-params _handler] :as _route}]
  [:div.h-screen.w-screen.bg-red-500
   [vegalite (assoc spec :data {:values data})]])