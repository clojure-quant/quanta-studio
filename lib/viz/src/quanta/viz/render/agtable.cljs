(ns quanta.viz.render.agtable
  (:require
   [re-frame.core :as rf]
   [rtable.tmlds.aggrid :as ag-tml]))

;(rf/dispatch [:css/set-theme-component :aggrid "material"])
;(rf/dispatch [:css/set-theme-component :aggrid "alpine"])
;(rf/dispatch [:css/set-theme-component :aggrid "balham-dark"])

(rf/dispatch [:css/set-theme-component :aggrid true]) ; default

(defn agtable [opts data]
  ; agtable needs 3 parameters: opts, cols, data
  ; our spec format only uses two parameters; we moved the 
  ; columns definition to a :columns key in opts
  (let [cols (:columns opts)
        style (or (:style opts)
                  {;:width "800px" :height "600px"
                   :width "100%" :height "100%"})]
    (with-meta
      (if (empty? data)
        [:div.h-full.w-full.p-10 "No Rows in this table. "]
        [ag-tml/aggrid-url {:style style :columns cols} (:url data)])
      {:R true})))