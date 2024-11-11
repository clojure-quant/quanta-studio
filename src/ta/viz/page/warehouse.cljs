(ns ta.viz.page.warehouse
  (:require
   [quanta.studio.lib.loader :refer [clj->p]]
   [quanta.studio.lib.link :refer [link-href]]
   [ta.tradingview.goldly.view.aggrid :refer [table]]))

; todo: fix it . ta.db.bars.overview/overview-map is removed.

(defn warehouse-overview-view [wh f]
  (let [wh-overview (clj->p 'ta.db.bars.overview/overview-map wh f)]
    (fn [wh f]
      [:div
       [:h1.text-bold.bg-green-500 "Warehouse " (str wh) " " (str f)]
       (case (:status @wh-overview)
         :loading [:p "loading"]
         :error [:p "error!"]
         :data [table (:data @wh-overview)]
         [:p "unknown: status:" (pr-str @wh-overview #_(:status @wh-overview))])])))

(defn warehouse-page [_route]
  [:div.h-screen.w-screen.bg-red-500
   [:div.flex.flex-col.h-full.w-full
      ;[:div.flex.flex-row.bg-blue-500
    [link-href "/" "main"]
    [warehouse-overview-view :stocks "D"]
    [warehouse-overview-view :crypto "D"]
   ;    ]
    ]])