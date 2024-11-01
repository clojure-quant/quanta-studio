(ns quanta.studio.dev.page.date
  (:require
   [reagent.core :as r]
   [tick.core :as t]
   [quanta.studio.view.date :refer [dt-scroller]]))

(defn page [{:keys [_route-params _query-params _handler] :as _route}]
  (let [dt-a (r/atom (t/instant))]
    (fn [{:keys [_route-params _query-params _handler] :as _route}]
      [:div
       [dt-scroller dt-a]])))