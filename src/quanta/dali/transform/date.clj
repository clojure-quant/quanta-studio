(ns quanta.dali.transform.date
  (:require
   [tick.core :as t]
   [tablecloth.api :as tc]))

;; DATE FORMAT

(defn create-format-date [zone]
  (let [fmt (t/formatter "YYYY-MM-dd HH:mm")]
    (fn [dt]
      (let [dt (-> dt
                   (t/zoned-date-time)
                   (t/in zone))]
        (t/format fmt dt)))))

(comment
  ((create-format-date "UTC") (t/instant))
  ((create-format-date "America/New_York") (t/instant))
;  
  )

(defn date->string  [timezone ds]
  (let [fmt (create-format-date timezone)]
    (tc/add-column ds :date (map fmt (:date ds)))))

(defn format-date [ds spec]
  (let [tz (or (:timezone spec) "UTC")]
    (date->string tz ds)))

