(ns quanta.studio.calendar
  (:require
   [tick.core :as t]
   [ta.calendar.core :as cal]
   [missionary.core :as m]))

(defn gather-calendar [calendar-kw interval-kw dt]
  {:calendar [calendar-kw interval-kw]
   :prior (t/instant (cal/prior-close calendar-kw :m dt))
   :current (t/instant (cal/next-close calendar-kw :m dt))
   :next (t/instant (cal/next-close calendar-kw :m dt))})

(defn gather-calendars [dt]
  (let [cals (for [c [:us :crypto :forex :eu :jp]
                   i [:D :m :m15 :h]]
               (gather-calendar c i dt))]
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
  (gather-calendar :crypto :m (t/instant))

  (gather-calendars (t/instant))

; 
  )
