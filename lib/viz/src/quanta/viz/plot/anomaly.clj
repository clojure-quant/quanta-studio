(ns quanta.viz.plot.anomaly
  (:require
   [de.otto.nom.core :as nom]))

(defn remove-ex [[type category data-map]]
  ; [::nom/anomaly ::my-category {:some data}]
  [type category (dissoc data-map :ex)])

(defn anomaly
  "returns a plot specification {:render-fn :spec :data}. 
   The ui shows the error message of a nom-anomaly."
  [nom-anomaly]
  ^{:render-fn 'ta.viz.renderfn/render-spec} ; needed for notebooks
  {:render-fn 'ta.viz.renderfn.error/nom-error
   :data (remove-ex nom-anomaly)
   :spec :whatever})