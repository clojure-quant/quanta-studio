(ns quanta.studio.layout.algo
  (:require
   [reagent.core :as r]
   [tick.core :as t]
   [ui.flexlayout :refer [component-ui get-data]]
   [ui.frisk :refer [frisk]]
   [re-flow.core :refer [flow-ui]]
   [dali.cljviewer :refer [clj-viewer]]))

(defmethod component-ui "help" [{:keys [id]}]
  (fn [options]
    [clj-viewer {:fun  'quanta.dali.plot.md/md
                 :args ["docy/quanta-studio-layout.md"]}]))

(defmethod component-ui "calendar" [{:keys [id]}]
  (fn [options]
    [flow-ui {:clj 'quanta.studio.calendar/calendar-time
              :args []
              :render 'quanta.studio.view.calendar/calendar-ui}]))

(defmethod component-ui "algo" [{:keys [id] :as opts}]
  (fn [options]
    (let [template-id (:template-id options)
          options (dissoc options :template-id)]
;      calculate template: docy/quanta-studio-layout.md mode:  {[0 :asset] "BTCUSDT", [2 :atr-n] 10} dt:  :chart  options:  :bollinger

      [clj-viewer {:fun  'quanta.studio.template.calculate/calculate
                   :args [template-id
                          options
                          :chart ; (get-mode state)
                          (t/instant)]}]
      #_[:div
         "I am an algo: " (name template-id)
     ;(pr-str opts)
         [:br]
         "options"
         [:br]
         (pr-str options)])))

(defmethod component-ui "data" [{:keys [id state]}]
  (let [data-a (r/atom nil)
        fetch (fn []
                (println "fetching data..")
                (reset! data-a (get-data state)))]
    (fn [options]
      [:div.bg-red-200
       "I can show all the data of the layout:"
       [:br]
       [:button.bg-blue-400.border-round.border {:on-click #(fetch)} "get-data"]
       [:hr]
       "data"
       ;[:hr]
       ;(pr-str @data-a)
       [:hr]
       [frisk @data-a]])))