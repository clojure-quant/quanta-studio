(ns quanta.studio.view.calendar)

(defn one [{:keys [calendar calendar-time current next open business]}]
  [:<>
   [:div (str calendar)]
   [:div (str calendar-time)]
   [:div (str current)]
   [:div (if open "OPEN" "CLOSED")]
   [:div (if business "WORK" "HOLIDAY")]
   [:div (str next)]])

(defn calendar-ui [{:keys [dt cals]}]
  (if dt
    [:div
     [:div "time: " (str dt)]
     [into [:div
            {:class "grid gap-1" ;.grid-cols-2.auto-cols-min
             :style {:grid-template-columns "1fr 2fr 1fr 1fr 1fr 1fr"
                     :max-width "1200px"}}
            [:div "calendar"]
            [:div "local time"]
            [:div "current close"]
            [:div "open"]
            [:div "holiday"]
            [:div "next close"]]
      (map one cals)]]
    [:div "loading.."]))

