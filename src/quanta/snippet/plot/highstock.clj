(ns quanta.snippet.plot.highstock
  (:require
   [reval.core :refer [*env*]]
   [quanta.snippet.data.random-bars :refer [random-bar-ds]]
   [quanta.dali.plot :as plot]))

(def ds (random-bar-ds 200))

ds

(plot/highstock-ds
 *env*
 {:charts [{:bar {:type :ohlc
                  :mode :candle}
            :close :line}
           {:volume :column}
           {:close :line}]}
 ds)