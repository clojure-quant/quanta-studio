(ns dev.algo-bollinger
  (:require
   [tech.v3.datatype :as dtype]
   [tablecloth.api :as tc]
   [ta.indicator :as ind]
   [ta.indicator.band :as band]
   [ta.indicator.signal :refer [cross-up]]
   [quanta.dag.env :refer [log]]
   [quanta.algo.env.bars :refer [get-trailing-bars]]
   [quanta.trade.backtest :refer [backtest]]
   [quanta.trade.backtest2 :as b2]
   [quanta.viz.plot.trade.core :refer [roundtrip-stats-ui]]))

(defn entry-one [long short]
  (cond
    long :long
    short :short
    :else :flat))

(defn bollinger-calc [opts dt]
  (println "bollinger-calc dt: " dt " opts: " opts)
  (log "bollinger-dt: " dt)
  (log "bollinger-opts: " opts)
  (let [n (or (:atr-n opts) 2)
        k (or (:atr-k opts) 1.0)
        ds-bars (get-trailing-bars opts dt)
        ;_ (log "trailing-bars: " ds-bars) ; for debugging - logs to the dag logfile
        ds-bollinger (band/add-bollinger {:n n :k k} ds-bars)
        long-signal (cross-up (:close ds-bollinger) (:bollinger-upper ds-bollinger))
        short-signal (cross-up (:close ds-bollinger) (:bollinger-lower ds-bollinger))
        entry (dtype/clone (dtype/emap entry-one :keyword long-signal short-signal))
        ds-signal (tc/add-columns ds-bollinger {:entry entry
                                                :atr (ind/atr {:n n} ds-bars)})
        ]
    ds-signal))

(defn bollinger-stats [opts ds-d ds-m]
  (let [day-mid (-> ds-d :bollinger-mid last)
        min-mid (-> ds-m :bollinger-mid last)]
    {:day-dt (-> ds-d :date last)
     :day-mid day-mid
     :min-dt (-> ds-m :date last)
     :min-mid min-mid
     :diff (- min-mid day-mid)}))

(def bollinger-algo
  [{:asset "BTCUSDT"} ; this options are global
   :day {:calendar [:crypto :d]
         :algo  bollinger-calc
         :bardb :nippy
         :trailing-n 1100
         :atr-n 10
         :atr-k 0.6}
   :min {:calendar [:crypto :m]
         :algo bollinger-calc   ; min gets the global option :asset 
         :bardb :nippy
         :trailing-n 20         ; on top of its own local options 
         :atr-n 5
         :atr-k 0.3}
   :stats {:formula [:day :min]
           :algo bollinger-stats
           :carry-n 2}
   :backtest-old {:formula [:day]
              :algo backtest
              :entry [:fixed-amount 100000]
              :exit [:loss-percent 2.0
                     :profit-percent 1.0
                     :time 5]}
   :backtest {:formula [:day]
              :algo b2/backtest
              :entry {:type :fixed-qty :fixed-qty 1.0}
              :exit [{:type :trailing-stop-offset :col :atr}
                     {:type :stop-prct :prct 2.0}
                     {:type :profit-prct :prct 1.0}
                     {:type :time :max-bars 10}]}
   
   ])

;; TEMPLATE

(defn viz-print [opts data]
  (log "calculating viz-fn with data: " data)
  {:creator "viz-print"
   :data data
   :viz-opts opts})

(def bollinger-template
  {:id :bollinger
   :algo bollinger-algo
   :options [{:type :select
              :path [0 :asset]
              :name "asset"
              :spec ["BTCUSDT" "ETHUSDT"]}
             {:type :string
              :path [2 :atr-n]
              :name "atr-n"
              :coerce :int}]
   :print {:viz viz-print
           :viz-options {:print-mode :simple}
           :key :day}
   :backtest-raw {:viz viz-print
                  :viz-options {}
                  :key :backtest}
   :backtest {:viz roundtrip-stats-ui
              :viz-options {}
              :key :backtest}})