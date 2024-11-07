(ns quanta.dali.plot.backtest
  (:require
   [tech.v3.dataset :as tds]
   [de.otto.nom.core :as nom]
   [dali.spec :refer [create-dali-spec]]
   [dali.plot.anomaly :as plot]
   [dali.store.file.transit] ; side effects
   [dali.store :refer [write]]))

(defn ds->map [ds]
  ;(tc/rows :as-maps) ; this does not work, type of it is a reified dataset. 
  ; this works in repl, but when sending data to the browser it fails.
  (into []
        (tds/mapseq-reader ds)))

(defn backtest-ui-ds-impl [{:keys [dali-store]} opts data]
  (create-dali-spec
   {:viewer-fn 'quanta.dali.viewer.backtest/backtest-ui-ds
    :data (write dali-store "transit-json" data)}))

(defn backtest-ui-ds
  "returns a render specification {:render-fn :spec :data}. 
   spec must follow chart-pane format.
   The ui shows a barchart with extra specified columns 
   plotted with a specified style/position, 
   created from the bar-algo-ds"
  ([env spec data]
   (if (nom/anomaly? data)
     (plot/anomaly data)
     (backtest-ui-ds-impl env spec data))))
