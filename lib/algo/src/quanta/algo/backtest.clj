(ns quanta.algo.backtest
  (:require
   [tablecloth.api :as tc]
   [ta.trade.backtest.from-entry :refer [entry-signal->roundtrips]]
   [ta.trade.roundtrip.core :refer [roundtrip-stats]]
   ;[quanta.viz.plot.trade.core :refer [roundtrip-stats-ui]]
   ))

(defn backtest [{:keys [asset entry exit] :as opts} bar-ds]
  ; we need to get the asset from the bar-ds, because
  ; here we only see the viz-opts. this needs to be improved.
  (assert asset "backtest needs :asset option")
  (assert entry "backtest needs :entry option")
  (assert exit "backtest needs :exit option")
  (let [last-row (-> (tc/last bar-ds)
                     (tc/rows :as-maps)
                     last)
        {:keys [asset]} last-row
        backtest-opts (select-keys opts [:asset :entry :exit])
        {:keys [roundtrips exit-signal bar-entry-exit-ds] :as full}
        (entry-signal->roundtrips backtest-opts bar-ds)
        rt-stats (roundtrip-stats roundtrips)]
    rt-stats
    ;full
    ))

#_(defn backtest-ui [backtest-opts bar-ds]
    (->> (backtest backtest-opts bar-ds)
         (roundtrip-stats-ui backtest-opts)))