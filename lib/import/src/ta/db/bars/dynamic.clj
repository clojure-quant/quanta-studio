(ns ta.db.bars.dynamic
  (:require
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [ta.db.bars.protocol :refer [bardb barsource] :as b]
   [ta.db.bars.dynamic.overview-db :as overview]
   [ta.db.bars.dynamic.import :refer [import-on-demand]]))


(defrecord bardb-dynamic [bar-db importer overview-db]
  barsource
  (get-bars [this opts window]
    (info "get-bars " (select-keys opts [:task-id :asset :calendar :import]) 
          " window: " (select-keys window [:start :end]))
    (if (:import opts)
      (import-on-demand this opts window)
      (debug "no import defined for: " opts))
    (b/get-bars (:bar-db this) opts window))
  bardb
  (append-bars [this opts ds-bars]
    (info "dynamic append-bars " opts ds-bars)
    ;(info "this: " this)
    (b/append-bars (:bar-db this) opts ds-bars)))

(defn start-bardb-dynamic [bar-db importer overview-path]
  (let [overview-db (overview/start-overview-db overview-path)]
    (bardb-dynamic. bar-db importer overview-db)))

(defn stop-bardb-dynamic [this]
  (overview/stop-overview-db (:overview-db this)))
