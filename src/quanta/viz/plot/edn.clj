(ns quanta.viz.plot.edn)

(defn edn
  "returns a plot specification {:render-fn :spec :data}.
   The ui shows a edn printout with pr-str
   data-edn must be printable to edn"
  [spec data-edn]
  ^{:render-fn 'quanta.viz.render.core/render-spec} ; needed for notebooks
  {:render-fn 'quanta.viz.render.edn/edn
   :data data-edn
   :spec spec})