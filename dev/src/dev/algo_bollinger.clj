(ns dev.algo-bollinger
  (:require
   [missionary.core :as m]
   [taoensso.telemere :as tm]
   [tech.v3.datatype :as dtype]
   [tablecloth.api :as tc]
   [ta.indicator :as ind]
   [ta.indicator.band :as band]
   [ta.indicator.signal :refer [cross-up]]
   [quanta.dag.env :refer [log]]
   [quanta.bar.env :refer [get-trailing-bars]]
   [quanta.trade.backtest :refer [backtest]]
   [quanta.trade.backtest2 :as b2]
   [quanta.dali.plot :as plot]))

(defn entry-one [long short]
  (cond
    long :long
    short :short
    :else :flat))

(defn get-trailing-bars-window [env opts dt]
  (m/sp
   (if-let [width (get-in opts [:window :width])]
     (let [trailing-n (min 80 (int (/ width 10)))]
       (log env "trailing window!" (str "window width:" width " trailing# " trailing-n))
       (m/? (get-trailing-bars env (assoc opts :trailing-n trailing-n) dt)))
     (do
       (log env "trailing window-no-width" opts)
       (m/? (get-trailing-bars env opts dt))))))

(defn bollinger-calc [env opts bar-ds]
  (when (and (= (:asset opts) "ETHUSDT")
             (= (:atr-n opts) 50))
    (log env "simulated crash eth-usdt atr-50" :bruteforce-test)
    (tm/log! "simulated crash ethusdt atr-50")
    (throw (ex-info "eth-atr-50-ex" {:message "this is used for bruteforce test"})))
  (log env "bollinger-opts: " opts)
  (log env "bollinger bar-ds: " bar-ds)
  (tm/log! "bollinger start")
  (let [n (or (:atr-n opts) 2)
        k (or (:atr-k opts) 1.0)
        _ (tm/log! "bollinger bar-load complete")
        ;_ (log "trailing-bars: " ds-bars) ; for debugging - logs to the dag logfile
        ds-bollinger (band/add-bollinger {:n n :k k} bar-ds)
        long-signal (cross-up (:close ds-bollinger) (:bollinger-upper ds-bollinger))
        short-signal (cross-up (:close ds-bollinger) (:bollinger-lower ds-bollinger))
        entry (dtype/clone (dtype/emap entry-one :keyword long-signal short-signal))
        ds-signal (tc/add-columns ds-bollinger {:entry entry
                                                :atr (ind/atr {:n n} bar-ds)})]
    (tm/log! "bollinger strategy calc complete")
    ds-signal))

(defn bollinger-stats [opts ds-d ds-m]
  (let [day-mid (-> ds-d :bollinger-mid last)
        min-mid (-> ds-m :bollinger-mid last)]
    {:day-dt (-> ds-d :date last)
     :day-mid day-mid
     :min-dt (-> ds-m :date last)
     :min-mid min-mid
     :diff (- min-mid day-mid)}))

(defn add-positions [opts bar-ds]
  (->> bar-ds
       (b2/entry->roundtrips opts)
       :level-ds))

(def bollinger-algo
  {:* {:asset "BTCUSDT"} ; this options are global
   :bars-day {:calendar [:crypto :d]
              :fn get-trailing-bars-window
              :bardb :nippy
              :trailing-n 1100}
   :day {:formula [:bars-day]
         :fn bollinger-calc
         :env? true
         :atr-n 10
         :atr-k 0.6}
   :bars-min {:calendar [:crypto :m]
              :fn get-trailing-bars-window
              :bardb :nippy
              :trailing-n 20   ; on top of its own local options      
              }
   :min {:formula [:bars-min]
         :fn bollinger-calc   ; min gets the global option :asset 
         :env? true
         :atr-n 5
         :atr-k 0.3}
   :stats {:formula [:day :min]
           :fn bollinger-stats
           :carry-n 2}
   :backtest {:formula [:day]
              :fn b2/backtest
              :portfolio {:fee 0.1 ; per trade in percent
                          :equity-initial 50000.0}
              :entry {:type :fixed-qty :fixed-qty 1.0}
              :exit [;{:type :trailing-stop-offset :col :atr}
                     {:type :stop-prct :prct 5.0}
                     {:type :profit-prct :prct 8.0}
                     {:type :time :max-bars 50}]}

   :position {:formula [:day]
              :fn add-positions
              :portfolio {:fee 0.1 ; per trade in percent
                          :equity-initial 50000.0}
              :entry {:type :fixed-qty :fixed-qty 1.0}
              :exit [;{:type :trailing-stop-offset :col :atr}
                     {:type :stop-prct :prct 5.0}
                     {:type :profit-prct :prct 8.0}
                     {:type :time :max-bars 50}]}})

;; TEMPLATE

(defn viz-print [env opts data]
  (log env "calculating viz-fn with data: " data)
  {:creator "viz-print"
   :data (pr-str data)
   :viz-opts opts})

(def chart-no-position
  {:viz plot/highstock-ds
   :key :day
   :viz-options {:charts [{:bar {:type :ohlc
                                 :mode :candle}
                           :bollinger-lower {:type :line :color "black"}
                           :bollinger-upper {:type :line :color "black"}
                           ;:entry {:type :flags
                           ;        :fillColor "black"
                           ;        :width 10
                           ;        :height 10
                           ;        :v2style {:long "url(/r/quanta/arrow-up.svg)"
                           ;                  true "url(/r/quanta/arrow-down.svg)" ;"flags
                           ;                  :short "url(/r/quanta/arrow-down.svg)"}}
                           }
                          {:atr {:type :line :color "black"}}
                          {:volume :column}]}})

(def chart
  {:viz plot/highstock-ds
   :key :position
   :viz-options {:charts [{:bar {:type :ohlc
                                 :mode :candle}
                           :bollinger-lower {:type :line :color "black"}
                           :bollinger-upper {:type :line :color "black"}
                           :entry {:type :flags
                                   :fillColor "black"
                                   :width 10
                                   :height 10
                                   :v2style {:long "url(/r/quanta/arrow-up.svg)"
                                             true "url(/r/quanta/arrow-down.svg)" ;"flags
                                             :short "url(/r/quanta/arrow-down.svg)"}}
                           :target-profit {:type :point :color "orange"}
                           :target-loss {:type :point :color "orange"}}
                          {:atr {:type :line :color "black"}}
                          {:volume :column}]}})

(def bollinger-template
  {:id :bollinger
   :md "dev/algo_bollinger.md"
   :algo bollinger-algo
   :options [{:type :select
              :path [:* :asset]
              :name "asset"
              :spec ["BTCUSDT" "ETHUSDT"]}
             {:type :string
              :path [:day :atr-n]
              :name "atr-n"
              :coerce :int}]
   :backtest-new {:viz plot/backtest-ui-ds
                  :viz-options {}
                  :key :backtest}
   :chart-no-position chart-no-position
   :chart chart
   ;; debug
   :no-ui-bars {:viz viz-print
                :viz-options {:print-mode :simple}
                :key :bars-day}
   :no-ui-print {:viz viz-print
                 :viz-options {:print-mode :simple}
                 :key :day}
   :no-ui-backtest {:viz viz-print
                    :viz-options {}
                    :key :backtest}})