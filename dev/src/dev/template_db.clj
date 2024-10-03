(ns dev.template-db
  (:require 
     [quanta.studio.template.db :as tdb]))

(def s
  (-> {:templates  (atom {})}
      (tdb/add-template 'dev.algo-bollinger/bollinger-template)))

s

(tdb/available-templates s)
;; => (:bollinger)
 
(tdb/template-info s :bollinger)
;; => {:options
;;     [{:type :select, :path [0 :asset], :name "asset", :spec ["BTCUSDT" "ETHUSDT"]}
;;      {:type :string, :path [2 :atr-n], :name "atr-n", :coerce :int}],
;;     :current {[0 :asset] "BTCUSDT", [2 :atr-n] 10},
;;     :views [:select-viz :print]}

(tdb/load-template s :bollinger)
;; => {:id :bollinger,
;;     :algo
;;     [{:asset "BTCUSDT"}
;;      :day
;;      {:calendar [:crypto :d], :algo #function[dev.algo-bollinger/bollinger-calc], :trailing-n 800, :atr-n 10, :atr-k 0.6}
;;      :min
;;      {:calendar [:crypto :m], :algo #function[dev.algo-bollinger/bollinger-calc], :trailing-n 20, :atr-n 5, :atr-k 0.3}
;;      :stats
;;      {:formula [:day :min], :algo #function[dev.algo-bollinger/bollinger-stats], :carry-n 2}
;;      :backtest
;;      {:formula [:day],
;;       :algo #function[quanta.trade.backtest/backtest],
;;       :entry [:fixed-amount 100000],
;;       :exit [:loss-percent 2.0 :profit-percent 1.0 :time 5]}],
;;     :options
;;     [{:type :select, :path [0 :algo], :name "asset", :spec ["BTCUSDT" "ETHUSDT"]}
;;      {:type :string, :path [2 :atr-n], :name "atr-n", :coerce :int}],
;;     :print {:viz #function[dev.algo-bollinger/viz-print], :viz-options {:print-mode :simple}}}

(tdb/load-with-options s :bollinger {[0 :asset] "ETHUSDT"
                                     [2 :trailing-n] 2000})
;; => {:id :bollinger,
;;     :algo
;;     [{:asset "ETHUSDT"}
;;      :day
;;      {:calendar [:crypto :d], :algo #function[dev.algo-bollinger/bollinger-calc], :trailing-n 2000, :atr-n 10, :atr-k 0.6}
;;      :min
;;      {:calendar [:crypto :m], :algo #function[dev.algo-bollinger/bollinger-calc], :trailing-n 20, :atr-n 5, :atr-k 0.3}
;;      :stats
;;      {:formula [:day :min], :algo #function[dev.algo-bollinger/bollinger-stats], :carry-n 2}
;;      :backtest
;;      {:formula [:day],
;;       :algo #function[quanta.trade.backtest/backtest],
;;       :entry [:fixed-amount 100000],
;;       :exit [:loss-percent 2.0 :profit-percent 1.0 :time 5]}],
;;     :options
;;     [{:type :select, :path [0 :algo], :name "asset", :spec ["BTCUSDT" "ETHUSDT"]}
;;      {:type :string, :path [2 :atr-n], :name "atr-n", :coerce :int}],
;;     :print {:viz #function[dev.algo-bollinger/viz-print], :viz-options {:print-mode :simple}}}
