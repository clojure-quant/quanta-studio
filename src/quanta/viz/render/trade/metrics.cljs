(ns quanta.viz.render.trade.metrics
  (:require
   [quanta.viz.format :as f]))

(defn to-fixed [n d]
  (.toFixed n d))

(defn round-number-digits
  [digits number] ; digits is first parameter, so it can easily be applied (data last)
  (try
    (if (nil? number) "" (to-fixed number digits))
    (catch js/Error e
      (println "round-numnber-digits exception: " e)
      number)))

(defn metrics-view [{:keys [class style roundtrip nav]
                     :or {class "w-full h-full"
                          style {}}}]
  (let [{:keys [pf all win loss]} roundtrip
        {:keys [equity-final cum-pl fee-total
                max-drawdown max-drawdown-prct]} nav]
    [:div {:class class :style style}
     [:table
      [:tr
       [:td "equity final"]
       [:td (f/nr-format-0-digits equity-final)]]
      [:tr
       [:td "cum-pl"]
       [:td (f/nr-format-0-digits  cum-pl)]]
      [:tr
       [:td "max-dd"]
       [:td (f/nr-format-0-digits max-drawdown) " - " (f/nr-format-0-digits max-drawdown-prct)]]
      [:tr
       [:td "profit factor "]
       [:td pf]]
      [:tr
       [:td "fees"]
       [:td  (f/nr-format-0-digits fee-total)]]]
     [:table
      [:tr
       [:td {:style {:width "3cm"}} " "]
       [:td {:style {:width "100"}} "all"]
       [:td {:style {:width "100"}} "win"]
       [:td {:style {:width "100"}} "loss"]]
      [:tr
       [:td "#trades"]
       [:td (:trades all) [:span {:class "text-blue-500"
                                  :style {:float "right"}}
                           (-> all :trade-prct f/nr-format-0-digits)]]
       [:td (:trades win) [:span {:class "text-blue-500"
                                  :style {:float "right"}}
                           (-> win :trade-prct f/nr-format-0-digits)]]
       [:td (:trades loss) [:span {:class "text-blue-500"
                                   :style {:float "right"}}
                            (-> win :trade-prct f/nr-format-0-digits)]]]
      [:tr
       [:td "pl"]
       [:td (-> all :pl f/nr-format-0-digits)]
       [:td (-> win :pl f/nr-format-0-digits)]
       [:td (-> loss :pl f/nr-format-0-digits)]]
      [:tr
       [:td "median pl/trade"]
       [:td (-> all :pl-mean f/nr-format-auto)]
       [:td (-> win :pl-mean f/nr-format-auto)]
       [:td (-> loss :pl-mean f/nr-format-auto)]]
      [:tr
       [:td "bars avg [total]"]
       [:td (-> all :bar-avg f/nr-format-0-digits) [:span {:class "text-blue-500"
                                                           :style {:float "right"}}
                                                    (str "[" (:bars all) "]")]]
       [:td (-> win :bar-avg f/nr-format-0-digits)
        [:span {:class "text-blue-500"
                :style {:float "right"}}
         (str "[" (:bars win) "]")]]
       [:td (-> loss :bar-avg f/nr-format-0-digits)
        [:span {:class "text-blue-500"
                :style {:float "right"}}
         (str "[" (:bars loss) "]")]]]]]))

