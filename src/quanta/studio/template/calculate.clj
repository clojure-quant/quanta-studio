(ns quanta.studio.template.calculate
  (:require
   [clojure.string :as str]
   [taoensso.timbre :refer [info warn error]]
   [taoensso.telemere :as tm]
   [de.otto.nom.core :as nom]
   [nano-id.core :refer [nano-id]]
   [tick.core :as t]
   [babashka.fs :as fs]
   [quanta.algo.template :as templ]
   [quanta.studio.template.db :refer [load-with-options]]
   [quanta.viz.plot.exception :refer [exception]]
   [quanta.viz.plot.anomaly :as plot]))

(defn calculate-init [{:keys [calculate] :as this}]
  (assert calculate "studio needs :calculate (the dag-env for calculations)")
  (assert (map? calculate) "studio needs :calculate (the dag-env for calculations) to be a map")
  ; will create dir hierarchy if not existing, otherwise nothing done.
  (fs/create-dirs (:log-dir calculate))
  this)

(defn safe-date [dt]
  (cond
    ; nil -> current date
    (nil? dt) (t/instant)

    ; use instant / zoned-date-time
    (t/instant? dt) dt
    (t/zoned-date-time? dt) (t/instant dt)

    ; string -> possibly parse it
    (string? dt)
    (try
      (if (str/blank? dt)
        (t/instant)
        (t/instant dt))
      (catch Exception _ex
        (warn "could not parse dt, using now: " dt)))
    ; we expect either a time class or a string, 
    ; if we get something else (which should never happen).
    ; then log it and use current date.
    :else
    (do
      (warn "safe-date unsupported type: " (type dt))
      (t/instant))))

;; todo .. catch exceptions and wrap viz-anomaly

(defn calculate
  "this runs a viz-task once and returns the viz-result.
   output is guaranteed to be always viz-spec format, so
   possible anomalies are converted to viz-spec"
  ([this template-id template-options mode dt]
   (calculate this template-id template-options mode dt (nano-id 6)))
  ([{:keys [calculate] :as this} template-id template-options mode dt task-id]
   (info "calculate template:" template-id "mode: " mode "dt: " dt
         " options: " template-options)
   (let [dt (safe-date dt)
         template (load-with-options this template-id template-options)]
     (try
       (templ/calculate calculate template mode dt) ; task-id
       (catch Exception ex
         (tm/log! (str "dag exception: " ex))
         (exception (str "template: " template-id " viz: " mode)
                    ex))))))
