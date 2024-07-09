(ns ta.db.bars.source
  (:require
   [tech.v3.dataset :as tds]
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [ta.db.bars.protocol :as b :refer [barsource bardb]]))

(defn get-source [{:keys [sources default] :as this} opts]
  (let [source (or (:source opts)
                   default)]
    (get sources source)))

(defrecord bardb-source [sources default]
  barsource
  (get-bars [this opts window]
    (b/get-bars (get-source this opts) opts window))
  bardb
  (append-bars [this opts bar-ds]
    (b/append-bars (get-source this opts) opts bar-ds)))

(defn start-bardb-source
  "Returns bars from a selectable bardb-source.
   The selection of the source depends on
   1. It tries to use the :source key the get-bars request,
   2. otherwise the default source is used."
  [{:keys [sources default]}]
  (info "starting bardb-source with " (count sources) "sources ..")
  (bardb-source. sources default))

(defn ds->map [ds]
  ;(tc/rows :as-maps) ; this does not work, type of it is a reified dataset. 
  ; this works in repl, but when sending data to the browser it fails.
  (into []
        (tds/mapseq-reader ds)))

(defn get-bars-source
  "used from ui, exposed via clj-service"
  [this opts window]
  (-> (b/get-bars this opts window)
      ds->map))