(ns ta.viz.publish
  (:require
   [taoensso.timbre :refer [info warn error]]
   [quanta.dali.plot :as plot]))

(defonce topics (atom {}))

(defn topic-keys []
  (keys @topics))

(defn get-topic [k]
  (info "get-topic: " k)
  (let [v (get @topics k)]
    ; logging is bad when data of charts/tables is really big.
    ;(info "topic: " v)
    v))

(defn publish [env spec render-spec]
  (when render-spec
    (let [topic (:topic spec)]
      (assert topic "publish needs to have :topic spec")
      (info "publishing topic: " topic)
      (swap! topics assoc topic render-spec)
      render-spec)))

(defn publish-ds->table
  "publishes a dataset, the columns that will be displayed, 
   and its formatting depend on the spec."
  [env spec ds]
  (when ds
    (let [cols (:columns spec)]
      (assert cols "publish-dataset needs to have :columns spec")
      (publish env spec (plot/rtable-ds spec ds)))))

(defn publish-ds->highstock [env spec ds]
  (when ds
    (let [cols (:charts spec)]
      (assert cols "publish-dataset needs to have :charts spec")
      (publish env spec (plot/highstock-ds env spec ds)))))



