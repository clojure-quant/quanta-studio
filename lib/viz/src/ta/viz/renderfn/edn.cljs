(ns ta.viz.renderfn.edn)

(defn edn [_spec data]
  (with-meta
    [:div {:class "w-full.h-full"}
     (pr-str data)]
    {:R true}))
