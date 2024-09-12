(ns quanta.viz.plot.rtable
  (:require
   [tick.core :as t]
   [tech.v3.dataset :as tds]
   [tablecloth.api :as tc]
   [de.otto.nom.core :as nom]
   [quanta.viz.plot.anomaly :as plot]))

(defn rtable-cols [spec]
  (map :path (:columns spec)))

(defn rtable-spec? [spec]
  (and (map? spec)
       (:columns spec)
        ; TODO: all cols have :path
       ))

(defn ds->map [ds]
  ;(tc/rows :as-maps) ; this does not work, type of it is a reified dataset. 
  ; this works in repl, but when sending data to the browser it fails.
  (into []
        (tds/mapseq-reader ds)))

(def default-spec
  {:class "table-head-fixed padding-sm table-blue table-striped table-hover"
   :style {:width "100%"
           :height "100%"
           :border "1px solid blue"}})

(defn rtable-render-spec-impl
  "returns a plot specification {:render-fn :spec :data}. 
   spec must follow r-table spec format.
   The ui shows a table with specified columns,
   Specified formats, created from the bar-algo-ds"
  [spec bar-algo-ds]
  (assert (rtable-spec? spec) "rtable-spec needs to have :columns key")
  ^{:render-fn 'ta.viz.renderfn/render-spec} ; needed for notebooks
  {:render-fn 'ta.viz.renderfn.rtable/rtable
   :data (-> bar-algo-ds
             (tc/select-columns (rtable-cols spec))
             ds->map)
   :spec (merge default-spec spec)})

(defn rtable
  "returns a plot specification {:render-fn :spec :data}. 
   plot shows a table with specified columns (with custom formatting)
   spec must follow rtable-spec format.
   bar-signal-ds must be a tml/dataset with columns as specified."
  [spec bar-signal-ds]
  (if (nom/anomaly? bar-signal-ds)
    (plot/anomaly bar-signal-ds)
    (rtable-render-spec-impl spec bar-signal-ds)))