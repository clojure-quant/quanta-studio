(ns quanta.studio.view.state
  (:require
   [reagent.core :as r]
   [promesa.core :as p]
   [re-frame.core :as rf]
   [nano-id.core :refer [nano-id]]
   [frontend.notification :refer [show-notification]]
   [goldly.service.core :refer [clj]]))

(defn get-result-a [state]
  (get state :result))

(defn set-result [state result]
  (reset! (get state :result) result))

(defn start-listening! [state]
  (rf/reg-event-db
   :interact/subscription
   (fn [db [_ {:keys [task-id result]}]]
     (let [target-task-id @(:task-id state)]
       ; only store if we currently want to see it.
       (if (= target-task-id task-id)
         (do (println "received subscription result for task: " task-id "result :" result)
             (set-result state result))
         (println
          (println "received task result that is not subscribed task " task-id
                   " target-task-id " target-task-id " result: " result)))
       db))))

;; state management

(defn create-state []
  {:template-list (r/atom [])
   :template (r/atom nil)
   :options (r/atom {})
   :current (r/atom {})
   :views (r/atom [])
   :task-id (r/atom nil)
   :result (r/atom nil)})

(defn set-state [state k v]
  ;(println "setting state k: " k "val: " v)
  (reset! (k state) v))

(defn get-view-a [state k]
  (get state k))

(defn clj-state-k
  "executes fun in clj.
   on success sets k in state.
   on error notifies ui."
  [state k fun & args]
  (println "loading clj fun: " fun " args: " args)
  (let [rp (apply clj fun args)]
    (-> rp
        (p/then (fn [r]
                  (set-state state k r)))
        (p/catch (fn [_r]
                   (show-notification :error (str "data load error:"  fun args)))))
    rp))

(defn get-available-templates [state]
  (clj-state-k state :template-list 'quanta.template.db/available-templates)
  nil)

(defn get-template-options [state template-id]
  (let [rp (clj-state-k state :options 'quanta.studio/get-options template-id)]
    (p/then rp (fn [r]
                 (println "current template-options: " (:current r))
                 (println "current template-views: " (:views r))
                 (set-state state :current (:current r))
                 (set-state state :views (:views r))))
    nil))

(defn stop [state]
  (when-let [task-id @(:task-id state)]
    (println "stopping task-id: " task-id)
    (clj 'quanta.studio/stop task-id)))

(defn get-mode [state]
  (or (get @(:current state) :mode)
      :chart))

(defn start [state]
  ;(unsubscribe state)
  (let [task-id (nano-id 6)]
    (set-state state :task-id task-id)
    (start-listening! state) ; result gets pushed, so it might be that the subscription request
    ; comes back after the request comes back.
    (let [rp (clj 'quanta.studio/start
                  @(:template state)
                  @(:current state)
                  (get-mode state)
                  task-id)]
      (-> rp
          (p/then (fn [task-id]
                    (println "successfully started task-id: " task-id)))
          (p/catch (fn [err]
                     (println "start task error: " err)
                     (show-notification :error  "start task failed!"))))
      nil)))

(defn calculate [state]
  ;(unsubscribe state)
  (clj-state-k state :result 'quanta.studio/calculate
               @(:template state)
               @(:current state)
               (get-mode state))
  nil)

(defn get-current-task-result [state task-id]
  (clj-state-k state :result 'quanta.studio/current-task-result task-id))

(defn view-task [state task-id]
  (set-state state :task-id task-id)
  (get-current-task-result state task-id))

