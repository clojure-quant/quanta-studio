(ns quanta.dev.agtable
  (:require
   [nano-id.core :refer [nano-id]]
   [tech.v3.io :as io]
   [tech.v3.libs.clj-transit :as tech-transit]
   [tablecloth.api :as tc]
   [quanta.snippet.data.random-bars :refer [random-bar-ds]]
   [quanta.viz.plot.agtable :as ag]))

(def ds (random-bar-ds 200))

ds

(ag/format-date {} ds)
(ag/format-date {:timezone "America/Panama"} ds)

(defn transit-json-file->ds
  [fname]
  (with-open [ins (io/input-stream fname)]
    (tech-transit/transit->dataset ins :json tech-transit/java-time-write-handlers)))

(transit-json-file->ds "./target/webly/public/ds/xXBt2.transit-json")

(transit-json-file->ds "./target/webly/public/ds/_0fLM.transit-json")


