(ns quanta.studio.page.layout
  (:require
   [ui.flexlayout :refer [create-model layout add-node get-data]]
   [goldly.service.core :refer [clj]]
   [quanta.studio.layout.algo] ; side-effects to register components
   ))

(def model-empty
  {:global {:tabEnableRename true
            :tabEnableClose true
            :tabEnableFloat false
            :tabSetEnableActiveIcon true}
   :layout {:type "row"
            :weight 100
            :children [{:type "tabset"
                        :weight 50
                        :children [{:type "tab"
                                    :name "One"
                                    :component "url"
                                    :icon "/r/images/add.svg"
                                    :helpText "this tab has helpText defined"
                                    :id "quanta-github"}]}]}
   :borders [{:type "border"
              ;:selected 13,
              :size 350
              :location "left"
              :children [{:type "tab"
                          :id "options"
                          :name "Options"
                          :component "option"
                          :icon "/r/quanta/adjustments-vertical.svg"
                          :enableClose false}]}]})

(def m (create-model
        {:model model-empty
         :options {"quanta-github" "https://github.com/clojure-quant/algo-alex/issues"}}))

(def layout-name-a (atom nil))

(defn save-layout []
  (let [data (get-data m)]
    (println "saving layout " @layout-name-a)
    (clj 'quanta.studio.layout.core/save-layout @layout-name-a data)))

(defn page [{:keys [route-params query-params handler] :as route}]
  [:div.h-screen.w-screen
   {:style {:display "flex"
            :flex-direction "column"
            :flex-grow 1}}
   [:div {:dir "ltr"
          :style {:margin "2px"
                  :display "flex"
                  :align-items "center"}}

    [:input {:type "text"
             :placeholder "no name"
             :style {:width "150px"
                     :min-width "150px"
                     :max-width "150px"}
             :on-change (fn [e]
                          (let [v (-> e .-target .-value)]
                            (println "textbox value: " v)
                            (reset! layout-name-a v)))
             :on-key-up (fn [e]
                          (println "key-up: " e)
                          (when (or (= (.-key e) "Enter")
                                    (= (.-keyCode e) 13))
                            (println "Enter pressed")
                            (save-layout)))}]

    [:svg {:fill "none"
           :viewBox "0 0 24 24"
           :stroke-width "1.5"
           :stroke "currentColor"
           :width "24px"
           :height "24px"
           :on-click #(add-node m {:component "calendar"
                                   :icon "/r/quanta/calendar-days.svg",
                                   :name "calendar"})}
     [:path {:d "M6.75 3v2.25M17.25 3v2.25M3 18.75V7.5a2.25 2.25 0 0 1 2.25-2.25h13.5A2.25 2.25 0 0 1 21 7.5v11.25m-18 0A2.25 2.25 0 0 0 5.25 21h13.5A2.25 2.25 0 0 0 21 18.75m-18 0v-7.5A2.25 2.25 0 0 1 5.25 9h13.5A2.25 2.25 0 0 1 21 11.25v7.5m-9-6h.008v.008H12v-.008ZM12 15h.008v.008H12V15Zm0 2.25h.008v.008H12v-.008ZM9.75 15h.008v.008H9.75V15Zm0 2.25h.008v.008H9.75v-.008ZM7.5 15h.008v.008H7.5V15Zm0 2.25h.008v.008H7.5v-.008Zm6.75-4.5h.008v.008h-.008v-.008Zm0 2.25h.008v.008h-.008V15Zm0 2.25h.008v.008h-.008v-.008Zm2.25-4.5h.008v.008H16.5v-.008Zm0 2.25h.008v.008H16.5V15Z"
             :stroke-linecap "round"
             :stroke-linejoin "round"}]]

    [:button
     {:on-click #(add-node m {:component "data"
                              :icon "/r/images/article.svg",
                              :name "Grid-added"})
      :style {:border-radius "5px"
              :border "1px solid lightgray"}}
     "add data"]

    [:button
     {:on-click #(add-node m {:component "url"
                              :icon "/r/images/article.svg",
                              :name "duck"
                              :options "https://kibot.com"
                              :id "duck1"})
      :style {:border-radius "5px"
              :border "1px solid lightgray"}}
     "kibot"]

    [:button
     {:on-click #(add-node m {:component "algo"
                              :icon "/r/images/article.svg",
                              :name "algo1"
                              :id "algo1"
                              :options {[0 :asset] "USD/JPY",
                                        [2 :trailing-n] 120,
                                        [2 :atr-n] 10,
                                        [2 :percentile] 70,
                                        [2 :step] 1.0E-4,
                                        [4 :max-open-close-over-low-high] 0.3}
                              :edit [{:type :select
                                      :path [0 :asset],
                                      :name "asset",
                                      :spec
                                      ["EUR/USD" "USD/CHF" "GBP/USD" "USD/SEK" "USD/NOK" "USD/CAD" "USD/JPY"
                                       "AUD/USD" "NZD/USD" "USD/MXN" "USD/ZAR" "EUR/JPY" "EUR/CHF" "EUR/GBP" "GBP/JPY"]}
                                     {:type :select :path [2 :trailing-n], :name "DailyLoad#", :spec [2 5 10 20 30 50 80 100 120 150]}
                                     {:type :select :path [2 :atr-n], :name "dATR#", :spec [5 10 20 30]}
                                     {:type :select :path [2 :percentile], :name "dPercentile", :spec [10 20 30 40 50 60 70 80 90]}
                                     {:type :select :path [2 :step], :name "dStep", :spec [0.001 1.0E-4 4.0E-5]}
                                     {:type :select :path [4 :max-open-close-over-low-high], :name "doji-co/lh max", :spec [0.1 0.2 0.3 0.4 0.5 0.6 0.7 0.8 0.9]}]})

      :style {:border-radius "5px"
              :border "1px solid lightgray"}}
     "add algo"]]

   [:div {:style {:display "flex"
                  :flex-grow "1"
                  :position "relative"
                  :border "1px solid #ddd"}}
    [layout m]]])