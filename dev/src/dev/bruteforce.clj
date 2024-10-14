(ns dev.bruteforce
  (:require
   [dev.bruteforce-helper :refer [bruteforce]]))

(def variations
  [[0 :asset] ["BTCUSDT" "ETHUSDT"]
   [2 :day :atr-n] [20 50]])

(bruteforce :bollinger variations)

; | [0 :asset] | [2 :day :atr-n] |              :target | :trades |            :cum-pl | :max-drawdown-prct |    :id |
; |------------+-----------------+----------------------+---------+--------------------+--------------------+--------|
; |    ETHUSDT |              50 | -0.22859218546532867 |     189 | -4275.807979526272 |   43.4565741896745 | VrWBOS |
; |    ETHUSDT |              20 | -0.22859218546532867 |     189 | -4275.807979526272 |   43.4565741896745 | RJ9IeW |
; |    BTCUSDT |              50 | -0.27461784008370577 |     178 | -56906.15905189399 |  541.2051150254176 | 6mkG5G |
; |    BTCUSDT |              20 | -0.27461784008370577 |     178 | -56906.15905189399 |  541.2051150254176 | m0ZGyH |
; "Elapsed time: 12701.029513 msecs"
