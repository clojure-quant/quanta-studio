(ns quanta.algo.template
  (:require
   [taoensso.timbre :refer [debug info warn error]]
   [quanta.algo.options :as algo-opts]))

(defn get-views 
  "returns all vizualisation-ids of a template
   in a vector."
  [template]
  (let [views (-> template
                  (dissoc :id :options :algo)
                  (keys)
                  sort)]
    (->> views
         (concat [:select-viz])
         (into []))))

(defn- get-default-value [template path]
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

(defn- get-default-values [template options]
  ;(info "getting default values options: " options)
  (let [paths (map :path options)]
    ;(info "paths: " paths)
    (->> (map #(get-default-value template %) paths)
         (into {}))
    #_(:algo template)))

(defn template-info
  "returns information about a template.
   :views - a vector of vizualisation ids
   :options - a list of options that can be edited in the ui
   :current - the default default options of the algo"
  [template]
  (let [options (or (:options template) [])
        options (if (vector? options)
                  options
                  (options) ; options could be a function, in which case we need to execute it.
                  )]
    {:options options
     :current (get-default-values template options)
     :views (get-views template)}))

(defn- get-option-by-path [template path]
  (let [options (or (:options template) [])]
    (->> (filter #(or (= (:path %) path)
                      (= [(:path %)] path)) options)
         first)))

(defn- coerce-to
  "returns a keyword if value in path should be coerced"
  [template path]
  (:coerce (get-option-by-path template path)))

(defn- coerce-value
  "returns a keyword if value in path should be coerced"
  [template path v]
  (if-let [c (coerce-to template path)]
    (do (info "coercing value in path: " path " coercer: " c " v: " v)
        [path (case c
                :int (parse-long (str v))
                :double (parse-double (str v))
                v)])
    [path v]))

(defn- coerce-options [template options]
  (->> options
       (map (fn [[path v]]
             (coerce-value template path v)))
       (into {})))

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
   (try (let [options (if coerce-enabled?
                        (coerce-options template options)
                        options)]
          (assoc template :algo (algo-opts/apply-options (:algo template) options)))
        (catch Exception ex
          (error "template:apply-options"
                             {:template template
                              :options options}
                             ex)
          (throw (ex-info "options-apply-ex" {:options options}))))))


(comment

 

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


    

;
  )