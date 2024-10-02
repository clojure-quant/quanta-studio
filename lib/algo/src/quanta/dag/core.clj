(ns quanta.dag.core
  (:require
   [missionary.core :as m]
   [nano-id.core :refer [nano-id]]
   [quanta.dag.util :as util]
   [quanta.dag.trace :as trace]
   [quanta.dag.env]))

; no value

(defrecord no-val [cell-id])

(defn create-no-val [cell-id]
  (no-val. cell-id))

(defn is-no-val? [v]
  (instance? no-val v))

; cell db

(defn add-cell [dag cell-id cell]
  (swap! (:cells dag) assoc cell-id cell
         ;(m/stream cell)
         )
  dag)

(defn get-cell [dag cell-id]
  (get @(:cells dag) cell-id))

(defn cell-ids [dag]
  (-> @(:cells dag) keys))

#_(defn- msg-flow [!-a]
    (m/observe
     (fn [!]
       (reset! !-a !)
       (fn []
         (reset! !-a nil)))))

#_(defn- flow-sender
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

#_(defn add-input-cell [dag cell-id initial-v]
    (let [{:keys [flow send]} (flow-sender initial-v)]
      (swap! (:inputs dag) assoc cell-id send)
    ;(add-cell dag cell-id (m/seed [input]))
      (add-cell dag cell-id flow)))

#_(defn modify-input-cell [dag cell-id new-v]
    (if-let [send-fn (get @(:inputs dag) cell-id)]
      (send-fn new-v)
      (throw (ex-info "cannot modify non-existing input cell" {:cell-id cell-id}))))

(defn- get-cell-or-throw [dag cell-id]
  (let [cell (get-cell dag cell-id)]
    (when-not cell
      (throw (ex-info "cell-not-found" {:cell-id cell-id
                                        :msg "cell not found in dag."})))
    cell))

(defn some-input-no-value? [args]
  (some is-no-val? args))

(comment
  (some-input-no-value? [1 2 3])
  (some-input-no-value? [1 2 3 nil])
  (some-input-no-value? [1 2 3 nil (create-no-val :34)])
 ; 
  )

(defn add-formula-cell [dag cell-id formula-fn input-cell-id-vec]
  (assert dag "dag needs to be non nil")
  (assert (vector? input-cell-id-vec) "input-cell-id-vec needs to be a vector")
  (let [input-cells (map #(get-cell-or-throw dag %) input-cell-id-vec)
        _ (println "all input cells are good!")
        formula-fn-wrapped (fn [& args]
                             (if (some-input-no-value? args)
                               (create-no-val cell-id)
                               (try
                                 (let [start (. System (nanoTime))
                                       result (with-bindings (:env dag)
                                                (apply formula-fn args))
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
                                   (throw ex)))))
        formula-cell (apply m/latest formula-fn-wrapped input-cells)]
    (add-cell dag cell-id formula-cell)))

(defn create-dag
  ([]
   (create-dag {}))
  ([{:keys [id log-dir env opts]
     :or {id (nano-id 5)
          env {}
          opts {}}}]
   (let [dag {:id id
              :cells (atom {})
              :inputs (atom {})
              :opts (atom opts)
              :logger (when log-dir
                        (trace/setup log-dir id))
              :tasks (atom {})}]
     (assoc dag :env (merge {#'quanta.dag.env/*dag* dag}
                            env)))))

(defn get-current-value [dag cell-id]
  (let [cell (get-cell dag cell-id)]
    (m/? (util/current-v cell))))

(defn -listen
  ; from ribelo/praxis
  "[pure] creates a `listener` for the [[Node]] of the `dag`, every time the
  value of a [[Node]] changes the function is called.


  function `f` should take two arguments, the first is the listener `id`, the
  second is the [[Node]] value. returns a function that allows to delete a
  `listener`


  use as event
  ```clojure
  (emit ::listen! id f)
  ```"
  [>flow f]
  (m/ap
   (m/?> (m/eduction (comp (map (fn [[e v]] (f e v)))) >flow))))

(defn take-first-non-noval [f]
  ; flows dont implement deref
  (m/eduction
   (remove is-no-val?)
   (take 1)
   f))

(defn current-valid-val
  "gets the first non-nil value from the flow"
  [f]
  (m/reduce (fn [r v]
              (println "current v: " v " r: " r)
              v) nil
            (take-first-non-noval f)))

(defn get-current-valid-value [dag cell-id]
  (let [cell (get-cell dag cell-id)]
    (m/? (current-valid-val cell))))

(defn start!
  "starts a missionary task
   task can be stopped with (stop! task-id).
   useful for working in the repl with tasks"
  [dag cell-id task]
  (let [dispose! (task
                  #(println "task completed: " %)
                  #(println "task crashed: " %))]
    (swap! (:tasks dag) assoc cell-id dispose!)
    (str "use (stop! " cell-id ") to stop this task.")))

(defn stop!
  "stops a missionary task that has been started with start!
    useful for working in the repl with tasks"
  [dag task-id]
  (if-let [dispose! (get @(:tasks dag) task-id)]
    (dispose!)
    (println "cannot stop task - not existing!" task-id)))

(defn start-log-cell
  "starts logging a missionary flow to a file.
   can be stopped with (stop! id) 
   useful for working in the repl with flows."
  [dag cell-id]
  (if-let [cell (get-cell dag cell-id)]
     (let [ log-task (m/reduce (fn [r v]
                                (trace/write-edn (:logger dag) cell-id v)
                                nil)
                              nil cell)]
        (start! dag cell-id log-task))
    (str "cell " cell-id " not found - cannot start!")))

(defn stop-log-cell [dag cell-id]
  (stop! dag cell-id))