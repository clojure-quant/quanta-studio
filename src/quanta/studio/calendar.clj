(ns quanta.studio.calendar
  (:require
   [tick.core :as t]
   [ta.calendar.core :as cal]
   [missionary.core :as m]))


(defn gather-calendar [calendar-kw interval-kw dt]
  {:calendar [calendar-kw interval-kw]
   :prior (t/instant (cal/prior-close calendar-kw interval-kw dt))
   :current  (t/instant (cal/current-close calendar-kw interval-kw dt))
   :next (t/instant (cal/next-close calendar-kw interval-kw dt))
   })

(defn gather-calendars [dt]
  (let [cals (for [c [:us :crypto :forex 
                      :eu :jp]
                   i [:m :m5 :m15 :h :d]]
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
