(ns quanta.template.viz
  (:require
   [de.otto.nom.core :as nom]
   [taoensso.timbre :as log :refer [tracef debug debugf info infof warn error errorf]]
   [ta.algo.error-report :refer [save-error-report]]
   [ta.algo.compile :refer [compile-symbol]]))

(defn get-viz-mode-input [template mode model-result-a]
  (let [viz-spec (get template mode)
        key (:key viz-spec)]
    (if key (key model-result-a)
        model-result-a)))

(defn create-viz-fn [template mode id]
  ;(info "create-viz-fn: " viz)
  (let [viz-spec (get template mode)
        {:keys [viz viz-options]
         :or {viz-options {}}} viz-spec
        viz-fn (compile-symbol viz)
        ; 2024 09 20 awb99: we cannot make an assumption her to the applied options of the algo.
        ; for single algo this is a map. But for multiple algos this is a vector. In the future
        ; there might be even more options

        ;algo-options (or (:algo template) {})
        ; viz options are static. 
        ; algo options can be changed in ui or code
        ; therefore algo options need to be the second parameter
        ;merged-options (merge viz-options algo-options)
        ]
    (if (nom/anomaly? viz-fn)
      viz-fn
      (fn [result]
        (if (nom/anomaly? result)
          result
          (try
            (info "calculating visualization mode:" mode " template: " id " .. ")
            ;(warn "viz-fn input: " result)
            (let [r (viz-fn viz-options result)]
              (debug "calculating visualization mode:" mode " template: " id " DONE! ")
              r)
            (catch Exception ex
              (let [filename (save-error-report "viz" template ex)]
                (error "algo-viz " id " exception. details: " filename)
                (nom/fail ::algo-calc {:message "algo viz exception!"
                                       :filename filename
                                       :location :visualize})))))))))