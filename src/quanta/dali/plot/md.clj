(ns quanta.dali.plot.md
  (:require
   [taoensso.timbre :as log :refer [tracef debug debugf info infof warn error errorf]]
   [clojure.java.io]
   [commonmark-hiccup.core :refer [markdown->hiccup]]
   [dali.spec :refer [create-dali-spec]]))

(defn load-from-resource [filename]
  (let  [file (-> filename
                  clojure.java.io/resource
                  .getFile)]
    (slurp file)))

(defn md [resource-filename]
  (let [hiccup (markdown->hiccup (load-from-resource resource-filename))]
    (create-dali-spec
     {:viewer-fn 'dali.viewer.hiccup/hiccup
      :data [:div.bg-green-200.p-5.w-full.h-full.prose
             [:link {:href "/r/quanta/prose.css"
                     :rel "stylesheet"
                     :type "text/css"}]
             (into [:div] hiccup)]})))

(comment
  (md "docy/quanta-studio-layout.md")

; 
  )
