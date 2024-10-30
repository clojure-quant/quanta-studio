(ns quanta.snippet.plot.highstock
  (:require
   [quanta.snippet.data.random-bars :refer [random-bar-ds]]
   [quanta.dali.plot :as plot]))

(def ds (random-bar-ds 200))

ds

(plot/highstock-ds {:charts [{:bar {:type :ohlc
                                    :mode :candle}
                              :close :line}
                             {:volume :column}
                             {:close :line}]}
                   ds)