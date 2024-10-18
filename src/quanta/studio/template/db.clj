(ns quanta.studio.template.db
  (:require
   [taoensso.timbre :as log :refer [tracef debug debugf info infof warn error errorf]]
   [clojure.java.io]
   [commonmark-hiccup.core :refer [markdown->hiccup]]
   [extension :as ext]
   [quanta.algo.template :as algo-template]))

(defn add
  "adds a template to the template-db
   templates are used in the browser so traders can add
   and configure algos easily."
  [{:keys [templates] :as this}  {:keys [id algo] :as template-spec}]
  (assert id "missing mandatory parameter :id")
  (assert algo "missing mandatory parameter :algo")
  (swap! templates assoc id template-spec)
  this)

(defn add-template [this template-symbol]
  (try
    (info "adding template: " template-symbol)
    (when-let [template-var (requiring-resolve template-symbol)]
      (let [template-val (var-get template-var)]
        (add this template-val)))
    (catch Exception ex
      (error "could not resolve template " template-symbol " ex: " ex)
      (throw (ex-info "quanta-template could not be resolved"
                      {:template template-symbol})))))

(defn add-templates
  "adds templates from extensions"
  [this exts]
  (info "searching for templates in extensions.. ")
  (let [template-symbols (ext/get-extensions-for exts :quanta/template concat [] [])]
    (info "adding templates: " template-symbols)
    (doall (map #(add-template this %) template-symbols))))

(defn load-template
  "returns the template for a template-id"
  ; note: get is used, because template-id might be a string.
  [{:keys [templates]} template-id]
  (-> @templates (get template-id)))

(defn available-templates
  "returns all template-ids. 
   used in the browser to select a template"
  [{:keys [templates]}]
  (-> @templates keys sort))

(defn load-with-options [this template-id options]
  (assert options "options may not be nil")
  (assert (map? options) "options needs to be a map")
  (if-let [template (load-template this template-id)]
    (algo-template/apply-options template options true)
    (throw (ex-info "template not found" {:template-id template-id}))))

(defn template-info
  "returns the options (what a user can edit) for a template-id"
  ; exposed at start of studio
  [this template-id]
  (info "getting options for template: " template-id)
  (-> (load-template this template-id)
      (algo-template/template-info)))

(defn load-from-resource [filename]
  (let  [file (-> filename
                  clojure.java.io/resource
                  .getFile)]
    (slurp file)))

(defn template-help [this template-id]
  (let [template (load-template this template-id)
        md (:md template)]
    (cond
      md
      (markdown->hiccup (load-from-resource md))
      template
      [:div [:p "template does not have markdown file!"]]
      :else
      [:div [:p "template not found: " template-id]])))
