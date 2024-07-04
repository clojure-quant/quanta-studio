(ns quanta.notebook.studio-template
  (:require
   [clojure.pprint :refer [print-table]]
   [modular.system]
   [quanta.template :refer [get-options make-variations apply-options]]
   [quanta.template.db :refer [load-template ]]
   [quanta.studio :refer [backtest
                          start stop
                          current-task-result task-summary
                          start-variations]]))

;; first lets get the runnign studio instance

(def s (modular.system/system :studio))

;; template - load and get options.

(load-template s :alex/bollinger)

(-> (load-template s :alex/bollinger)
    (get-options))

;; backtest

(backtest s :alex/bollinger {} :table)
(backtest s :alex/bollinger {} :chart)

;; start/stop task (a realtime calculation)

(def id
  (start s :alex/bollinger {} :table))

id

(current-task-result s id)

(stop s id)

;; task admin

(-> (task-summary s)
    (print-table))

(-> (task-summary s [:asset])
    (print-table))
 
;; start variations

(let [option-seq (make-variations [:asset [:a :b :c :d]])
      template (load-template s :alex/bollinger)]
   (map #(apply-options template %) option-seq))

   (start-variations
   s
   :alex/bollinger
   :chart
   [:asset ["BTCUSDT" "ETHUSDT"]
    :calendar [[:crypto :m]
               [:crypto :m15]
               [:crypto :m30]
               [:crypto :h]]])




  





