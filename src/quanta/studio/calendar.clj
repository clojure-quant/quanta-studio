(ns quanta.studio.calendar
  (:require
   [tick.core :as t]
   [quanta.calendar.calendar-info :refer [market-info gather-calendar]]
   [missionary.core :as m]))

(defn gather-calendars [dt]
  (let [cals (for [c [:us :crypto :forex
                      :eu :jp]
                   i [:m :m5 :m15 :h :d]]
               (gather-calendar [c i] dt))]
    {:dt dt
     :cals cals}))

(defn calendar-time []
  ;(m/stream
  (m/ap
   (loop [dt (t/instant)]
     (let [cal (gather-calendars dt)]
       (m/amb
        (m/? (m/sleep 500 cal))
        (recur (t/instant)))))))

(comment
  (market-info :crypto)
  (market-info :eu)

  (gather-calendar [:crypto :m] (t/instant))

  (gather-calendars (t/instant))

  (filter #(= true (:business %)) (:cals (gather-calendars (t/instant))))

  (filter #(= (last (:calendar %)) :d) (:cals (gather-calendars (t/instant))))

; 
  )
