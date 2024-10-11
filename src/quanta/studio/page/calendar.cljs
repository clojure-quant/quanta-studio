(ns quanta.studio.page.calendar
  (:require
   [reagent.core :as r]
   [promesa.core :as p]
   [missionary.core :as m]
   [ta.viz.lib.ui :refer [link-dispatch link-href]]
   [re-flow.core :refer [re-flow flow-ui]]))

(defn calendar-page [{:keys [_route-params _query-params _handler] :as _route}]
  [:div
   ; quanta studio main page
   [:div.bg-blue-300.m-5
    [:h1.text-xl.text-red-600 "quanta studio - calendars "]
    [flow-ui {:clj 'quanta.studio.calendar/calendar-time
              :args []
              :render 'quanta.studio.view.calendar/calendar-ui}]

    [link-dispatch [:bidi/goto 'quanta.studio.page.main/main-page] "main"]]

;
   ])







