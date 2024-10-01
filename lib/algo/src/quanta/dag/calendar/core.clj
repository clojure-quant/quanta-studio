(ns quanta.dag.calendar.core
  (:require 
     [missionary.core :as m]
     [quanta.dag.calendar.live :as live]))

(defn calculate-calendar [dt]
  (m/seed [dt]))

(defn live-calendar [cal]
  (live/get-calendar-flow cal))
