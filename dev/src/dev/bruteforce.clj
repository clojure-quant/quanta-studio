(ns dev.bruteforce
  (:require
   [dev.bruteforce-helper :refer [bruteforce-old]]))

(bruteforce-old
 {:template-id :bollinger
  :options {[2 :trailing-n] 20000}
  :variations {[0 :asset] ["ETHUSDT" "BTCUSDT"]
               ;[2 :atr-n] [20 50]
               }})

; | [0 :asset] | :trades |             :cum-pl | :max-drawdown-prct |    :id |                :target |
; |------------+---------+---------------------+--------------------+--------+------------------------|
; |    BTCUSDT |     191 |           -270000.0 | 270.00000000000006 | GE0_fj | 1.1351841277684315E-15 |
; |    ETHUSDT |     199 | -316567.26050347753 | 316.56726050347737 | oFSaUu |   7.36203791126886E-16 |

;; algo has a exception thrower buit in
; for eth atr-n 50.

(bruteforce-old
 {:template-id :bollinger
  :label "eth-atr-50-exception-test"
  :options {[2 :trailing-n] 20000}
  :variations {[0 :asset] ["ETHUSDT" "BTCUSDT"]
               [2 :atr-n] [20 50]}})
