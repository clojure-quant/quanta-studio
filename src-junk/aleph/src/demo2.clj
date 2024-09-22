(ns demo2
  (:require
   [clojure.pprint :refer [print-table]]))

(defn show-props [& _]
  (let [p (System/getProperties)]
    (println p)

    (println "-------------------------")
    (println "keys: " (keys p))

    (->> ;(dissoc p ) 
     p
     (map (fn [[k v]] {:name (str k) :val v}))
     (remove (fn [{:keys [name val]}]
               (if (= name "java.class.path")
                 (do (println "java class path: " val)
                     true)
                 false)))
     (print-table))))

