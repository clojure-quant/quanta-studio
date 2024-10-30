(ns quanta.studio.view.result
  (:require
   [dali.viewer :refer [viewer viewer2]]
   [quanta.studio.view.state :refer [get-result-a]]))

(defn error-view [error-message]
  [:div.h-full.w-full.bg-blue-100
   "no viz-result-data received yet!"])

(defn result-view [state]
  (let [result-a (get-result-a state)]
    (if result-a
      [:div.w-full.h-full.bg-red-200
          ;[:p "topic: " (str topic)]
          ;[:p "viz-spec: "  (pr-str result)]
       (if @result-a
         [viewer2 @result-a] ; viewer is not updating after the initial render.
         [error-view "result-a has value of nil."])]
      [error-view "result-a has not been initialized."])))
