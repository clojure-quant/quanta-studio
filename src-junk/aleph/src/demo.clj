(ns demo
  (:require
   [aleph.http :as http]))



(defn show-props [& _ ]
  (println (System/getProperties)))

(System/getProperties)


(defn download-link [& _]
  (let [url "https://www.google.com"
        _ (println  "downloading link: " url)      
        request @(http/get url {:socket-timeout 90000
                                :connection-timeout 90000})]
    (println request)))
