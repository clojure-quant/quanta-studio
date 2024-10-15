(ns dev.bruteforce
  (:require
   [dev.bruteforce-helper :refer [bruteforce]]))

(def variations
  [[0 :asset] ["BTCUSDT" "ETHUSDT"]
   [2 :day :atr-n] [20 50]])

(bruteforce :bollinger variations)
;; => nil

; | [0 :asset] | [2 :day :atr-n] |             :target | :trades |             :cum-pl | :max-drawdown-prct |    :id |
; |------------+-----------------+---------------------+---------+---------------------+--------------------+--------|
; |    ETHUSDT |              50 |  -0.332916646516985 |     189 |  -3391.970259526272 |  7.064964609436861 | 2bNMLk |
; |    ETHUSDT |              20 |  -0.332916646516985 |     189 |  -3391.970259526272 |  7.064964609436861 | dxxKXR |
; |    BTCUSDT |              50 | -0.3986514403003539 |     178 | -43415.344551893984 |  88.05984120213229 | u5EKqt |
; |    BTCUSDT |              20 | -0.3986514403003539 |     178 | -43415.344551893984 |  88.05984120213229 | EP4zLZ |
; "Elapsed time: 12701.029513 msecs"
