(ns quanta.studio.layout.core
  (:require
   [modular.fipp :refer [pprint-str]]))

(defn save-layout [{:keys [layout-dir]} layout-name layout-data]
  (if layout-dir
    (spit (str layout-dir layout-name ".edn") (pprint-str layout-data))
    (println "not saving layout " layout-name " - no layout-dir defined.")))