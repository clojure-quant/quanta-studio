(ns quanta.studio.page.preload
  (:require
   [reagent.core :as r]
   [promesa.core :as p]
   [ajax.promise :refer [GET]]
   [dali.viewer.text :refer [text]]
   [quanta.studio.lib.link :refer [link-dispatch link-href]]))

(defn preload-text []
  (let [text-a (r/atom "")
        get-p (GET "/r/preload.txt")]
    (-> get-p
        (p/then (fn [txt]
                  (reset! text-a txt)))
        (p/catch (fn [err]
                   (reset! text-a "lo preload report found."))))
    (fn []
      [text {:text @text-a}])))

(defn preload-page [{:keys [_route-params _query-params _handler] :as _route}]
  [:div
   ; quanta studio main page
   [:div.bg-blue-300.m-5
    [:h1.text-xl.text-red-600 "quanta studio - preload "]
    [preload-text]
    [link-dispatch [:bidi/goto 'quanta.studio.page.main/main-page] "main"]]

;
   ])







