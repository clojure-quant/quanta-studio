(ns quanta.studio.calendar
  (:require
   [tick.core :as t]
   [ta.calendar.core :as cal]
   [ta.calendar.calendars :as caldb]
   [ta.calendar.helper :as calhelp]
   [missionary.core :as m]))

(defn market-info [market-kw]
  (let [cal (caldb/get-calendar market-kw)
        dt (t/instant)
        dt-cal (-> dt
                   (t/zoned-date-time)
                   (t/in (:timezone cal)))
        open? (calhelp/time-open? cal dt-cal)
        business? (calhelp/day-open? cal dt-cal)]
    {:calendar market-kw
     :open open?
     :business business?
     :calendar-time (t/date-time dt-cal)}))

(defn gather-calendar [calendar-kw interval-kw dt]
  (let [current-close (t/instant (cal/current-close calendar-kw interval-kw dt))]
    (assoc (market-info calendar-kw)
           :calendar [calendar-kw interval-kw]
       ;:prior (t/instant (cal/prior-close calendar-kw interval-kw dt))
           :current current-close
           :next (t/instant (cal/next-close calendar-kw interval-kw current-close)))))

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
  (market-info :crypto)
  (market-info :eu)

  (gather-calendar :crypto :m (t/instant))

  (gather-calendars (t/instant))

; 
  )
