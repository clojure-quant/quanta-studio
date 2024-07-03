(ns quanta.studio.template
  (:require
   [de.otto.nom.core :as nom]
   [taoensso.timbre :as log :refer [tracef debug debugf info infof warn error errorf]]
   [quanta.template :as qtempl]))

(defn add
  "adds a template to the template-db
   templates are used in the browser so traders can add
   and configure algos easily."
  [{:keys [templates]} {:keys [id algo] :as template-spec}]
  (assert id "missing mandatory parameter :id")
  (assert algo "missing mandatory parameter :algo")
  (swap! templates assoc id template-spec))

(defn available-templates
  "returns all template-ids. 
   used in the browser to select a template"
  [{:keys [templates]}]
  (-> @templates keys sort))

(defn load-template
  "returns the template for a template-id"
  ; note: get is used, because template-id might be a string.
  [{:keys [templates]} template-id]
  (-> @templates (get template-id)))

(defn get-options
  "returns the options (what a user can edit) for a template-id"
  [this template-id]
  (info "getting options for template: " template-id)
  (-> (load-template this template-id)
      (qtempl/get-options)))

(defn load-with-options [this template-id options]
  (let [template (load-template this template-id)
        template (qtempl/apply-options template options)]
    (info "template " template-id " options: " (:algo template))
    ;(warn "full template: " template)
    template))

(defn- get-fn [fun]
  (if (symbol? fun)
    (requiring-resolve fun)
    fun))

(defn show-result [template result viz-mode]
  (let [{:keys [viz viz-options]} (get template viz-mode)
        viz-fn (get-fn viz)]
    (if viz-fn
      (if (nom/anomaly? result)
        result
        (try
          (let [r (if viz-options
                    (viz-fn viz-options result)
                    (viz-fn result))]
            r)
          (catch Exception ex
            (error "viz calc exception: " ex)
            (nom/fail ::algo-calc {:message "algo viz exception!"
                                   :location :visualize}))))
      (nom/fail ::unknown-viz {:message (str "algo viz not found: " viz-mode)}))))

(comment

  

  (def template (load-template :juan-fx))

  (load-with-options :juan-fx {[1 :atr-n] 20})

  (assoc-in template [:algo 1 :atr-n] 50)
  (assoc-in template [:algo 1 :atr-n] 50)

  {:asset "ETHUSDT",
   [1 :atr-n] 30,
   [1 :asset] "NZD/USD",
   [3 :asset] "NZD/USD"}


; 
  )


