(ns quanta.viz.render.exception
  (:require
   [clojure.string :as str]))

(defn line-with-br [t]
  [:div
   [:span.font-mono.text-lg.whitespace-pre t]
   [:br]])

(defn text
  "Render text (as string) to html
     works with \\n (newlines)
     Needed because \\n is meaningless in html"
  [t]
  (let [lines (str/split t #"\n")]
    (into [:div {:class "bg-red-200 w-full h-full"
                 :style {:overflow-y "scroll"}}]
          (map line-with-br lines))))

(defn exception [spec data]
  (with-meta
    [text data]
    {:R true}))
