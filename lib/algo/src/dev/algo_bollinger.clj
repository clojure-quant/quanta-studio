(ns dev.algo-bollinger
  (:require
    [ta.indicator.band :as band]
    [tablecloth.api :as tc]
    [quanta.algo.dag.spec :refer [spec->ops]]
    [quanta.algo.options :refer [apply-options]]
    [quanta.dag.env :refer [log]]
    [quanta.algo.env.bars :refer [get-trailing-bars]]))

(defn bollinger-calc [opts dt]
  (println "bollinger-calc dt: " dt " opts: " opts)
  (log "bollinger-dt: " dt)
  (log "bollinger-opts: " opts)
  (let [n (or (:atr-n opts) 2)
        k (or (:atr-k opts) 1.0)
        ds-bars (get-trailing-bars opts dt)]
    (log "trailing-bars: " ds-bars)
    (->> ds-bars
         (band/add-bollinger {:n n :k k}))))

(defn bollinger-signal [opts ds-d ds-m]
  (println "bollinger-singal opts: " opts)
  (let [day-mid (-> ds-d :bollinger-mid last )
        min-mid (-> ds-m :bollinger-mid last )]
  {:day-dt (-> ds-d :date last)
   :day-mid day-mid
   :min-dt (-> ds-m :date last)
   :min-mid min-mid
   :diff (- min-mid day-mid)}))

(def bollinger-algo
  [{:asset "BTCUSDT"} ; this options are global
   :day {:calendar [:crypto :d]
         :algo  bollinger-calc
         :trailing-n 20
         :atr-n 10
         :atr-k 0.6}
   :min {:calendar [:crypto :m]
         :algo bollinger-calc   ; min gets the global option :asset 
         :trailing-n 20         ; on top of its own local options 
         :atr-n 5
         :atr-k 0.3}
   :signal {:formula [:day :min]
            :algo bollinger-signal
            :carry-n 2}
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
