(ns quanta.dali.viewer.backtest
  (:require
   [dali.viewer :refer [viewer]]))

(defn backtest-ui-ds [data]
  [viewer {:viewer-fn 'quanta.dali.viewer.trade.backtest/backtest-ui
           :transform-fn 'dali.transform.transit/load-transit
           :data data}])