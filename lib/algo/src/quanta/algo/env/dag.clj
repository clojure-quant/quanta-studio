(ns quanta.algo.env.dag
  (:require
    [quanta.algo.dag.trace :as trace]))

(def ^:dynamic *dag* nil)

(defn opts []
  @(:opts *dag*))

(defn log [label v]
  (trace/write-edn-raw(:logger *dag*) label v))

 
