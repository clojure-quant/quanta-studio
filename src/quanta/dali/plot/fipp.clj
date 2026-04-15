(ns quanta.dali.plot.fipp
  (:require
   [dali.spec :refer [create-dali-spec]]
   [ednx.fipp :refer [pprint]]))

(defn edn-fipp [v]
  (let [edn-str (pprint v)]
    (create-dali-spec
     {:viewer-fn 'dali.viewer.text/text
      :data edn-str})))

