(ns quanta.algo.dag.util
  (:require
    [taoensso.timbre :as timbre :refer [debug info warn error]]
    [missionary.core :as m])
   (:import
    [missionary Cancelled]
    [java.util.concurrent.locks ReentrantLock]))

(defn take-first-non-nil [f]
  ; flows dont implement deref
  (m/eduction
   (remove nil?)
   (take 1)
   f))

(defn current-v
  "gets the first non-nil value from the flow"
  [f]
  (m/reduce (fn [r v]
              (println "current v: " v " r: " r)
              v) nil
            (take-first-non-nil f)))