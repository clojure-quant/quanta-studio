(ns quanta.studio.page.tasks
  (:require
   [reagent.core :as r]
   [re-frame.core :as rf]
   [promesa.core :as p]
   [spaces.core]
   [rtable.rtable]
   [frontend.notification :refer [show-notification]]
   [ta.viz.lib.ui :refer [link-href]]
   [ta.viz.lib.format-date :refer [dt-yyyymmdd-hhmm]]
   [goldly.service.core :refer [clj]]))

(defn get-tasks [a]
  (let [rp (clj 'quanta.studio/task-summary)]
    (-> rp
        (p/then (fn [tasks]
                  (println "successfully received task summary: " tasks)
                  (reset! a tasks)))
        (p/catch (fn [err]
                   (println "subscription error: " err)
                   (show-notification :error  "could not get tasks summary!"))))

    nil))

(defn stop-task [task-id]
  (let [rp (clj 'quanta.studio/stop task-id)]
    (-> rp
        (p/then (fn [_r]
                  (println "successfully stopped task: " task-id)
                  (show-notification :info  (str "stopped algo-viz-task " task-id))))
        (p/catch (fn [err]
                   (println "error in stopping task " task-id " error: " err)
                   (show-notification :error  (str "could not stop task " task-id)))))

    rp))

(defn error-viz [error?]
  (if error?
    [:i {:class "fas fa-exclamation-circle m-1 text-red-400"}]
    [:i {:class "far fa-check-circle m-1 text-green-700"}]))

(defn make-cols  [tasks-a]
  [{:path :task-id :header "task2"
    :render-cell (fn [col-info row]
                   (let [task-id (:task-id row)]
                     [:div
                      [:a {:href (str "/interact/" task-id)
                           :on-click #(rf/dispatch [:bidi/goto 'quanta.studio.page.algo/algo-task-page
                                                    :task-id task-id
                                                     ;:query-params {:expected-guests 299}
                                                    ])}
; font awesome v5
                       [:i {:class "fas fa-eye m-1"}]]
                      [:a {:on-click #(let [sp (stop-task task-id)]
                                        (p/then sp (fn [r]
                                                     (get-tasks tasks-a))))}
                       [:i {:class "far fa-stop-circle m-1"}]]
                      [:span.text-blue-500 task-id]]))}
   {:path :start-dt :format dt-yyyymmdd-hhmm :header "start-dt" :max-width "160px"}
   {:path :template-id :format pr-str :header "algo-template"}
   {:path :error? :format error-viz :header "e?"}
   {:path :algo :header "algo-opts" :format pr-str}])

(def opts
  {:class "table-head-fixed padding-sm table-blue table-striped table-hover"
   :style {:width "100vw"
           :height "100vh"
           :border "3px solid green"}})

(defn tasks-ui []
  (let [tasks-a (r/atom [])
        cols (make-cols tasks-a)]
    (get-tasks tasks-a)
    (fn []
      [rtable.rtable/rtable opts cols @tasks-a])))

(defn tasks-page [_route]
  [tasks-ui])



