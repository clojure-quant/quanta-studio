(ns quanta.studio.bruteforce
  (:require
   [quanta.trade.bruteforce :as bf]
   [quanta.studio.template.db :refer [load-template load-with-options]]))

(defn bruteforce
  "bruteforce-opts example:
   :template-id :algo/bollinger
   :cell-id :backtest
   :options {}
   :variations []
   :target-fn get-pf
   :show-fn show-fn
   :dt dt
   :label \"brute1\""
  [{:keys [calculate bruteforce-dir] :as this}
   {:keys [template-id options]
    :or {options {}}
    :as bruteforce-opts}]
  (if-let [;templ (load-template this template-id)
           templ (load-with-options this template-id options)]
    (bf/bruteforce calculate
                   (merge bruteforce-opts
                          {:algo (:algo templ)
                           ;:options options
                           :data-dir bruteforce-dir}))
    (throw (ex-info "quanta-template could not be found!"
                    {:template template-id
                     :bruteforce-opts bruteforce-opts}))))

(defn show-available [{:keys [bruteforce-dir] :as this}]
  (bf/show-available bruteforce-dir))

(defn load-label [{:keys [bruteforce-dir] :as this} label]
  (bf/load-label bruteforce-dir label))


