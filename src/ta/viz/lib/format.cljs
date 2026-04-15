(ns ta.viz.lib.format
  (:require
   [tick.helper :refer [dt-format]]
   [pinkgorilla.goog.string :refer [format]]))

(defn align-right [_c]
  {:style {:float "right"}})