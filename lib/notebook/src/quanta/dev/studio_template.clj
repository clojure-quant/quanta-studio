(ns quanta.dev.studio-template
  (:require
   [clojure.pprint :refer [print-table]]
   [modular.system]
   [quanta.template :refer [get-options make-variations apply-options]]
   [quanta.template.db :refer [load-template]]
   [quanta.studio :refer [load-with-options
                          calculate
                          start stop
                          current-task-result task-summary
                          start-variations]]))

;; first lets get the running studio instance

(def s (modular.system/system :studio))

;; template - load and get options.

(load-template s :juan-fx)

(-> (load-template s :juan-fx)
    (get-options))


(load-with-options s :juan-fx nil)
;; => Execution error (AssertionError) at quanta.studio/load-with-options (REPL:76).
;;    Assert failed: options may not be nil
;;    options

(load-with-options s :juan-fx [])
;; => Execution error (AssertionError) at quanta.studio/load-with-options (REPL:77).
;;    Assert failed: options needs to be a map
;;    (map? options)

(load-with-options s :juan-fx {}) 


(load-with-options s :juan-fx {[2 :asset] "GBP/JPY"})


(load-with-options s :juan-fx {[0 :dummy] "99"})




;; calculate

(calculate s :juan-fx {} :agtable)

(calculate s :juan-fx {[0 :dummy] "9999"} :agtable)



(calculate s :alex/bollinger {} :chart)
(calculate s :alex/bollinger {} :backtest)
(calculate s :alex/bollinger {} :backtest-raw)


(calculate s :alex/bollinger {:asset "LTCUSDT"
                             :entry [:fixed-amount 1000] } :backtest-raw)


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

; test code to get a list of templates
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

; get result specific task

(current-task-result s "2CYCPN")

(current-task-result s "dsVoP1")

(current-task-result s "nlavCI")






  





