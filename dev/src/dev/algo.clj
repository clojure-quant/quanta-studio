(ns dev.algo
  (:require
   [tick.core :as t]
   [quanta.dag.core :as dag]
   [quanta.algo.core :as algo]
   [quanta.bar.env]
   [modular.system]
   [tablecloth.api :as tc]
   [cquant.tmlds :refer [ds->transit-json-file transit-json-file->ds]]
   [tech.v3.libs.clj-transit :refer [java-time-write-handlers  java-time-read-handlers]]
   [dev.algo-bollinger :refer [bollinger-algo]]))

;; ENV

(def s (modular.system/system :studio))
(def be (modular.system/system :bar-engine))
(def env {:bar-db be})

;; SNAPSHOT

(def bollinger
  (-> (dag/create-dag {:log-dir ".data/"
                       :env env})
      (algo/add-env-time-snapshot (t/instant))
      (algo/add-algo bollinger-algo)))

(dag/cell-ids bollinger)

(dag/start-log-cell bollinger [:crypto :d])
(dag/start-log-cell bollinger [:crypto :m])
(dag/start-log-cell bollinger :bars-day)
(dag/start-log-cell bollinger :day)

(def ds
  (dag/get-current-value bollinger :day))

ds

(tc/info ds)

(ds->transit-json-file ds ".data/demo.transit-json")

(transit-json-file->ds ".data/demo.transit-json")

;2021-11-10T00:00:00Z
;2024-11-12T00:00:00Z 
;1970-01-19T22:35:02.400Z
;1970-01-21T00:56:09.600Z 

(def dt (t/instant))
dt
;; => #inst "2024-11-13T02:22:54.944732493-00:00"

(ds->transit-json-file dt ".data/dt.transit-json")
(transit-json-file->ds ".data/dt.transit-json")
