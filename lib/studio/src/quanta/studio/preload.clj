(ns quanta.studio.preload
  (:require
   [clojure.pprint :refer [print-table]]
   [taoensso.timbre :refer [info warn error]]
   [de.otto.nom.core :as nom]
   [tablecloth.api :as tc]
   [modular.system]
   [ta.db.bars.protocol :as b]
   [ta.db.bars.duckdb.delete :refer [delete-bars]]
   [ta.db.bars.source :refer [get-source]]
   [ta.import.helper.retries :refer [with-retries]]))

;; TODO: make delete a interface, so it works for nippy and duckdb.

(defn bar-env [bardb from to]
  (let [b  (modular.system/system bardb)]
    {:import (get-source b {:source from})
     :to (get-source b  {:source to})}))

(defn- import-bars-one
  "imports bars from a bar-source to a bar-db.
    asset: EUR/USD 
    calendar: [:us :d]
    from one of :kibot :bybit :bybit-parallel
    to: a bar-db like :nippy :duckdb
    window {:from :to} both instant or zoneddatetime
    label: optional label for logging
    retries: optional, number of retries to import bars."
  [{:keys [bar-db import-manager]
    :or {bar-db :bardb
         import-manager :import}}
   {:keys [asset calendar
           from to window
           retries
           label]
    :or {label ""
         retries 5}}]
  (info "getting " label " for: " asset)
  (let [{:keys [import to] :as env} (bar-env bar-db import-manager to)
        _ (info "env: " env)
        opts {:asset asset
              :calendar calendar
              :import from}
        opts-save (dissoc opts :import)
             ; ds (b/get-bars import opts window)
        ds (with-retries retries b/get-bars import opts window)]
    (if (nom/anomaly? ds)
      (do
        (error "could not get asset: " asset)
        {:asset asset :count 0})
      (let [c (tc/row-count ds)]
        (info "received asset:" asset " count: " c)
             ;(delete-bars to [:forex :d] asset)
        (b/append-bars to opts-save ds)
        {:asset asset
         :count c
         :start (-> ds tc/first :date first)}))))

(defn import-bars
  "imports bars from a bar-source to a bar-db.
   asset: either a single asset EUR/USD or multiple assets [EUR/USD USD/JPY]
   calendar: [:us :d]
   from one of :kibot :bybit :bybit-parallel
   to: a bar-db like :nippy :duckdb
   window {:from :to} both instant or zoneddatetime
   label: optional label for logging
   retries: optional, number of retries to import bars.
   this function is intended to be used to import data prior to doing backtests.
   the optional environment key settings are useful if you use a non-standard
   modular config"
  ([{:keys [asset calendar
            from to window
            label]
     :or {label ""}
     :as opts}]
   (import-bars {} opts))
  ([{:keys [bar-db import-manager]
     :or {bar-db :bardb
          import-manager :import}
     :as env}
    {:keys [asset calendar
            from to window
            label
            retries]
     :or {label ""
          retries 5}
     :as opts}]
   (if (or (seq? asset) (vector? asset))
     (let [result (map #(import-bars-one env (assoc opts :asset %)) asset)]
       (println "result for: " label)
       (print-table result)
       result)
     (import-bars-one env opts))))

(comment
  (bar-env :bardb :import :nippy)

  (require '[tick.core :as t])
  (import-bars-one {} {:asset "EUR/USD" :calendar [:us :d]
                       :from :kibot :to :nippy
                       :window {:start (t/instant "2000-01-01T00:00:00Z")
                                :end (t/instant "2024-09-10T00:00:00Z")}
                       :label "test"})

  (import-bars {:asset "EUR/USD" :calendar [:us :d]
                :from :kibot :to :nippy
                :window {:start (t/instant "2000-01-01T00:00:00Z")
                         :end (t/instant "2024-09-10T00:00:00Z")}
                :label "test"})

  (import-bars {:asset ["EUR/USD" "USD/JPY"] :calendar [:us :d]
                :from :kibot :to :nippy
                :window {:start (t/instant "2000-01-01T00:00:00Z")
                         :end (t/instant "2024-09-10T00:00:00Z")}
                :label "multiple forex quotes test"})

; 
  )