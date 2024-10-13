(ns dev.bruteforce-helper
  (:require
   [modular.system]
   [clojure.pprint :refer [print-table]]
   [quanta.algo.env.bars]
   [quanta.studio.bruteforce :as bf]))

(def studio (modular.system/system :studio))

(defn get-pf [r]
  (-> r :metrics :roundtrip :pf))

(defn show-fn [r]
  (let [{:keys [roundtrip nav]} (:metrics r)]
    (merge
     (select-keys roundtrip [:trades])
     (select-keys nav  [:cum-pl :max-dd]))))

(defn bruteforce [template-id variations]
  (time
   (-> (bf/bruteforce
        studio
        {:template-id template-id
         :label "demo-bruteforce"
         :cell-id :backtest
         :variations variations
         :target-fn get-pf
         :show-fn show-fn})
       print-table)))

