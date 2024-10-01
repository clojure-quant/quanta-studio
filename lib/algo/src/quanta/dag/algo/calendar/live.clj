(ns quanta.dag.algo.calendar.live
  (:require
   [tick.core :as t]
   [missionary.core :as m]
   [ta.calendar.core :refer [current-close next-close]]
   [ta.calendar.calendars :refer [get-calendar-list]]
   [ta.calendar.interval :refer [get-interval-list]]))

(defn scheduler
  "returns a missionary flow
   fires current and all upcoming timestamps for a calendar"
  [calendar]
  (m/stream
   (m/ap
    (println "starting scheduler for calendar: " calendar)
    (let [[market-kw interval-kw] calendar
          dt (t/now)
          current-close (current-close market-kw interval-kw dt)] 
      (m/amb current-close)
      (loop [dt dt
             current-dt current-close]
        (let [current-dt-inst (t/instant current-dt)
              diff-ms (* 1000 (- (t/long current-dt-inst) (t/long dt)))]
          (when (> diff-ms 0)
           (println "sleeping for ms: " diff-ms " until: " current-dt)
            (m/? (m/sleep diff-ms current-dt))
           (println "finished sleeping")
            :bongo)
          (m/amb
           current-dt
           (recur (t/now)
                  (next-close market-kw interval-kw current-dt)))))))))

(defn all-calendars []
  (->> (for [c (get-calendar-list)
             i (get-interval-list)]
         (let [cal [c i]]
           [cal (scheduler cal)]))
       (into {})))

(def calendar-dict (all-calendars))

(defn get-calendar-flow [calendar]
  (get calendar-dict calendar))


(comment 

  (m/? (->> (scheduler [:us :d])
            (m/eduction (take 1))
            (m/reduce conj)))
  
  (m/? (->> (scheduler [:forex :m])
            (m/eduction (take 2))
            (m/reduce conj)))
  
  (get-calendar-flow [:forex :m])
  (get-calendar-flow [:forex :m333])
    
 ;
  )
