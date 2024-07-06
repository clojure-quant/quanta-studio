(ns quanta.studio.page.tasks
  (:require
   [reagent.core :as r]
   [re-frame.core :as rf]
   [promesa.core :as p]
   [spaces.core]
   [rtable.rtable]
   [options.core :as o]
   [frontend.notification :refer [show-notification]]
   [ta.viz.lib.ui :refer [link-href]]
   [ta.viz.lib.format-date :refer [dt-yyyymmdd-hhmm]]
   [goldly.service.core :refer [clj]]))

(defn get-tasks [{:keys [tasks-a
                         options-a] :as state}]
  (println "get=tasks options: " @options-a " ... ")
  (let [rp (clj 'quanta.studio/task-summary @options-a)]
    (-> rp
        (p/then (fn [tasks]
                  (println "get-tasks success: " tasks)
                  (reset! tasks-a tasks)))
        (p/catch (fn [err]
                   (println "get-tasks error: " err)
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

(defn make-cols  [state]
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
                                                     (get-tasks state))))}
                       [:i {:class "far fa-stop-circle m-1"}]]
                      [:span.text-blue-500 task-id]]))}
   {:path :start-dt :format dt-yyyymmdd-hhmm :header "start-dt" :max-width "160px"}
   {:path :template-id :format pr-str :header "algo-template"}
   {:path :mode :header "mode"}
   {:path :error? :format error-viz :header "e?"}
   {:path :algo :header "algo-opts" :format pr-str}])

(def table-opts
  {:class "table-head-fixed padding-sm table-blue table-striped table-hover"
   :style {:width "100vw"
           :height "100vh"
           :border "3px solid green"}})

;; OPTIONS

(defn options [state]
  [{:type :select
    :path :mode
    :name "Mode"
    :spec [:* :chart :table :backtest :alert]}
   {:type :select
    :path :error
    :name "Error"
    :spec [:* :only-error :only-valid]}
   {:type :button
    :name "Get Tasks!"
    :class "bg-blue-500 hover:bg-blue-700 text-white font-bold rounded" ; py-2 px-4
    :on-click
    #(get-tasks state)}])

(defn header [{:keys [options-a] :as state}]
  [o/options-ui {:class "bg-blue-300 options-label-left" ; options-debug
                 :style {:width "100%"
                         :height "40px"}}
   {:state options-a
    :options (options state)
    :current {:error :only-valid
              :mode :alert}}])

(defn tasks-ui []
  (let [state {:tasks-a (r/atom [])
               :options-a (atom {:error :only-valid
                                 :mode :alert})}
        cols (make-cols state)]
    (get-tasks state)
    (fn []
      [:div.flex.flex-col.h-full.w-full
       [header state]
       [rtable.rtable/rtable table-opts cols @(:tasks-a state)]])))

(defn tasks-page [_route]
  [tasks-ui])



