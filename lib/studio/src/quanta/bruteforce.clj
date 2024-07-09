(ns quanta.bruteforce
  (:require
   [de.otto.nom.core :as nom]
   [taoensso.timbre :refer [info warn error]]
   [missionary.core :as m]
   [quanta.template :as qtempl]
   [quanta.template.db :as template-db]
   [quanta.studio :refer [backtest-template]]))

(defn load-with-options [this template-id options]
  (let [template (template-db/load-template this template-id)
        template (qtempl/apply-options template options)]
    (info "template " template-id " options: " (:algo template))
    ;(warn "full template: " template)
    template))

(defn create-variations
  "creates template variations with the same mode"
  [this template-id options variation-spec]
  (info "starting template: " template-id " variations: " variation-spec)
  (let [template (load-with-options this template-id options)
        template-seq (qtempl/create-template-variations template variation-spec)]
    template-seq))

    ;(doall (map #(start-template this % mode) template-seq))))

(defn variation-keys [variations]
  (->> variations
       (partition 2)
       (map first)))

(defn get-variation [template v]
  (cond
    (keyword? v)
    [v (get template v)]

    (vector? v)
    [v (get-in template v)]

    :else
    []))

(defn summarize [template variations]
  ; the old version only was working for variations that were keywords
  #_(-> template :algo
        (select-keys (variation-keys variations)))
  (->> (variation-keys variations)
       (map #(get-variation (:algo template) %))
       (into {})))

(defn run-target-fn-safe [target-fn result]
  (if (nom/anomaly? result)
    0.0
    (target-fn result)))

(defn run-show-fn-safe [show-fn result]
  (try
    (show-fn result)
    (catch Exception ex
      {})))

(defn create-algo-task [this variations mode target-fn show-fn template]
  ; needs to throw so it can fail.
  ; returned tasks will not be cpu intensive, so m/cpu.
  (m/via m/cpu
         (let [result (backtest-template this template mode)
               summary (summarize template variations)
               target {:target (run-target-fn-safe target-fn result)}
               show (run-show-fn-safe show-fn result)]
           (merge summary target show))))

(defn bruteforce [this {:keys [template-id mode options variations target-fn show-fn]
                        :or {show-fn (fn [result] {})}}]
  ; from: https://github.com/leonoel/missionary/wiki/Rate-limiting#bounded-blocking-execution
  ; When using (via blk ,,,) It's important to remember that the blocking thread pool 
  ; is unbounded, which can potentially lead to out-of-memory exceptions. 
  ; A simple way to work around it is by using a semaphore to rate limit the execution:
  (let [sem (m/sem 10)
        template-seq (create-variations this template-id options variations)
        tasks (map #(create-algo-task this variations mode target-fn show-fn %) template-seq)
        ;tasks-limited (map #(limit-task sem %) tasks)
        ]
    (info "brute force backtesting " (count tasks) " variations ..")
    (let [result (m/?
                  (apply m/join vector tasks))]
      (->> result
           (sort-by :target)
           (reverse)))))

(comment

  (require '[modular.system])
  (def s (modular.system/system :studio))

  (def options
    {:source :nippy
     :calendar [:crypto :m]
     :trailing-n 5000
     :entry [:fixed-amount 7]})

  (def variations
    [:asset [;"BTCUSDT" 
             ;"ETHUSDT"
             ;"BNBUSDT" 
             "TRXUSDT"]
     ;:k1 [1.0 1.5]
     [:exit 1] [60 90]])

  (variation-keys variations)

  (require '[clojure.pprint :refer [print-table]])

  (->> (create-variations s :alex/bollinger options variations)
       ;count
       ;first
       ;:algo
       (map #(summarize % variations))
       print-table)

  (identity 5)

  (defn get-pf [r]
    (-> r :metrics :roundtrip :pf))

  (defn show-fn [r]
    (-> r :metrics :roundtrip (select-keys [:trades])))

  (-> (bruteforce
       s
       {:template-id :alex/bollinger
        :mode :backtest-raw
        :options options
        :variations variations
        :target-fn get-pf
        ;:show-fn show-fn
        })
      print-table)

;identity
 ;:metrics

; 
  )

