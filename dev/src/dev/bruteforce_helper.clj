(ns dev.bruteforce-helper
  (:require
   [modular.system]
   [clojure.pprint :refer [print-table]]
   [quanta.algo.env.bars]
   [quanta.studio.template.bruteforce :as bf]))

(def studio (modular.system/system :studio))

(defn get-pf [r]
  (-> r :metrics :roundtrip :pf))

(defn show-fn [r]
  (let [{:keys [roundtrip nav]} (:metrics r)]
    (merge
     (select-keys (:all roundtrip) [:trades])
     (select-keys nav  [:cum-pl :max-drawdown-prct]))))

(defn bruteforce [{:keys [_template-id _variations _label] :as opts}]
  (time
   (-> (bf/bruteforce
        studio
        (merge
         {:cell-id :backtest
          :target-fn get-pf
          :show-fn show-fn}
         opts))
       print-table)))

(defn bruteforce-old [opts]
  (time
   (let [result (bf/bruteforce
                 studio
                 (merge
                  {:cell-id :backtest-old
                   :target-fn get-pf
                   :show-fn show-fn}
                  opts))]
     (print-table (:ok result)))))

