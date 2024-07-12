(ns docy.page
  (:require
   [reagent.core :as r]
   [promesa.core :as p]
   [frontend.notification :refer [show-notification]]
   [goldly.service.core :refer [clj]]
   [docy.util :refer [link text]]))

(def namespaces-dict-a (r/atom {}))

(defn ns-seq->dict [ns-seq]
  (->> ns-seq
       (map (fn [{:keys [ns names]}]
              [ns names]))
       (into {})))

(defn ^:export init-docy!
  [new-namespaces]
  (println "init-docy: " new-namespaces)
  (println "init-docy! ns-count: " (count new-namespaces))
  (reset! namespaces-dict-a (ns-seq->dict new-namespaces)))

(defn get-data []
  (let [rp (clj 'docy.core/build-namespaces)]
    (-> rp
        (p/then (fn [ns-seq]
                  (init-docy! ns-seq)))
        (p/catch (fn [err]
                   (println "docy fetch ns-seq  error: " err)
                   (show-notification :error  "docy ns-seq fetch failed!"))))))

; this makes sense because it only needs to be done once.
(get-data)

(defn docy-fun-page [{:keys [route-params] :as route}]
  (fn [{:keys [route-params] :as route}]
    (let [{:keys [nss fun]} route-params
          data (get @namespaces-dict-a nss)]
      [:div
       [:h1.text-xxl.text-blue-800 "Namespace: " (str nss) " Function: " (str fun)]
       [:div (pr-str data)]])))

;{:ns "missionary.core",
; :name "amb",
; :doc "In an `ap` block, evaluates each form sequentially and returns successive results.",
; :file "missionary/core.cljc",
; :arglists ([] [form] [form & forms]),
; :macro true,
; :line 496,
; :column 1}


(defn fun-ui [{:keys [name doc file line column arglists macro] :as fun}]
  [:<>
   ; colum left - function  name
   [:h1.text-xxl.text-blue-800.m-1.p-1
    name]
   ; column right - arglist and docstring
   [:div {:class "bg-gray-300 border-solid border-green-300"
        :style {:max-height "4cm"
                :overflow "auto"}}
    (when arglists
      [:span {:class "text-blue-900 text-bold"}
       (str arglists)])
    (when macro
      [:span {:class "text-red-500 p-1"} "macro"])
    [text doc]]])

(defn docy-ns-page [{:keys [route-params] :as route}]
  (fn [{:keys [route-params] :as route}]
    (let [{:keys [nss]} route-params
          data (get @namespaces-dict-a nss)]
      [:div.p-5
       [:h1.text-xxl.text-blue-800 "Namespace: " (str nss)]
       ;[:div (pr-str data)]
       [into [:div
              {:class "grid gap-1" ;.grid-cols-2.auto-cols-min
               :style {:grid-template-columns "1fr 4fr"
                       :max-width "1200px"}}] (map fun-ui data)]])))

(defn ns-entry [ns-name]
  [:li {:style {:width "100px"
                :min-width "100px"}}
   [link {:to ['docy.page/docy-ns-page :nss (str ns-name)]}
    [:span {:style {:width "100px"
                    :min-width "100px"}
            :class "w-64"
            }
     (str ns-name)]]])

(defn ns-list [ns-symbol-seq]
  (if (seq? ns-symbol-seq)
    (into [:ul {:style {:width "100px"
                        :min-width "100px"}}]

          (map ns-entry ns-symbol-seq))
    [:div "no docs available!"]))

(defn docy-page [_route]
  (fn [{:keys [route-params] :as route}]
    (let [ns-symbol-seq (keys @namespaces-dict-a)]
      [:div.m-3.h-screen.w-screen
       [:h1.text-xxl.text-blue-800.p-2 "Docy Documentation "]
       [ns-list ns-symbol-seq]
       ;[:div (pr-str ns-symbol-seq)]
       ])))
