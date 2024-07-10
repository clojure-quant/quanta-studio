(ns docy.util
  (:require
   [clojure.string :as str]
   [re-frame.core :as rf]))

;; links

(defn link [{:keys [to class style] :as opts
             :or {class "bg-blue-600 cursor-pointer hover:bg-red-700 m-1"
                  style {}}} & body]
  (let [v (->> (concat [:bidi/goto] to)
               (into []))
        goto-link (fn [& _]
                    (println "going to: " v)
                    (rf/dispatch v))]
    (println "link: " v)
    (into [:a {:class class
               :style style
               :on-click goto-link}] body)))
  

(defn line-with-br [t]
  [:div
   [:span.font-mono.text-xs.whitespace-pre t]
   [:br]])

(defn text
  "Render text (as string) to html
   works with \\n (newlines)
   Needed because \\n is meaningless in html"
  [t]
  (let [lines (str/split t #"\n")]
    (into [:div ] (map line-with-br lines))))

