(ns dev.bollinger
  (:require
   [tablecloth.api :as tc]
   [ta.indicator.band :as band]
   [quanta.dag.env.bars :refer [get-trailing-bars]]
   [quanta.dag.algo.spec :as spec]))

(def ds1
  (tc/dataset {:close [100.0 101.0 103.0 102.0 104.0 105.0]}))

(band/add-bollinger {:n 2 :k 3.0} ds1)

(defn bollinger-calc [opts time]
  (->> time
       (get-trailing-bars opts)
       (band/add-bollinger {:n 2 :k 3.0})))

(defn bollinger-signal [opts d m]
  (vector d m))

(def bollinger-algo
  [{:asset "USD/JPY"}
   :day {:calendar [:forex :d]
         :type :trailing-bar
         :algo  bollinger-calc
         :bardb :nippy
                 ; daily opts
         :trailing-n 2000
                 ; atr-band
         :atr-n 10
         :atr-m 0.6}
   :min {:calendar [:forex :m]
         :type :trailing-bar
         :algo bollinger-calc
         :trailing-n 2000}
   :signal {:formula [:day :min]
            :algo bollinger-signal
            :carry-n 2}])

(spec/spec->ops bollinger-algo)