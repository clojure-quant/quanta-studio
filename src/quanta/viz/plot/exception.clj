(ns quanta.viz.plot.exception
  (:require
   [clojure.string :as string]
   [clojure.stacktrace :as stack]))

(defn stack-frame
  "Return a map describing the stack frame."
  [^StackTraceElement frame]
  {:name   (str (.getClassName frame) "/" (.getMethodName frame))
   :file   (.getFileName frame)
   :line   (.getLineNumber frame)
   :class  (.getClassName frame)
   :method (.getMethodName frame)})

(defn stacktrace [e]
  (->> e
       .getStackTrace
       (map stack-frame)
       (into [])))

(defn err [e]
  (let [c (class e)]
    {:class (if c (.getName c) "no class")
     :message (.getMessage e)
   ;:stacktrace (stacktrace e)
     }))

(defn exception
  "returns a plot specification {:render-fn :spec :data}. 
   The ui shows the exception."
  [text ex]
  ^{:render-fn 'quanta.viz.render.core/render-spec} ; needed for notebooks
  {:render-fn 'quanta.viz.render.exception/exception
   :data  ;(err (.getCause ex))
   (str text "\r\n"
        (with-out-str
          (stack/print-stack-trace ex)))
   :spec :whatever})

(comment
  (def ex (ex-info "asdf" {:y 3}))
  (class ex)
;; => clojure.lang.ExceptionInfo
  (ex-data ex)
;; => {:y 3}
  (ex-cause ex)
;; => nil
  (ex-message ex)
;; => "asdf"
  ex

  (stack/root-cause ex)
  (stack/print-cause-trace ex)
  (stack/print-stack-trace ex)

  (def ex2 (Exception. "asdf"))
  ex2
  (class ex2)
;; => java.lang.Exception
  (ex-data ex2)
;; => nil
  (ex-cause ex2)
;; => nil
  (ex-message ex2)
;; => "asdf"

  (stack/root-cause ex2)
  (stack/print-cause-trace ex2)
  (stack/print-stack-trace ex2)

;
  )