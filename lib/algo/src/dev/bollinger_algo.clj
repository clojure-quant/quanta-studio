(ns dev.bollinger-algo
  (:require
    [ta.indicator.band :as band]
    [quanta.algo.spec :refer [spec->ops]]
    [quanta.algo.options :refer [apply-options]]
    [quanta.algo.env.bars :refer [get-trailing-bars]]))

(defn bollinger-calc [opts dt]
  (println "bollinger-calc dt: " dt " opts: " opts)
  (let [n (or (:atr-n opts) 2)
        k (or (:atr-k opts) 1.0)]
    (->> (get-trailing-bars opts dt)
         (band/add-bollinger {:n n :k k}))))

(defn bollinger-signal [opts d m]
  (println "bollinger-singal opts: " opts)
  (vector d m))

(def bollinger-algo
  [{:asset "BTCUSDT"} ; this options are global
   :day {:calendar [:crypto :d]
         :algo  bollinger-calc
         :trailing-n 20
         :atr-n 10
         :atr-m 0.6}
   :min {:calendar [:crypto :m]
         :algo bollinger-calc   ; min gets the global option :asset 
         :trailing-n 20         ; on top of its own local options 
         :atr-n 5
         :atr-m 0.3}
   ;:signal {:formula [:day :min]
   ;         :algo bollinger-signal
   ;         :carry-n 2}
   ])


(spec->ops bollinger-algo)
;; => [[:day {:calendar [:forex :d],
;;            :algo-fn #function[dev.bollinger-algo/bollinger-calc],
;;            :opts {:asset "BTCUSDT", :calendar [:forex :d], :trailing-n 20, :atr-n 10, :atr-m 0.6}}]
;;     [:min {:calendar [:forex :m],
;;            :algo-fn #function[dev.bollinger-algo/bollinger-calc],
;;           :opts {:asset "BTCUSDT", :calendar [:forex :m], :trailing-n 20, :atr-n 5, :atr-m 0.3}}]
;;     [:signal {:formula [:day :min],
;;               :algo-fn #function[dev.bollinger-algo/bollinger-signal],
;;               :opts {:asset "BTCUSDT", :formula [:day :min], :carry-n 2}}]]



(-> bollinger-algo
    (apply-options {[0 :asset] "ETHUSDT"
                    [4 :calendar] [:forex :h]}))
