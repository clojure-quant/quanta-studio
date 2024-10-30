(ns quanta.snippet.plot.agtable
  (:require
   [quanta.snippet.data.random-bars :refer [random-bar-ds]]
   [quanta.dali.plot :as plot]))

(def ds (random-bar-ds 200))

(def opts
  {:style {;:width "800px" :height "600px"
           :width "800px" :height "600px"
           ;:width "100%" :height "100%"
           }
   :timezone "America/Panama"
   :columns [{:field :date}
             {:field :open
              :cellStyle {:color "red" :background-color "green"}
              :resizable true}
             {:field :high
              :type "rightAligned"
              :resizable true
              ;:valueGetter: p => p.data.athlete
              ; A Value Getter is a function that gets called for each row to return the Cell Value for a Column. 
              }
             {:field :low :width 70
              :cellClass "shaded-class"
              :resizable true}
             {:field :close :header "C" :resizable true
              ;:cellClassRules {"bg-blue-500" (fn [p]
              ;                                 (println "ccr: " p)
              ;                                 (nil? (.-value p)))}
              }
             {:field :volume :width 70
              :resizable true
              :cellStyle {:fontWeight "bold"}}]})

(plot/aggrid-ds opts ds)


