(ns dev.template-calculate
  (:require
   [tick.core :as t]
   [ta.import.provider.bybit.ds :as bybit]
   [quanta.algo.env.bars]
   [quanta.studio.template.db :as tdb]
   [quanta.studio.template.calculate :refer [calculate calculate-init]]))

(def bar-db (bybit/create-import-bybit))
(def env {#'quanta.algo.env.bars/*bar-db* bar-db})

(def s
  (-> {:templates  (atom {})
       :calculate {:log-dir ".data/template-calc/"
                   :env env}}
      (calculate-init)
      (tdb/add-template 'dev.algo-bollinger/bollinger-template)))

;; calculate

(calculate s :bollinger {} :print (t/instant))
(calculate s :bollinger {} :backtest-raw (t/instant))

; calcualte, and get back a render-spec
(calculate s :bollinger {} :backtest (t/instant))