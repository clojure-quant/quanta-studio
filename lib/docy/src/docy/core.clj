(ns docy.core
  (:require
   [orchard.info :as orchard]
   [taoensso.timbre :refer [info warn error]]
   [extension :as ext]
   [clj-service.core :refer [expose-functions]]))

(defn docstring [symbol]
  (:doc (meta (resolve symbol))))

(defn describe-fun [nss fun]
  (let [data (orchard/info nss fun)]
    {:ns (str (:ns data))
     :name (str (:name data))
     :doc (:doc data)
     :file (:file data)
     ; :arglists ([[calendar-kw interval-kw]]),
     :arglists (:arglists data)
     :line (:line data)
     :column (:column data)}))

(defn describe-ns [nss]
  (require [nss])
  (let [symbols (keys (ns-publics nss))]
    {:ns (str nss)
     :names (->> symbols
                 (map #(describe-fun nss %))
                 (filter :doc)
                 (sort-by :name)
                 )}))

(defn build-namespaces [ns-symbol-seq]
  (info "building namespaces: " (count ns-symbol-seq))
  (let [r (->> (map describe-ns ns-symbol-seq)
               (sort-by :ns)
               ;(take 1)
               (into []))]
    ;(info "result: " r)
    r))

(defn start-docy [{:keys [exts clj role namespaces]}]
  (info "starting docy .. ")
  (assert (vector? namespaces))
  (info "starting docy namespaces: " (count namespaces))
    ;(add-discovered-namespaces this exts)
  (if clj
    (do
      (info "starting docy clj-services..")
      (expose-functions clj
                        {:name "docy"
                         :symbols ['docy.core/build-namespaces]
                         :permission role
                         :fixed-args [namespaces]}))
    (warn "docy starting without clj-services, perhaps you want to pass :clj key"))
  (info "docy running!")
  namespaces)

(comment
  (describe-ns 'ta.calendar.core)

  (describe-ns 'ta.indicator.band)
  

  (require '[modular.system])

  (def d (modular.system/system :docy))

  d

  (build-namespaces d))