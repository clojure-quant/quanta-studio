(ns quanta.dali.plot.trade.metrics
  (:require
   [dali.spec :refer [create-dali-spec]]))

(defn metrics-ui [metrics]
  (create-dali-spec
   {:viewer-fn 'quanta.dali.viewer.trade.metrics/metrics-view
    :data metrics}))
