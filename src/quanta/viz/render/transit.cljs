(ns quanta.viz.render.transit
  (:require
   [promesa.core :as p]
   [reagent.core :as r]
   [tech.v3.dataset :as tmlds]
   [cquant.tmlds :refer [GET]]))

(defn load-dataset [url]
  (println "loading dataset from url: " url)
  (let [load-promise (GET url)]
    (-> load-promise
        (p/then (fn [ds]
                  (println "ds from url " url " loaded successfully. rows: " (tmlds/row-count ds) "cols: " (tmlds/column-names ds))
                  ds))
        (p/catch (fn [err]
                   (println "could not load ds from url " url " err: " err))))
    ; give back the original promise
    load-promise))

(defn loading-ui [opts data render-fn]
  (let [url (:url data)
        a (r/atom nil)]
    (-> (load-dataset url)
        (p/then (fn [ds]
                  (println "load and transform complete!")
                  (reset! a ds)))
        (p/catch (fn [err]
                   (println "load   error: " err)
                   (reset! a :load-error))))
    (fn [opts data render-fn]
      (cond
        (nil? @a)
        [:p "nil"]
        (= @a :load-error)
        [:p "load error!"]
        :else
        [render-fn opts @a]))))