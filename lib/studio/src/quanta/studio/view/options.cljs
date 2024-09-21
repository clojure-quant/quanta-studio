(ns quanta.studio.view.options
  (:require
   [options.core :as o]
   [quanta.studio.view.state :as s]
   [quanta.studio.view.asset-picker :refer [editor-asset-picker]]))

(o/register-editor :asset-picker editor-asset-picker)

(defn make-button-backtest  [state]
  {:type :button
   :name "Calc!"
   :class "bg-blue-500 hover:bg-blue-700 text-white font-bold rounded" ; py-2 px-4
   :on-click #(s/calculate state)})

(defn make-button-start  [state]
  {:type :button
   :name "Start!"
   :class "bg-blue-500 hover:bg-blue-700 text-white font-bold rounded" ; py-2 px-4
   :on-click #(s/start state)})

(defn mode-selector [views]
  {:type :select
   :path :mode
   :name "Mode"
   :spec views})

(def dt-selector
  {:type :string
   :path :dt
   :name "DateTime"})

(defn options-ui [state]
  (let [options-a (s/get-view-a state :options)
        current-a (s/get-view-a state :current)
        views-a (s/get-view-a state :views)]
    (fn [state]
      (let [options (->> (concat (:options @options-a) [(mode-selector @views-a)
                                                        dt-selector
                                                        (make-button-backtest state)
                                                        (make-button-start state)])
                         (into []))
            config (assoc @options-a :state current-a :options options)]
        [o/options-ui {:class "bg-blue-300 options-label-left" ; options-debug
                       :style {:width "100%"
                               :height "50px"}} config]
        ;[:span "options-ui: " (pr-str config)]
        ))))
