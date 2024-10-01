(ns dev.bollinger-algo
  (:require
    [ta.indicator.band :as band]
    [quanta.dag.env.bars :refer [get-trailing-bars]]
    [quanta.dag.algo.spec :as spec]))

(defn bollinger-calc [opts dt]
  (let [n (or (:atr-n opts) 2)
        k (or (:atr-k opts) 1.0)]
    (->> (get-trailing-bars opts dt)
         (band/add-bollinger {:n n :k k}))))

(defn bollinger-signal [opts d m]
  (vector d m))

(def bollinger-algo
  [{:asset "BTCUSDT"}
   :day {:calendar [:forex :d]
         :algo  bollinger-calc
         :trailing-n 20
         :atr-n 10
         :atr-m 0.6}
   :min {:calendar [:forex :m]
         :algo bollinger-calc
         :trailing-n 20
         :atr-n 5
         :atr-m 0.3}
   :signal {:formula [:day :min]
            :algo bollinger-signal
            :carry-n 2}])


(spec/spec->ops bollinger-algo)
;; => [[:day {:calendar [:forex :d],
;;            :algo-fn #function[dev.bollinger-algo/bollinger-calc],
;;            :opts {:asset "BTCUSDT", :calendar [:forex :d], :trailing-n 20, :atr-n 10, :atr-m 0.6}}]
;;     [:min {:calendar [:forex :m],
;;            :algo-fn #function[dev.bollinger-algo/bollinger-calc],
;;           :opts {:asset "BTCUSDT", :calendar [:forex :m], :trailing-n 20, :atr-n 5, :atr-m 0.3}}]
;;     [:signal {:formula [:day :min],
;;               :algo-fn #function[dev.bollinger-algo/bollinger-signal],
;;               :opts {:asset "BTCUSDT", :formula [:day :min], :carry-n 2}}]]
