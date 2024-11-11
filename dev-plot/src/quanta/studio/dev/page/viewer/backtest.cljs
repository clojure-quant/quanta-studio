(ns quanta.studio.dev.page.viewer.backtest
  (:require
   [dali.viewer :refer [viewer]]))

(defn page [{:keys [_route-params _query-params _handler] :as _route}]
  [:div.h-screen.w-screen.bg-blue-100
   [viewer {:viewer-fn 'quanta.dali.viewer.trade.backtest/backtest-ui
            :transform-fn 'dali.transform.transit/load-transit
            ; note: no :data :load :url because we use only the loader
            :data {:url "/r/data/report-stock-future.transit-json"}}]])