(ns quanta.studio.page.main
  (:require
   [ta.viz.lib.ui :refer [link-dispatch link-href]]))

; main page 

(defn main-page [{:keys [_route-params _query-params _handler] :as _route}]
  [:div
   ; quanta studio main page
   [:div.bg-blue-300.m-5
    [:h1.text-xl.text-red-600 "quanta studio "]

    [link-dispatch [:bidi/goto 'quanta.studio.page.algo/algo-page] "interact"]
    [link-dispatch [:bidi/goto 'quanta.studio.page.tasks/tasks-page] "running tasks"]]

;
   ])