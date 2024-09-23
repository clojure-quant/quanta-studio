(ns quanta.template
  (:require
   [taoensso.timbre :refer [debug info warn error]]
   [com.rpl.specter :as specter]
   [ta.algo.error-report :refer [save-error-report]]))

(defn get-default-value [template path]
  (debug "getting default value template: " (:id template) " path: " path)
  (let [algo (:algo template)
        [k v]  (cond
                 (keyword path)
                 [path (get algo path)]

                 (vector? path)
                 [path (get-in algo path)]

                 :else
                 [path nil])]
    ;(info "getting default value algo: " algo " path: " path)
    [k v]))

(defn get-default-values [template options]
  ;(info "getting default values options: " options)
  (let [paths (map :path options)]
    ;(info "paths: " paths)
    (->> (map #(get-default-value template %) paths)
         (into {}))
    #_(:algo template)))

(defn get-views [template]
  (let [views (-> template
                  (dissoc :id :options :algo)
                  (keys)
                  sort)]
    (->> views
         (concat [:select-viz])
         (into []))))

(defn get-options
  "returns the options (what a user can edit) for a template-id"
  [template]
  (let [options (or (:options template) [])
        options (if (vector? options)
                  options
                  (options) ; options could be a function, in which case we need to execute it.
                  )]
    {:options options
     :current (get-default-values template options)
     :views (get-views template)}))

(defn get-option-by-path [template path]
  (let [options (or (:options template) [])]
    (->> (filter #(or (= (:path %) path)
                      (= [(:path %)] path)) options)
         first)))

(defn coerce-to
  "returns a keyword if value in path should be coerced"
  [template path]
  (:coerce (get-option-by-path template path)))

(defn coerce-value
  "returns a keyword if value in path should be coerced"
  [template path v]
  (if-let [c (coerce-to template path)]
    (if (string? v)
      (do (info "coercing value in path: " path " coercer: " c)
          (case c
            :int (parse-long v)
            :double (parse-double v)
            v))
      v)
    v))

(defn apply-options
  "sets options for a template. 
   returns a variation of the template"
  ([template options]
   ; this path is used by bruteforcer
   (apply-options template options false))
  ([template options coerce-enabled?]
  ; if all paths are keys, this is really simple.
  ; (update template :algo merge options)
  ; but if we can have hierarchical paths, then we 
  ; need to set them via specter, so type gets
  ; preserved. 
   (try (assoc template :algo
               (reduce
                (fn [r [path v]]
                  (let [path (if (keyword? path)
                               [path]
                               path)
                        v (if coerce-enabled?
                            (coerce-value template path v)
                            v)]
                    (debug "setting path: " path " to val: " v)
                    (specter/setval path v r)))
                (:algo template)
                options))

        (catch Exception ex
          (save-error-report "template:apply-options"
                             {:template template
                              :options options}
                             ex)
          (ex-info "options-apply-ex" {:options options})))))

(defn- add-key [m [k seq]]
  (map #(assoc m k %) seq))

(defn- map-keys [r key-seq-tuples]
  (let [seq (add-key r (first key-seq-tuples))
        next (rest key-seq-tuples)]
    (if (empty? next)
      seq
      (flatten (map #(map-keys % next) seq)))))

(defn make-variations
  "returns a seq of different options that each can be
   applied to a template.

   example:
     (make-variations [:x [1 2 3] 
                       :y [:a :b :c]
                       :debug [true false]])
   returns:
    ;; => ({:x 1, :y :a, :debug true}
  ;;     {:x 1, :y :a, :debug false}
  ;;     {:x 1, :y :b, :debug true}
  ;;     {:x 1, :y :b, :debug false}
  ;;     {:x 1, :y :c, :debug true}
  ;;     {:x 1, :y :c, :debug false}
  ;;     {:x 2, :y :a, :debug true}
  ;;     {:x 2, :y :a, :debug false}
  ;;     {:x 2, :y :b, :debug true}
  ;;     {:x 2, :y :b, :debug false}
  ;;     {:x 2, :y :c, :debug true}
  ;;     {:x 2, :y :c, :debug false}
  ;;     {:x 3, :y :a, :debug true}
  ;;     {:x 3, :y :a, :debug false}
  ;;     {:x 3, :y :b, :debug true}
  ;;     {:x 3, :y :b, :debug false}
  ;;     {:x 3, :y :c, :debug true}
  ;;     {:x 3, :y :c, :debug false})"
  [variation-spec]
  (let [key-seq-tuples (partition 2 variation-spec)]
    (map-keys {} key-seq-tuples)))

(defn create-template-variations
  "input: template and variation spec
   returns a templates, with different option
   variations applied. example:
   (create-template-variations t :asset [:EURUSD :SPY :BTCUSD]
                        :n [100 200 500])"
  [template variation-spec]
  (let [option-seq (make-variations variation-spec)]
    (map (partial apply-options template) option-seq)))

(comment

  (apply-options {:algo {:x 1
                         :y 2
                         :users {:w "walter"}}}
                 {:x 5
                  [:users :w] "willy"})

  (apply-options {:algo {:x 1
                         :y 2
                         :users {:w "walter"}}}
                 {:x 5
                  [:users :w] "willy"})

  (require '[modular.system])
  (def s (modular.system/system :studio))
  s

  (require '[quanta.studio :refer [get-options load-with-options]])

  (require '[quanta.template.db :as template-db])

  (def t (template-db/load-template s :alex/bollinger))
  t

  (def t (get-options s :alex/bollinger))

  (apply-options t {[:exit :loss-percent] 100,
                    [:exit :profit-percent] 200})

  (load-with-options
   s
   :alex/bollinger
   {[:exit :loss-percent] 100,
    [:exit :profit-percent] 200})

  t

  get-default-values [template options]

  (def paths [:a [:b :c] :d])
  (def data [{:a 1 :b {:c 22 :x 5} :d 55}
             {:x 1 :i {:y 2 :x 5} :d 55}])

      ; option-ui => algo
  (specter/select [0 :b :c] data)
  (specter/setval [0 :b :c] 555 data)

  (specter/setval [0 :b :c] 555 [])

  (specter/select [0 :b :c] data)

  (defn no-path? [p]
    (info "no-path: " p)
    (not (contains? paths p)))

  (defn path? [p]
    (info "path: " p)
    (contains? paths p))

  (no-path? :d)

  (specter/setval [:a specter/ALL] 4 data)

  (specter/transform [0 :b :c]
                     specter/NONE
                     data)

  (specter/select [:a :b] data)

  (specter/setval [1 :asset]  "NZD/USD"
                  [:day {:feed :fx
                         :asset "EUR/USD"}
                   :minute {:type :trailing-bar, :asset "EUR/USD", :import :kibot-http,
                            :trailing-n 1440, :max-open-close-over-low-high 0.3, :volume-sma-n 30}
                   :signal {:formula [:day :minute], :spike-atr-prct-min 0.5, :pivot-max-diff 0.001,
                            :algo 'juan.algo.combined/daily-intraday-combined}])

  ;; VARIATIONS

  (add-key {:calendar [:us :d]}
           [:asset ["a" "b" "c"]])
  (map-keys {:calendar [:us :d]}
            [[:asset ["a" "b" "c"]]
             [:n [100 500 1000]]])

  (make-variations [:x [1 2 3]
                    :y [:a :b :c]])
    ;; => ({:x 1, :y :a}
    ;;     {:x 1, :y :b}
    ;;     {:x 1, :y :c}
    ;;     {:x 2, :y :a}
    ;;     {:x 2, :y :b}
    ;;     {:x 2, :y :c}
    ;;     {:x 3, :y :a}
    ;;     {:x 3, :y :b}
    ;;     {:x 3, :y :c})

  (make-variations [:x [1 2 3]
                    :y [:a :b :c]
                    :debug [true false]])
  ;; => ({:x 1, :y :a, :debug true}
  ;;     {:x 1, :y :a, :debug false}
  ;;     {:x 1, :y :b, :debug true}
  ;;     {:x 1, :y :b, :debug false}
  ;;     {:x 1, :y :c, :debug true}
  ;;     {:x 1, :y :c, :debug false}
  ;;     {:x 2, :y :a, :debug true}
  ;;     {:x 2, :y :a, :debug false}
  ;;     {:x 2, :y :b, :debug true}
  ;;     {:x 2, :y :b, :debug false}
  ;;     {:x 2, :y :c, :debug true}
  ;;     {:x 2, :y :c, :debug false}
  ;;     {:x 3, :y :a, :debug true}
  ;;     {:x 3, :y :a, :debug false}
  ;;     {:x 3, :y :b, :debug true}
  ;;     {:x 3, :y :b, :debug false}
  ;;     {:x 3, :y :c, :debug true}
  ;;     {:x 3, :y :c, :debug false})

; 
  )