(ns dev.tick
  (:require [tick.core :as t]))

(-> (t/instant "2018-01-01T00:00:00Z")
    ;(t/zoned-date-time)
    (t/in "UTC"))

(-> (keys (ns-publics 'tick.core))
    sort
    pr-str)