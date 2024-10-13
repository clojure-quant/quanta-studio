(ns quanta.studio.bruteforce
  (:require
   [quanta.trade.bruteforce :as bf]
   [quanta.studio.template.db :refer [load-template]]))

(defn bruteforce
  "bruteforce-opts example:
   :template-id :algo/bollinger
   :cell-id :backtest
   :variations []
   :target-fn get-pf
   :show-fn show-fn
   :dt dt
   :label \"brute1\""
  [{:keys [calculate bruteforce-dir] :as this}
   {:keys [template-id] :as bruteforce-opts}]
  (if-let [templ (load-template this template-id)]
    (bf/bruteforce calculate
                   (merge bruteforce-opts
                          {:algo (:algo templ)
                           :data-dir bruteforce-dir}))
    (throw (ex-info "quanta-template could not be found!"
                    {:template template-id
                     :bruteforce-opts bruteforce-opts}))))

(defn show-available [{:keys [bruteforce-dir] :as this}]
  (bf/show-available bruteforce-dir))

(defn load-label [{:keys [bruteforce-dir] :as this} label]
  (bf/load-label bruteforce-dir label))


