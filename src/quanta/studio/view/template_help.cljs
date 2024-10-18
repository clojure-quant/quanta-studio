(ns quanta.studio.view.template-help
  (:require
   [promesa.core :as p]
   [ui.overlay :refer [overlay-add overlay-remove]]
   [ui.rnd :refer [rnd]]
   [nano-id.core :refer [nano-id]]
   [goldly.service.core :refer [clj]]))

(defn show-floating-help [template-id hiccup]
  (let [id (str (nano-id 5))]
    (overlay-add id [rnd {:bounds "window"
                          :default {:width 600
                                    :height 700
                                    :x 50
                                    :y 60}
                          :style {:display "flex"
                                  ;:alignItems "center"
                                  :justifyContent "center"
                                  :border "solid 2px #ddd"
                                  :background "#f0f0f0"}}
                     [:div.bg-green-500.p-5.w-full.h-full.prose
                      [:link {:href "/r/quanta/prose.css"
                              :rel "stylesheet"
                              :type "text/css"}]
                      [:div "template: " template-id
                       [:span.bg-green-700.m-5 {:on-click #(overlay-remove id)} "X"]
                       [:br]]
                      hiccup]])))

(defn show-help
  [template-id]
  (println "loading help for template-id: " template-id)
  (-> (clj {:timeout 2000} 'quanta.studio.template.db/template-help template-id)
      (p/then (fn [help-hiccup]
                (println "help-hiccup loaded: " help-hiccup)
                (show-floating-help template-id help-hiccup)))
      (p/catch (fn [err]
                 (println "could not load template-help " template-id " error: " err)))))
