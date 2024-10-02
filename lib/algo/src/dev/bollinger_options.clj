(ns dev.bollinger-options
  (:require
   [quanta.algo.options :refer [apply-options]]
   [dev.bollinger-algo :refer [bollinger-algo]]))


(-> bollinger-algo
    (apply-options {[0 :asset] "ETHUSDT"
                    [4 :calendar] [:forex :h]}))
