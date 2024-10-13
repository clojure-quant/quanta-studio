(ns quanta.studio.page.main
  (:require
   [ta.viz.lib.ui :refer [link-dispatch link-href]]))

; main page 

(defn main-page [{:keys [_route-params _query-params _handler] :as _route}]
  [:div
   ; quanta studio main page
   [:div.bg-blue-300.m-5
    [:h1.text-xl.text-red-600 "quanta studio "]

    [link-dispatch [:bidi/goto 'quanta.studio.page.calendar/calendar-page] "calendars"]
    [link-dispatch [:bidi/goto 'quanta.studio.page.preload/preload-page] "preload"]
    [link-dispatch [:bidi/goto 'quanta.studio.page.algo/algo-page] "algo"]
    [link-dispatch [:bidi/goto 'quanta.studio.page.bruteforce/page] "bruteforce"]
    [link-dispatch [:bidi/goto 'quanta.studio.page.tasks/tasks-page] "running tasks"]

   ; documentation is relevant to the user.
    [:h1.text-xl.text-red-600 "documentation"]
    [link-dispatch [:bidi/goto 'docy.page/docy-page] "docy"]

    [:h1.text-xl.text-red-600 "notebook repl"]
    [link-dispatch [:bidi/goto 'reval.page.repl/repl-page] "repl"]]

;
   ])