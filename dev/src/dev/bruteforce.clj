(ns dev.bruteforce
  (:require 
   [dev.bruteforce-helper :refer [bruteforce]]))
  
(def variations
  [[0 :asset] ["BTCUSDT" "ETHUSDT"]
   [2 :day :atr-n] [20 50]])

(bruteforce :bollinger variations)


