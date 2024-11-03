(ns dev.preload
  (:require
   [tick.core :as t]
   [babashka.fs :as fs]
   [missionary.core :as m]
   [clojure.pprint :refer [print-table]]
   [modular.system]
   [quanta.bar.preload :refer [import-bars]]))

(def s (modular.system/system :bar-engine))

s

(def assets ["BTCUSDT"
             "ETHUSDT"])

(m/?
 (import-bars s {:asset assets
                 :calendar [:crypto :d]
                 ;:import :bybit-parallel
                 :import :bybit
                 :to :nippy
                 :window {:start (t/instant "2018-01-01T00:00:00Z")
                          :end (t/instant)}
                 :label "crypto d"}))

(defn import-d [end-dt]
  (import-bars s {:asset assets
                  :calendar [:crypto :d]
                ;:import :bybit-parallel
                  :import :bybit
                  :to :nippy
                  :window {:start (t/instant "2018-01-01T00:00:00Z")
                           :end end-dt}
                  :label "crypto d"}))

(defn import-1h [end-dt]
  (import-bars s {:asset assets
                  :calendar [:crypto :h]
                  :import :bybit-parallel
                  :to :nippy
                  :window {:start (t/instant "2024-01-01T00:00:00Z")
                           :end end-dt}
                  :label "crypto 1h"}))

(defn import-1m [end-dt]
  (import-bars s {:asset assets
                  :calendar [:crypto :m]
                  :import :bybit-parallel
                  :to :nippy
                  :window {:start (t/instant "2024-01-01T00:00:00Z")
                           :end end-dt}
                  :label "crypto 1min"}))

(defn import-5m [end-dt]
  (import-bars s {:asset assets
                  :calendar [:crypto :m5]
                  :bardb :nippy
                  :transform :compress
                  :to :nippy
                  :window {:start (t/instant "2024-01-01T00:00:00Z")
                           :end end-dt}
                  :label "crypto 5min"}))

(defn report [summary label]
  (with-out-str
    (println "summary for: " label)
    (print-table summary)))

(defn import-all-with-report [end-dt]
  (let [d  (import-d end-dt)
        h1  (import-1h end-dt)
        m1  (import-1m end-dt)
        m5  (import-5m end-dt)
        s (str (report d "daily")
               (report m1 "1 minute")
               (report h1 "1 hour")
               (report m5 "5 minute"))
        dir ".data/public/"]
    (fs/create-dirs dir)
    (spit (str dir "preload.txt") s)))

(comment
  (def end-dt (t/instant))
  (import-1h end-dt)
  (import-1m end-dt)
  (import-5m end-dt)

;  
  )

(defn start [& _]
  (println "preloading data..s")
  (let [end-dt (t/instant)]
    (import-all-with-report end-dt)))