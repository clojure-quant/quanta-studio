(ns quanta.studio.template.bruteforce
  (:require
   [quanta.trade.bruteforce :as bf]
   [quanta.studio.template.db :refer [load-template load-with-options]]))

(defn bruteforce
  "runs all variations on a template
   bruteforce-opts:
     :template-id a template that is added to quanta studio, example: 
     :cell-id the dag cell from the template that gets run
     :options overrides the default options of the template (done once on startup)
     :variations is a vector of [path value] tuples (partitions)
     :show-fn is a fn that receives the algo-mode-result and that must return a map 
        with data that should be associated to the variation-row.
     :target-fn is a value calculated from the algo-mode-result. It represents
       the value that we want to optimize (or are interested in)
   example:
     {:template-id :algo/bollinger
      :cell-id :backtest
      :options {}
      :variations [:asset [\"BTCUSDT\" \"TRXUSDT\"]
                  [:exit 1] [60 90]]
      :target-fn get-pf
      :show-fn show-fn
      :dt dt
      :label \"brute1\"}"
  [{:keys [calculate bruteforce-dir] :as this}
   {:keys [template-id options]
    :or {options {}}
    :as bruteforce-opts}]
  (if-let [;templ (load-template this template-id)
           templ (load-with-options this template-id options)]
    (bf/bruteforce calculate
                   (merge bruteforce-opts
                          {:algo (:algo templ)
                           :label-info {:template-id template-id}
                           :data-dir bruteforce-dir}))
    (throw (ex-info "quanta-template could not be found!"
                    {:template template-id
                     :bruteforce-opts bruteforce-opts}))))

(defn show-available [{:keys [bruteforce-dir] :as this}]
  (bf/show-available bruteforce-dir))

(defn load-label [{:keys [bruteforce-dir] :as this} label]
  (bf/load-label bruteforce-dir label))


