(ns quanta.viz.plot.transit
  (:require
   [nano-id.core :refer [nano-id]]
   [babashka.fs :as fs]
   [cquant.tmlds :refer [ds->transit-json-file]]))

(def ds-dir ".data/public/ds")

(defn store-dataset [ds]
  (let [id (nano-id 5)
        filename (str ds-dir "/" id ".transit-json")
        url (str "/r/ds/" id ".transit-json")]
    (fs/create-dirs ds-dir)
    (ds->transit-json-file ds filename)
    {:id id
     :url url
     :filename filename}))