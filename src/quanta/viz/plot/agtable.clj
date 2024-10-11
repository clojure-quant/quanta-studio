(ns quanta.viz.plot.agtable
  (:require
   [tick.core :as t]
   [tech.v3.dataset :as tds]
   [nano-id.core :refer [nano-id]]
   [clojure.java.io :as io]
   [tablecloth.api :as tc]
   [de.otto.nom.core :as nom]
   [quanta.viz.plot.anomaly :as plot]
   [cquant.tmlds :refer [ds->transit-json-file]]))

(defn agtable-spec? [spec]
  (and (map? spec)
       (:columns spec)))

;; DATE FORMAT

(defn create-format-date [zone]
  (let [fmt (t/formatter "YYYY-MM-dd HH:mm")]
    (fn [dt]
      (let [dt (-> dt
                   (t/zoned-date-time)
                   (t/in zone))]
        (t/format fmt dt)))))

(comment
  ((create-format-date "UTC") (t/instant))
  ((create-format-date "America/New_York") (t/instant))
;  
  )

(defn date->string  [timezone ds]
  (let [fmt (create-format-date timezone)]
    (tc/add-column ds :date (map fmt (:date ds)))))

(defn format-date [ds spec]
  (let [tz (or (:timezone spec) "UTC")]
    (date->string tz ds)))

;; SELECT COLUMNS

(defn ensure-keyword [v]
  (if (string? v)
    (keyword v)
    v))

(defn agtable-cols [spec]
  (->> (:columns spec)
       (map :field)
       (map ensure-keyword)))

(defn select-columns [ds spec]
  (let [columns (agtable-cols spec)]
    (tc/select-columns ds columns)))

;; STORE DS

(def ds-dir "./target/webly/public/ds")

(defn ensure-directory [dir-path]
  (let [dir (io/file dir-path)]
    (when-not (.exists dir)
      (.mkdirs dir))))

(defn store-dataset [ds]
  (let [id (nano-id 5)
        filename (str ds-dir "/" id ".transit-json")
        url (str "/r/ds/" id ".transit-json")]
    (ensure-directory ds-dir)
    (ds->transit-json-file ds filename)
    {:id id
     :url url
     :filename filename}))

(defn agtable-render-spec-impl
  "returns a plot specification {:render-fn :spec :data}. 
   spec must follow r-table spec format.
   The ui shows a table with specified columns,
   Specified formats, created from the bar-algo-ds"
  [spec bar-algo-ds]
  (assert (agtable-spec? spec) "agtable-spec needs to have :columns key")
  ^{:render-fn 'quanta.viz.render.core/render-spec} ; needed for notebooks
  {:render-fn 'quanta.viz.render.agtable/agtable
   :data (-> bar-algo-ds
             (format-date spec)
             (select-columns spec)
             #_(tc/rename-columns {:open "open"
                                   :high "high"})
             store-dataset)
   :spec spec})

(defn agtable
  "ag-grid table-plot with specified columns (with custom formatting)
   spec must follow agtable-spec format.
   bar-signal-ds must be a tml/dataset with columns as specified.
   returns a plot specification {:render-fn :spec :data}."
  [spec bar-signal-ds]
  (if (nom/anomaly? bar-signal-ds)
    (plot/anomaly bar-signal-ds)
    (agtable-render-spec-impl spec bar-signal-ds)))