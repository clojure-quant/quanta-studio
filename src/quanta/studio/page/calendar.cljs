(ns quanta.studio.page.calendar
  (:require
   [quanta.studio.lib.link :refer [link-dispatch link-href]]
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







