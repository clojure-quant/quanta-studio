(ns quanta.algo.dag.trace
  (:require
   [clojure.string :as str]
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [clojure.stacktrace]
   [tick.core :as t]))

(defn ex->str [ex]
 (str
   "\r\n" "\r\n" "\r\n"
   "ex cause: " (ex-cause ex) "\r\n" "\r\n" "\r\n"
   "ex message: " (ex-message ex) "\r\n" "\r\n" "\r\n"
   "stacktrace: " (with-out-str (clojure.stacktrace/print-stack-trace ex))))

(defn write-text [filename text]
  (spit filename text :append true))

(defn write-ex [filename cell-id ex]
  (write-text filename (str "\r\ncell-id: " cell-id "\r\n"
                            (ex->str ex))))

(defn write-edn [filename cell-id ex]
  (write-text filename (str "\r\ncell-id: " cell-id "\r\n"
                            (pr-str ex))))

(defn setup [path id]
  (let [dt (-> (t/zoned-date-time) (t/in "UTC"))
        dtformat (t/formatter "YYYY-MM-dd-HH-mm-ss")
        filename (str path (t/format dtformat dt) "-" id ".txt")]
    (info "dag " id " logged to: " filename)
    (write-text filename (str "dag id: " id))
    filename
    ))