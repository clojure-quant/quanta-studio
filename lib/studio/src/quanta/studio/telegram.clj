(ns quanta.studio.telegram
  (:require
   [telegram.pubsub :as tpubsub]))

(defn text [s]
  {:html [:p {pr-str s}]})

(defn ping [_]
  {:text "pong"})

; (tpubsub/publish this topic msg)