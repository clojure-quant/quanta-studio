(ns quanta.studio.dev.page.render.roundtripmetrics
  (:require
   [reagent.core :as r]
   [promesa.core :as p]
   [rtable.rtable]
   [cquant.tmlds :refer [GET]]
   [quanta.viz.render.trade.core :refer [roundtrip-stats-ui]]))

(defn load-backtest [url backtest-a]
  (println "loading backtest from url: " url)
  (let [load-promise (GET url)]
    (-> load-promise
        (p/then (fn [ds]
                  (println "ds from url " url " loaded successfully.")
                  (println ds)
                  (reset! backtest-a ds)
                  ds))
        (p/catch (fn [err]
                   (println "could not load ds from url " url " err: " err)
                   (reset! backtest-a nil))))
    nil))

(def opts
  {:intraday? false
   ;:style {:height "600px"
   ;        :width "800px"}
   ;:class "bg-red-500"
   })

(defn backtest-ui [url]
  (let [data-a (r/atom nil)]
    (load-backtest url data-a)
    (fn []
      (if @data-a
        [roundtrip-stats-ui opts @data-a]
        [:div "loading data.."]))))

(defn page [{:keys [_route-params _query-params _handler] :as _route}]
  [:div.h-screen.w-screen.bg-blue-100
   [backtest-ui "/r/data/HyIPuo-backtest.transit-json"]])

(defn page-stock [{:keys [_route-params _query-params _handler] :as _route}]
  [:div.h-screen.w-screen.bg-blue-100
   [backtest-ui "/r/data/report-stock-future.transit-json"]])