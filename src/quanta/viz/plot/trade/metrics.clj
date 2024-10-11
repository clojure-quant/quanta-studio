(ns quanta.viz.plot.trade.metrics)

(defn metrics-ui [metrics]
  (with-meta
    metrics
    {:render-fn 'quanta.viz.render.trade.metrics/metrics-view}))

