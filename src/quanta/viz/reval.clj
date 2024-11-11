(ns quanta.viz.reval
  (:require
   [dali.plot.text :refer [text]]
   [reval.type.protocol :refer [dali-convertable]]
   [tech.v3.dataset.impl.dataset]
   [tech.v3.dataset.impl.column]
   [quanta.dali.plot.fipp :refer [edn-fipp]]))

;; clojure

(extend-type clojure.lang.PersistentVector
  dali-convertable
  (to-dali [v _env]
    (edn-fipp v)))

(extend-type clojure.lang.LazySeq
  dali-convertable
  (to-dali [v _env]
    (edn-fipp v)))

(extend-type clojure.lang.PersistentArrayMap
  dali-convertable
  (to-dali [v _env]
    (edn-fipp v)))

(extend-type clojure.lang.PersistentHashMap
  dali-convertable
  (to-dali [v _env]
    (edn-fipp v)))

(extend-type java.time.Instant
  dali-convertable
  (to-dali [v _env]
    (text {:text v})))

;; techml

(extend-type tech.v3.dataset.impl.column.Column
  dali-convertable
  (to-dali [v _env]
    (text {:text v})))

(extend-type tech.v3.dataset.impl.dataset.Dataset
  dali-convertable
  (to-dali [v _env]
    (text {:text v})))

(defn quanta-default-reval-ui []
  ; this function is called just for the side-effects above.
  (println "adding techml render ui .."))
