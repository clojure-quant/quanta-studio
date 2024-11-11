(ns dev.preload
  (:require
   [tick.core :as t]
   [babashka.fs :as fs]
   [missionary.core :as m]
   [clojure.pprint :refer [print-table]]
   [modular.system]
   [quanta.bar.preload :refer [import-bars]]))

(def assets ["BTCUSDT"
             "ETHUSDT"])

(defn import-d [bar-engine end-dt]
  (import-bars bar-engine {:asset assets
                           :calendar [:crypto :d]
                ;:import :bybit-parallel
                           :import :bybit
                           :to :nippy
                           :window {:start (t/instant "2018-01-01T00:00:00Z")
                                    :end end-dt}
                           :label "crypto d"}))

(defn append-import-1d [bar-engine end-dt]
  (import-bars bar-engine
               {:asset assets
                :calendar [:crypto :d]
                :bardb :nippy
                :import :bybit-parallel
                :to :nippy
                :transform :append-only
                :window {:start (t/instant "2018-01-01T00:00:00Z")
                         :end end-dt}
                :parallel-nr 16 ; 16 cores.
                :label "crypto 1d"}))

(defn import-1h [bar-engine end-dt]
  (import-bars bar-engine {:asset assets
                           :calendar [:crypto :h]
                           :import :bybit-parallel
                           :to :nippy
                           :window {:start (t/instant "2024-01-01T00:00:00Z")
                                    :end end-dt}
                           :label "crypto 1h"}))

(defn append-import-1h [bar-engine end-dt]
  (import-bars bar-engine
               {:asset assets
                :calendar [:crypto :h]
                :bardb :nippy
                :import :bybit-parallel
                :to :nippy
                :transform :append-only
                :window {:start (t/instant "2022-01-01T00:00:00Z")
                         :end end-dt}
                :parallel-nr 16 ; 16 cores.
                :label "crypto 1min"}))

(defn import-1m [bar-engine end-dt]
  (import-bars bar-engine {:asset assets
                           :calendar [:crypto :m]
                           :import :bybit-parallel
                           :to :nippy
                           :window {:start (t/instant "2024-01-01T00:00:00Z")
                                    :end end-dt}
                           :label "crypto 1min"}))

(defn append-import-1m [bar-engine end-dt]
  (import-bars bar-engine
               {:asset assets
                :calendar [:crypto :m]
                :bardb :nippy
                :import :bybit-parallel
                :to :nippy
                :transform :append-only
                :window {:start (t/instant "2022-01-01T00:00:00Z")
                         :end end-dt}
                :parallel-nr 16 ; 16 cores.
                :label "crypto 1min"}))

(defn import-5m [bar-engine end-dt]
  (import-bars bar-engine {:asset assets
                           :calendar [:crypto :m5]
                           :bardb :nippy
                           :transform :compress
                           :to :nippy
                           :window {:start (t/instant "2024-01-01T00:00:00Z")
                                    :end end-dt}
                           :label "crypto 5min"}))

(defn append-compressed-bars [bar-engine end-dt timeframe]
  (import-bars bar-engine
               {:asset assets
                :calendar [:crypto timeframe]
                :bardb :nippy
                :import :bybit-parallel
                :transform :append-only
                :to :nippy
                :window {:start (t/instant "2024-01-01T00:00:00Z")
                         :end end-dt}
                :parallel-nr 16 ; 16 cores.
                :label (str "crypto " timeframe)}))

(defn report [summary label]
  (with-out-str
    (println "summary for: " label)
    (print-table summary)))

(defn import-all-with-report [bar-engine end-dt]
  (m/sp
   (let [d  (m/? (append-import-1d bar-engine end-dt))
         h1  (m/? (append-import-1h bar-engine end-dt))
         m1  (m/? (append-import-1m bar-engine end-dt))
         m5  (m/? (import-5m bar-engine end-dt))
         s (str (report d "daily")
                (report m1 "1 minute")
                (report h1 "1 hour")
                (report m5 "5 minute"))
         dir ".data/public/"]
     (fs/create-dirs dir)
     (spit (str dir "preload.txt") s))))

(comment
  (def end-dt (t/instant))
  (def bar-engine (modular.system/system :bar-engine))
  (m/? (import-1h bar-engine end-dt))
  (m/? (import-1m bar-engine end-dt))
  (m/? (import-5m bar-engine end-dt))
  (m/? (append-compressed-bars bar-engine end-dt :m5))
;  
  )

(defn start [& _]
  (println "preloading data..s")
  (let [end-dt (t/instant)
        bar-engine (modular.system/system :bar-engine)]
    (m/? (import-all-with-report bar-engine end-dt))))

