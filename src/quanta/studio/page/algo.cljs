(ns quanta.studio.page.algo
  (:require
   [promesa.core :as p]
   [spaces.core]
   [options.edit :as edit]
   [ta.viz.lib.ui :refer [link-href link-dispatch]]
   [quanta.studio.view.template-help :refer [show-help]]
   [quanta.studio.view.state :as s]
   [quanta.studio.view.result :refer [result-view]]
   [quanta.studio.view.options :refer [options-ui]]))

;; header

(defn keyword->spec [kw]
  {:id  kw
   :name (str kw)})

(defn keywords->spec [kws]
  (map keyword->spec kws))

(defn template-spec [template-list]
  (let [template-spec  (keywords->spec template-list)]
    (into []
          (concat [{:id :no-algo :name ""}]
                  template-spec))))

(defn header-ui [state]
  (s/get-available-templates state)
  (let [template-a (s/get-view-a state :template)
        template-list-a (s/get-view-a state :template-list)]
    (fn [state]
      [:div.w-full.h-full.bg-blue-300
       [link-dispatch [:bidi/goto 'quanta.studio.page.main/main-page] "studio"]
       ;[link-href "/" "main"]
       ; template selector
       ;[:p "template-selector" (pr-str @template-list-a)]
       [edit/select
        {:set-fn (fn [template-id]
                   (s/set-state state :current {})
                   (s/set-state state :options {})
                   (s/set-state state :template template-id)
                   (s/get-template-options state template-id))
         :options {:spec (template-spec @template-list-a)
                   :class "bg-green-500"}}
        @template-a]
       [:span {:on-click #(show-help @(:template state))
               :class "bg-red-300 p-1 m-1 border-round"} "?"]
       ; options for selected template
       [options-ui state]])))

(defn algo-ui [state]
  (fn [state]
    [spaces.core/viewport
     [spaces.core/top-resizeable {:size 50}
        ;[:div.bg-gray-200.w-full.h-full "top"]
      [header-ui state]]
     [spaces.core/fill
        ;[:div.bg-red-200.w-full.h-full "main"]
      [result-view state]]]))

(defn algo-page [_route]
  (let [state (s/create-state)]
    [algo-ui state]))

(defn algo-task-page [{:keys [route-params] :as route}]
  (let [{:keys [task-id]} route-params
        state (s/create-state)
        _ (println "algo-task-page task-id: " task-id)
        rp (s/view-task state task-id)]
    (p/then rp (fn [result]
                 (println "task result XXX: " result)))

    [algo-ui state]))

