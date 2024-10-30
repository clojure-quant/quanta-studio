(ns quanta.studio.layout.algo
  (:require
   [reagent.core :as r]
   [ui.flexlayout :refer [component-ui get-data]]
   [ui.frisk :refer [frisk]]
   [re-flow.core :refer [flow-ui]]))

(defmethod component-ui "calendar" [{:keys [id]}]
  (fn [options]
    [flow-ui {:clj 'quanta.studio.calendar/calendar-time
              :args []
              :render 'quanta.studio.view.calendar/calendar-ui}]))

(defmethod component-ui "algo" [{:keys [id]}]
  (fn [options]
    [:div
     "I am an algo"
     [:br]
     "options"
     [:br]
     (pr-str options)]))

(defmethod component-ui "data" [{:keys [id state]}]
  (let [data-a (r/atom nil)
        fetch (fn []
                (println "fetching data..")
                (reset! data-a (get-data state)))]
    (fn [options]
      [:div
       "I can show the data of the layout:"
       [:br]
       [:button {:on-click #(fetch)} "get-data"]
       [:hr]
       "data"
       ;[:hr]
       ;(pr-str @data-a)
       [:hr]
       [frisk @data-a]])))