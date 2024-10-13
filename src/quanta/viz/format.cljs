(ns quanta.viz.format
  (:require
   [tick.helper :as th]
   [pinkgorilla.goog.string :refer [format]]))

(defn nr-format [f nr]
  ;(println "fmt-nodigits nr: " nr)
  ;;(js/isNaN nr) 
  ;(= nr ##NaN) 
  ;(to-fixed nr 1)
  (if (nil? nr)
    "-"
    (if (string? nr)
      nr
      (format f nr))))

(defn nr-format-0-digits [nr]
  (nr-format "%.0f" nr))

;; date

(defn dt-format [f dt]
  (if (nil? dt)
    ""
    (if (string? dt)
      dt
      (th/dt-format f dt))))

(defn dt-yyyymmdd [dt]
  (dt-format "YYYY-MM-dd" dt))

(defn dt-yyyymmdd-hhmm [dt]
  (dt-format "YYYY-MM-dd HH:mm" dt))


;; bool

(defn format-bool [b]
  (if b "t" "f"))