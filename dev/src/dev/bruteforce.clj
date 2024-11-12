(ns dev.bruteforce
  (:require
   [dev.bruteforce-helper :refer [bruteforce]]))

(bruteforce
 {:template-id :bollinger
  :label "bongotrott"
  :options {[:bars-day :trailing-n] 20000}
  :variations {[:* :asset] ["ETHUSDT" "BTCUSDT"]
               [:day  :atr-n] [20 40]}})

;| [:* :asset] | [:day :atr-n] | :trades |           :cum-pl | :max-drawdown-prct |    :id |            :target |
;|-------------+---------------+---------+-------------------+--------------------+--------+--------------------|
;|     BTCUSDT |            20 |      87 | 30883.63105608469 | 22.013191122347607 | Ij2MUP | 1.3116150712912678 |
;|     ETHUSDT |            40 |      58 | 816.9327354497367 | 1.5062909550840848 | tvkYL- | 1.2014743881576975 |
;|     ETHUSDT |            20 |      91 |  905.100689523811 |  4.157657376510926 | o6rbeD | 1.1385615708466634 |
;|     BTCUSDT |            40 |      61 | 6812.732009523814 |  26.54696103944636 | aO_IXv |  1.092399750186653 |
;"Elapsed time: 301.409358 msecs"

;; algo has a exception thrower built in
; for eth atr-n 50.

(bruteforce
 {:template-id :bollinger
  :label "eth-atr-50-exception-test"
  :options {[:bars-day :trailing-n] 20000}
  :variations {[:* :asset] ["ETHUSDT" "BTCUSDT"]
               [:day  :atr-n] [20 50]}})

; SEE: ETH ATR-N 50 is REMOVED.
;| [:* :asset] | [:day :atr-n] | :trades |             :cum-pl | :max-drawdown-prct |    :id |            :target |
;|-------------+---------------+---------+---------------------+--------------------+--------+--------------------|
;|     BTCUSDT |            20 |      87 |   30883.63105608469 | 22.013191122347607 | Oj7KF1 | 1.3116150712912678 |
;|     ETHUSDT |            20 |      91 |    905.100689523811 |  4.157657376510926 | 5bL92Y | 1.1385615708466634 |
;|     BTCUSDT |            50 |      54 | -31455.740743068804 |  71.58271707321626 | pcbbG6 | 0.6005856739468522 |
;"Elapsed time: 216.871283 msecs"