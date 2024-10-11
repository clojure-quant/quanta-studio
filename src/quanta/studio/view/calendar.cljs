(ns quanta.studio.view.calendar)

(defn one [{:keys [calendar prior current next open holiday]}]
  [:<>
   [:div (str calendar)]
   [:div (str prior)]
   [:div (str current)]
   [:div (if open "OPEN" "CLOSED")]
   [:div (if holiday "HOLIDAY" "WORK")]
   [:div (str next)]])

(defn calendar-ui [{:keys [dt cals]}]
  (if dt
    [:div
     [:div "time: " (str dt)]
     [into [:div
            {:class "grid gap-1" ;.grid-cols-2.auto-cols-min
             :style {:grid-template-columns "1fr 1fr 1fr 1fr 1fr 1fr"
                     :max-width "1200px"}}
            [:div "calendar"]
            [:div "prior close"]
            [:div "current close"]
            [:div "open"]
            [:div "holiday"]
            [:div "next close"]]
      (map one cals)]]
    [:div "loading.."]))

