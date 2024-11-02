(ns quanta.studio.dev.page.render.rtable
  (:require
   [rtable.viewer.rtable :refer [rtable]]
   [ta.viz.lib.format :refer [fmt-nodigits]]))

(def spec
  {:class "table-head-fixed padding-sm table-red table-striped table-hover"
   :style {:width "50vw"
           :height "40vh"
           :border "3px solid green"}
   :columns [{:path :close
              :format ta.viz.lib.format/fmt-nodigits}
             {:path :sma
              :format (fn [v] (str "#" v))}]})

(def data
  [{:close 10.444 :sma 10}
   {:close 14 :sma 12}
   {:close 9 :sma 11}
   {:close 16 :sma 13}
   {:close 11 :sma 12}
   {:close 10 :sma 11}])

(defn page [{:keys [_route-params _query-params _handler] :as _route}]
  [:div.h-screen.w-screen.bg-red-500
   [rtable (assoc spec :rows data)]])