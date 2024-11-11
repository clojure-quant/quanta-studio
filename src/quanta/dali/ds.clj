(ns quanta.dali.ds
  (:require
   [tablecloth.api :as tc]
   [tech.v3.io :as io])
  (:import (java.io ByteArrayOutputStream ByteArrayInputStream)))

(defn sanitize-ds [ds]
  (let [s (ByteArrayOutputStream.)]
    (io/put-nippy! s ds)
    (let [bb (.toByteArray s)
          s (ByteArrayInputStream. bb)]
      (io/get-nippy s))))

(comment
  (def d (tc/dataset {:a [1 2 34]}))
  d
   ;; => _unnamed [3 1]:
   ;;    
   ;;    | :a |
   ;;    |---:|
   ;;    |  1 |
   ;;    |  2 |
   ;;    | 34 |
  (sanitize-ds d)

;  
  )

