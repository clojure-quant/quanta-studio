(ns quanta.dali.ds
  (:require
   [taoensso.telemere :as tm]
   [tablecloth.api :as tc]
   [tech.v3.io :as io]))

(defn save-ds [filename ds]
  (let [s (io/gzip-output-stream! filename)]
    (io/put-nippy! s ds)))

(defn load-ds [filename]
  (let [s (io/gzip-input-stream filename)
        ds (io/get-nippy s)]
    ds))

(defn sanitize-ds [ds]
  (tm/log! (str "sanitizing ds: " ds))
  (let [filename "/tmp/ds-safe.nippy.gz"]
    (save-ds filename ds)
    (load-ds filename)))

;  java.lang.RuntimeException: java.lang.Exception: Not supported: class tech.v3.datatype.unary_op$eval13921$fn$reify__13965
