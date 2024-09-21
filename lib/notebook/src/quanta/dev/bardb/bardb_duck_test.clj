(ns quanta.dev.bardb.bardb-duck-test
  (:require
   [tick.core :as t]
   [tablecloth.api :as tc]
   [modular.system]
   [ta.calendar.core :as cal]
   [ta.db.bars.protocol :as b]
   [ta.db.bars.duckdb :as duck]
   [ta.db.bars.dynamic :as dynamic]))

;; Test if duckdb get/append works

(def db-duck (duck/start-bardb-duck "/tmp/demo12.ddb"))
db-duck
(duck/init-tables db-duck)

(def ds
  (tc/dataset [{:date (-> "1999-12-31T00:00:00Z" t/instant)
                :open 1.0 :high 1.0 :low 1.0 :close 1.0 :volume 1.0}
               {:date (-> "2000-12-31T00:00:00Z" t/instant)
                :open 1.0 :high 1.0 :low 1.0 :close 1.0 :volume 1.0}]))
ds

(duck/order-columns (duck/empty-ds [:us :d]))

(b/append-bars db-duck {:asset "QQQ"
                        :calendar [:us :d]
                        :import :kibot}
               (duck/empty-ds [:us :d]))

(b/append-bars db-duck {:asset "QQQ"
                        :calendar [:us :d]
                        :import :kibot}
               (duck/order-columns-strange (duck/empty-ds [:us :d])))

(b/append-bars db-duck {:asset "MSFT"
                        :calendar [:us :d]
                        :import :kibot}
               ds)

(def window {:start (-> "1999-02-01T20:00:00Z" t/instant)
             :end (-> "2001-03-01T20:00:00Z" t/instant)})
window

; get all data available
(b/get-bars db-duck
            {:asset "MSFT"
             :calendar [:us :d]
             :import :kibot}
            {})

; just get the window
(b/get-bars db-duck
            {:asset "MSFT"
             :calendar [:us :d]
             :import :kibot}
            window)

;; Test if DYAMIC get/append works

(def db-dynamic (dynamic/start-bardb-dynamic db-duck "/tmp/overview"))
db-dynamic

(defn window-as-date-time [window]
  {:start (t/date-time (:start window))
   :end (t/date-time (:end window))})

(def window (-> (cal/trailing-range [:us :d] 10)
                (window-as-date-time)))

window

(b/append-bars db-dynamic {:asset "MSFT"
                           :calendar [:us :d]
                           :import :kibot} ds)

(-> (duck/empty-ds [:us :d]) (tc/info))

; since we dont have this asset in our db, it will fetch via kibot
; and save to duckdb.
(b/get-bars db-dynamic
            {:asset "MSFT"
             :calendar [:us :d]
             :import :kibot}
            window)

;; check if we get the same number of bars back:
(b/get-bars db-duck
            {:asset "QQQ"
             :calendar [:us :d]
             :import :kibot}
            window)