(ns quanta.dag.algo.spec-options
  (:require 
    [taoensso.timbre :as timbre :refer [debug info warn error]]
    [com.rpl.specter :as specter]))

(defn apply-options
  "returns algo with options applied 
   options is a map, keys are paths (vectors)
   and values are the values to set."
  [algo options]
  ; if all paths are keys, this is really simple.
  ; (update template :algo merge options)
  ; but if we can have hierarchical paths, then we 
  ; need to set them via specter, so type gets
  ; preserved. 
   (try (reduce
                (fn [r [path v]]
                  (let [path (if (keyword? path)
                               [path]
                               path)]
                    (debug "setting path: " path " to val: " v)
                    (specter/setval path v r)))
                algo
                options)
        (catch Exception ex
          (error "algo-apply-options"
                 {:algo algo
                  :options options}
                  ex)
          (throw ex))))