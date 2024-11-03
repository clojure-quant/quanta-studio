(ns dev.template-task
  (:require
   [clojure.pprint :refer [print-table]]
   [modular.system]
   [quanta.template :refer [get-options make-variations apply-options]]
   [quanta.studio.template.db :refer [load-template]]
   [quanta.studio :refer [load-with-options
                          calculate
                          start stop
                          current-task-result task-summary
                          start-variations]]))

;; first lets get the running studio instance

(def s (modular.system/system :studio))

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












