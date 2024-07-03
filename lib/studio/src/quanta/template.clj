(ns quanta.template
  (:require
   [taoensso.timbre :refer [debug info warn error]]
   [com.rpl.specter :as specter]))

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
  (info "getting default values options: " options)
  (let [paths (map :path options)]
    ;(info "paths: " paths)
    (->> (map #(get-default-value template %) paths)
         (into {}))
    #_(:algo template)))

(defn get-options
  "returns the options (what a user can edit) for a template-id"
  [template]
  (let [options (or (:options template) [])
        options (if (vector? options)
                  options
                  (options))]
    {:options options
     :current (get-default-values template options)}))

(defn apply-options
  "sets options for a template. 
   returns a variation of the template"
  [template options]
  ; if all paths are keys, this is really simple.
  ; (update template :algo merge options)
  ; but if we can have hierarchical paths, then we 
  ; need to set them via specter, so type gets
  ; preserved. 
  (assoc template :algo
         (reduce
          (fn [r [path v]]
            (let [path (if (keyword? path)
                         [path]
                         path)]
              (debug "setting path: " path " to val: " v)
              (specter/setval path v r)))
          (:algo template)
          options)))


(defn permutate [template option-key option-permutations]
  (map #(apply-options template {option-key %}) option-permutations))
  


(comment

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


 ; 
  )