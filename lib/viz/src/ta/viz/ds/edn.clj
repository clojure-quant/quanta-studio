(ns ta.viz.ds.edn)

(defn edn-render-spec
  "returns a render specification {:render-fn :spec :data}. 
   spec must follow chart-pane format.
   The ui shows a edn printout with pr-str"
  [spec data-edn]
  ^{:render-fn 'ta.viz.renderfn/render-spec} ; needed for notebooks
  {:render-fn 'ta.viz.renderfn.edn/edn
   :data data-edn
   :spec spec})