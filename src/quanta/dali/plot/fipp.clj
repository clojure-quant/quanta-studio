(ns quanta.dali.plot.fipp
  (:require
   [dali.spec :refer [create-dali-spec]]
   [modular.persist.edn :refer [pprint-str]]))

(defn edn-fipp [v]
  (let [edn-str (pprint-str v)]
    (create-dali-spec
     {:viewer-fn 'dali.viewer.text/text
      :data edn-str})))

