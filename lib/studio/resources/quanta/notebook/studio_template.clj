(ns quanta.notebook.studio-template
  (:require
   [clojure.pprint :refer [print-table]]
   [modular.system]
   [quanta.studio.template :refer [load-template get-options]]
   [quanta.studio :refer [backtest 
                          start stop 
                          current-task-result task-summary]]
   [quanta.template :refer [permutate]]
   ))

(def s (modular.system/system :studio))

(load-template s :alex/bollinger)
(get-options s :alex/bollinger)

(backtest s :alex/bollinger {} :table)
(backtest s :alex/bollinger {} :chart)



(def id 
   (start s :alex/bollinger {} :table))


id

(current-task-result s id)

(stop s id)

(-> (task-summary s)
    (print-table)
 )

(-> (task-summary s [:asset])
    (print-table))

(-> (load-template s :alex/bollinger)
    (permutate :asset ["BTCUSDT" "ETHUSDT"])
 )



