(ns quanta.studio.backtest.config-manager
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]))

(def config-dir "./algo-configs")

(defn ensure-directory [dir-path]
  (let [dir (io/file dir-path)]
    (when-not (.exists dir)
      (.mkdirs dir))))

(defn read-config [file-name]
  (-> (io/file config-dir file-name)
      (.getPath)
      (slurp)
      (edn/read-string)))

(defn write-config [template-id options]
  (let [file-name (str (random-uuid) "-test.edn")
        file (io/file config-dir file-name)
        data {:template-id template-id
              :options options
              ;:backtest-result {}
              }]
    (ensure-directory config-dir)
    (spit file (pr-str data))))

(defn load-edn-files []
  (let [dir (io/file config-dir)]
    (filter (fn [f]
              (and (.isFile f)
                   (.endsWith (.getName f) ".edn")))
            (file-seq dir))))

(defn load-files-by
  [{:keys [template-id]}]
  (let [edn-files (load-edn-files)]
    (filter (fn [f]
              (let [c (read-config (.getName f))]
                (= (:template-id c) template-id))) edn-files)))

(comment
  (write-config :alex/bollinger2 {:asset "BTCUSDT"
                                  :source :nippy
                                  :calendar [:crypto :m]
                                  :trailing-n 200000
                                  :n 120
                                  :k1 1.0
                                  :k2 2.0
                                  :min-above-bars 6
                                  :high-forward-bars 30 ; high-carry-forward-bars
                                  :entry [:fixed-amount 10000]
                                  :exit [:time 60
                                         :loss-percent 0.5
                                         :profit-percent 0.5]})

  (read-config "test.edn")

  (map str (load-files-by :x))

  (load-files-by {:template-id :alex/bollinger}))