(ns quanta.snippet.plot.rtable
  (:require 
   [quanta.snippet.data.random-bars :refer [random-bar-ds]]
   [quanta.viz.plot :as plot]))

(def ds (random-bar-ds 200))

(plot/rtable {:class "table-head-fixed padding-sm table-red table-striped table-hover"
              :style {:width "50vw"
                      :height "40vh"
                      :border "3px solid green"}
              :columns [{:path :date :format 'ta.viz.lib.format-date/dt-yyyymmdd-hhmm :max-width "80px"}
                        {:path :close :format 'ta.viz.lib.format-number/fmt-nodigits}
                        {:path :volume}]}
             ds)