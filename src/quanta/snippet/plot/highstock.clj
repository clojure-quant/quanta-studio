(ns quanta.snippet.plot.highstock
  (:require
   [quanta.snippet.data.random-bars :refer [random-bar-ds]]
   [quanta.viz.plot :as plot]))

(def ds (random-bar-ds 200))

ds

(plot/highstock {:chart {:box :fl}
                 :charts  [{:bars :candlestick 
                            :close :line}
                           {:volume :column}
                           {:close :line}
                           ]}
                ds)