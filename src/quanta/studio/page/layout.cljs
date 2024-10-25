(ns quanta.studio.page.layout
  (:require
   [ui.flexlayout :refer [create-model layout add-node]]
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
                          :enableClose false}]}]})

(def m (create-model
        {:model model-empty
         :options {"quanta-github" "https://github.com/clojure-quant/algo-alex/issues"}}))

(defn page [{:keys [route-params query-params handler] :as route}]
  [:div.h-screen.w-screen
   {:style {:display "flex"
            :flex-direction "column"
            :flex-grow 1}}
   [:div {:dir "ltr"
          :style {:margin "2px"
                  :display "flex"
                  :align-items "center"}}

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