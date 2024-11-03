(ns dev.template-calculate
  (:require
   [tick.core :as t]
   [modular.system]
   [quanta.studio.template.calculate :refer [calculate]]))

(def s (modular.system/system :studio))

;; calculate

(calculate s :bollinger {} :no-ui-bars (t/instant))
(calculate s :bollinger {} :chart-no-position (t/instant))

(calculate s :bollinger {} :print (t/instant))
(calculate s :bollinger {} :backtest-raw (t/instant))

; calcualte, and get back a render-spec
(calculate s :bollinger {} :backtest (t/instant))

(calculate s :bollinger {} :chart (t/instant))

(calculate s :bollinger {} :chart-edn (t/instant))
