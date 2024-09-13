(ns quanta.notebook.viz.render
  (:require
   [quanta.viz.render.core :refer [render]]))


(render
 {:render-fn 'quanta.viz.render.rtable/rtable
  :spec {:class "table-head-fixed padding-sm table-red table-striped table-hover"
         :style {:width "50vw"
                 :height "40vh"
                 :border "3px solid green"}
         :columns [{:path :close}
                   {:path :sma}]}
  :data [{:close 10 :sma 10}
         {:close 14 :sma 12}
         {:close 9 :sma 11}
         {:close 16 :sma 13}
         {:close 11 :sma 12}
         {:close 10 :sma 11}]})
