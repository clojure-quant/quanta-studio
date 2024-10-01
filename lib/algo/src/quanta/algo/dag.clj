(ns quanta.algo.dag
  (:require
   [missionary.core :as m]
   [nano-id.core :refer [nano-id]]
   [quanta.algo.dag.util :as util]
   [quanta.algo.dag.trace :as trace]))

(defn add-cell [dag cell-id cell]
  (swap! (:cells dag) assoc cell-id  (m/stream cell))
  dag)

(defn get-cell [dag cell-id]
  (get @(:cells dag) cell-id))


(defn- msg-flow [!-a]
   (m/observe
    (fn [!]
      (reset! !-a !)
      (fn []
        (reset! !-a nil)))))

(defn- flow-sender
  "returns {:flow f
            :send s}
    (s v) pushes v to f."
  [initial-v]
  (let [!-a (atom initial-v)]
    {:flow (msg-flow !-a)
     :send (fn [v]
             (when-let [! @!-a]
               (! v)))}))

(defn add-constant-cell [dag cell-id initial-v]
   (add-cell dag cell-id (m/seed [initial-v])))

(defn add-input-cell [dag cell-id initial-v]
  (let [{:keys [flow send]} (flow-sender initial-v)]
    (swap! (:inputs dag) assoc cell-id send) 
    ;(add-cell dag cell-id (m/seed [input]))
    (add-cell dag cell-id flow)))


(defn modify-input-cell [dag cell-id new-v]
   (if-let [send-fn (get @(:inputs dag) cell-id)]
     (send-fn new-v)
     (throw (ex-info "cannot modify non-existing input cell" {:cell-id cell-id}))))

(defn- get-cell-or-throw [dag cell-id]
  (let [cell (get-cell dag cell-id)]
    (when-not cell
      (throw (ex-info "cell-not-found" {:cell-id cell-id
                                        :msg "cell not found in dag."})))
    cell))

(defn add-formula-cell [dag cell-id formula-fn input-cell-id-vec]
  (assert dag "dag needs to be non nil")
  (assert (vector? input-cell-id-vec) "input-cell-id-vec needs to be a vector")
  (let [input-cells (map #(get-cell-or-throw dag %) input-cell-id-vec)
        formula-fn-wrapped (fn [& args]
                             (try
                               (let [start (. System (nanoTime))
                                     result (apply formula-fn args)
                                     stime (str "\r\ncell " cell-id
                                                " calculated in "
                                                (/ (double (- (. System (nanoTime)) start)) 1000000.0)
                                                " msecs")]
                                 (when (:logger dag)
                                   (trace/write-text (:logger dag) stime))
                                 result)
                               (catch Exception ex
                                 (when (:logger dag)
                                   (trace/write-ex (:logger dag) cell-id ex))
                                 (throw ex))))
        formula-cell (apply m/latest formula-fn-wrapped input-cells)]
    (add-cell dag cell-id formula-cell)))

(defn create-dag
  ([]
   (create-dag {}))
  ([{:keys [id log-dir]
     :or {id (nano-id 5)}}]
   {:id id
    :cells (atom {})
    :inputs (atom {})
    :logger (when log-dir
              (trace/setup log-dir id))}))

(defn get-current-value [dag cell-id]
  (let [cell (get-cell dag cell-id)]
    (m/? (util/current-v cell))))

