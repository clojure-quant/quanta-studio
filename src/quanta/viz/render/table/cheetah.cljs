(ns quanta.viz.render.table.cheetah
  (:require
   [rtable.render.cheetah :refer [cheetah-ds]]))

(defn cheetah-table [opts data]
  (let [cols (:columns opts)
        style (or (:style opts)
                  {;:width "800px" :height "600px"
                   :width "100%" :height "100%"})]
    (with-meta
      (if (empty? data)
        [:div.h-full.w-full.p-10 "No Rows in this table. "]
        [cheetah-ds {:style style :columns cols
                     :url (:url data)}])
      {:R true})))