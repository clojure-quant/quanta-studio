(ns quanta.studio.page.bruteforce
  (:require
   [reagent.core :as r]
   [promesa.core :as p]
   [spaces.core]
   [rtable.rtable]
   [options.core :as o]
   [frontend.notification :refer [show-notification]]
   [goldly.service.core :refer [clj]]))

(defn get-labels [labels-a]
  (let [rp (clj 'quanta.studio.bruteforce/show-available)]
    (-> rp
        (p/then (fn [labels]
                  (println "get-labels success: " labels)
                  (reset! labels-a (into [] labels))))
        (p/catch (fn [err]
                   (println "get-labels error: " err)
                   (show-notification :error  "could not get labels summary!"))))

    nil))

(def table-opts
  {:class "table-head-fixed padding-sm table-blue table-striped table-hover"
   :style {:width "100vw"
           :height "100vh"
           :border "3px solid green"}})

(defn header [{:keys [options-a labels-a] :as state}]
  [o/options-ui {:class "bg-blue-300 options-label-left" ; options-debug
                 :style {:width "100%"
                         :height "40px"}}
   {:state options-a
    :options  [{:type :select
               :path :label
               :name "Label"
               :spec @labels-a}]
    :current {:label nil}}])

(defn bruteforce-ui []
  (let [state {:labels-a (r/atom [])
               :options-a (atom {:label nil})}]
    (get-labels (:labels-a state))
    (fn []
      [:div.flex.flex-col.h-full.w-full
       [header state]
       ;[rtable.rtable/rtable table-opts cols @(:tasks-a state)]
       
       ])))

(defn page [_route]
  [bruteforce-ui])



