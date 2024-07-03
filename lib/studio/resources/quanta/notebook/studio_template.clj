(ns quanta.notebook.studio-template
  (:require
   [clojure.pprint :refer [print-table]]
   [modular.system]
   [quanta.studio.template :refer [load-template get-options]]
   [quanta.studio :refer [backtest subscribe get-subscription-state unsubscribe
                          subscription-summary
                          ]]))

(def s (modular.system/system :studio))

(load-template s :alex/bollinger)
(get-options s :alex/bollinger)

(backtest s :alex/bollinger {} :table)
(backtest s :alex/bollinger {} :chart)



(def id 
   (subscribe s :alex/bollinger {} :table))


id

(get-subscription-state s id)

(unsubscribe s id)

(-> (subscription-summary s)
    (print-table)
 )

(-> (subscription-summary s [:asset])
    (print-table))




