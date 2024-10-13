(ns quanta.studio.view.bruteforce)

(defn bruteforce-result-ui [{:keys [label template-id calculated algo variations result]}]
  [:div.grid.grid-cols-2
   [:p "label"] [:p label]
   [:p "template-id"] [:p template-id]
   [:p "calculated"] [:p calculated]
   [:p "variations"] [:p (pr-str variations)]
   [:p "result"] [:p (count result)]])


;;:variations [[0 :asset] ["BTCUSDT" "ETHUSDT"] [2 :day :atr-n] [20 50]],
{[0 :asset] "BTCUSDT",
 [2 :day :atr-n] 50,
 :target 0.5148529360496968,
 :trades 178,
 :cum-pl -31625.764798952816,
 :max-dd 32251.10599895282,
 :id "YRajWa"} "