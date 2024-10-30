(ns quanta.studio
  (:require
   [taoensso.timbre :refer [info warn error]]
   [taoensso.telemere :as tm]
   [babashka.fs :as fs]
   [clj-service.core :refer [expose-functions]]
   ;[dali.plot.anomaly :as plot]
   [quanta.studio.template.calculate :refer [calculate-init]]
   [quanta.studio.template.db :as template-db]
   ;[quanta.studio.template.task.core]
   ;
   ))

(defn start-studio
  "calculate are the dag opts {:log-dir :env}"
  [{:keys [exts clj role
           calculate
           bruteforce-dir
           layout-dir]
    :or {bruteforce-dir ".data/public/bruteforce/"
         layout-dir ".data/public/layout/"}}]
  (tm/log! "starting quanta-studio..")
  ;(info "starting quanta-studio..")
  ; this assert fucks up the starting of the clip system
  ;(assert calculate "studio needs :calculate (calculation-dag settings)")
  (let [this {:templates (atom {})
              :subscriptions-a (atom {})
              :calculate calculate
              :bruteforce-dir bruteforce-dir
              :layout-dir layout-dir}]
    (template-db/add-templates this exts)
    (tm/log! "dag init ..")
    (calculate-init this)

    (when bruteforce-dir
      (tm/log! (str "ensuring bruteforce-dir: " bruteforce-dir))
      (fs/create-dirs bruteforce-dir))

    (when layout-dir
      (tm/log! (str "ensuring layout-dir: " layout-dir))
      (fs/create-dirs layout-dir))

    (if clj
      (do
        (tm/log! "starting quanta-studio clj-services..")
        (expose-functions clj
                          {:name "quanta-studio"
                           :symbols [; template
                                     'quanta.studio.template.db/available-templates
                                     'quanta.studio.template.db/template-info ; one template
                                     'quanta.studio.template.db/template-help
                                     'quanta.studio.template.db/templates-info ; all templates
                                     ; calculate
                                     'quanta.studio.template.calculate/calculate
                                     ; bruteforce
                                     'quanta.studio.template.bruteforce/show-available
                                     'quanta.studio.template.bruteforce/load-label
                                     ; layout
                                     'quanta.studio.layout.core/save-layout
                                     'quanta.studio.layout.core/load-layout
                                     'quanta.studio.layout.core/files
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







