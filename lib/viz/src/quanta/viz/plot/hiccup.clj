(ns quanta.viz.plot.hiccup)

(defn hiccup
  "returns a plot specification {:render-fn :spec :data}. 
   plot shows hiccup structure."
  [hiccup]
  ^{:render-fn 'ta.viz.renderfn/render-spec} ; needed for notebooks
  {:render-fn 'ta.viz.renderfn.hiccup/hiccup
   :data []
   :spec hiccup})