(ns quanta.viz.plot.hiccup)

(defn hiccup
  "returns a plot specification {:render-fn :spec :data}. 
   plot shows hiccup structure."
  [hiccup]
  ^{:render-fn 'quanta.viz.render.core/render-spec} ; needed for notebooks
  {:render-fn 'quanta.viz.render.hiccup/hiccup
   :data []
   :spec hiccup})