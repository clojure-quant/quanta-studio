(ns quanta.viz.render.rtable
  (:require
   [reagent.core :as r]
   [promesa.core :as p]
   [rtable.rtable]
   [quanta.viz.render.rtable-resolve :refer [resolve-cols]]))

(defn resolve-columns [cols]
  (println "rtable-resolve-columns ..")
  (let [cols-a (r/atom nil)
        cols-p (resolve-cols cols)]
    (-> cols-p
        (p/then (fn [cols]
                  (println "rtable-resolve-columns done. cols: " (pr-str cols))
                  (reset! cols-a cols)))
        (p/catch (fn [err]
                   (println "rtable-resolve-columns error:  " err)
                   (reset! cols-a :error))))
    cols-a))

(defn rtable [spec data]
  ; rtable needs 3 parameters: opts, cols, data
  ; our spec format only uses two parameters; we moved the 
  ; columns definition to a :columns key in opts
  (let [opts spec ; we could dissoc :columns here, but why?
        cols (:columns spec)
        cols-a (resolve-columns cols)]
    (fn [spec data]
      (with-meta
        (if (empty? data)
          [:div.h-full.w-full.p-10 "No Rows in this table. "]
          (if @cols-a
            (if (= :error @cols-a)
              [:div.h-full.w-full.p-10 "Columns resolve error!"]
              [rtable.rtable/rtable opts @cols-a data])
            [:div.h-full.w-full.p-10 "Resolving Column Functions.."]))
        {:R true}))))

(defn rtable-simple [spec data]
  ; rtable needs 3 parameters: opts, cols, data
  ; our spec format only uses two parameters; we moved the 
  ; columns definition to a :columns key in opts
  (let [opts spec ; we could dissoc :columns here, but why?
        cols (:columns spec)]
    (with-meta
      (if (empty? data)
        [:div.h-full.w-full.p-10 "No Rows in this table. "]
        [rtable.rtable/rtable opts cols data])
      {:R true})))