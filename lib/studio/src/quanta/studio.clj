(ns quanta.studio
  (:require
   [clojure.string :as str]
   [taoensso.timbre :refer [info warn error]]
   [de.otto.nom.core :as nom]
   [nano-id.core :refer [nano-id]]
   [tick.core :as t]
   [clj-service.core :refer [expose-functions]]
   [quanta.studio.template.calculate :refer [calculate-init]]
   [quanta.studio.template.db :as template-db]
   [quanta.viz.plot.anomaly :as plot]
   [taoensso.telemere :as tm]
   ;[quanta.studio.template.task.core]
   ;
   ))

(defn start-studio [{:keys [exts clj role calculate]}]
  (tm/log! "starting quanta-studio..")
  ;(info "starting quanta-studio..")
  ; this assert fucks up the starting of the clip system
  ;(assert calculate "studio needs :calculate (calculation-dag settings)")
  (let [this {:templates (atom {})
              :subscriptions-a (atom {})
              :calculate calculate}]
    (tm/log! "starting quanta-studio..")
    ;(info "starting quanta-studio..")
    (template-db/add-templates this exts)

    (tm/log! "dag init ..")
    (calculate-init this)

    (if clj
      (do
        (tm/log! "starting quanta-studio clj-services..")
        ;(info "starting quanta-studio clj-services..")
        (expose-functions clj
                          {:name "quanta-studio"
                           :symbols [; template
                                     'quanta.studio.template.db/available-templates
                                     'quanta.studio.template.db/template-info
                                     ; calculate
                                     'quanta.studio.template.calculate/calculate
                                     ; task
                                     ;'quanta.studio/start
                                     ;'quanta.studio/stop
                                     ;'quanta.studio/current-task-result
                                     ;'quanta.studio/task-summary
                                     ]
                           :permission role
                           :fixed-args [this]}))
      (warn "quanta-studio starting without clj-services, perhaps you want to pass :clj key"))
    (info "quanta-studio running!")
    this))







