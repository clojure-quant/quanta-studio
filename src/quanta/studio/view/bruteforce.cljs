(ns quanta.studio.view.bruteforce
  (:require
   [reagent.core :as r]
   [promesa.core :as p]
   [rtable.rtable]
   [cquant.tmlds :refer [GET]]
   [quanta.viz.render.trade.core :refer [roundtrip-stats-ui]]))

;; bruteforce roundtrip-ui

(def stats-opts
  {:intraday? false
   :style {:height "600px"
           :width "800px"}
   :class "bg-red-500"})

(defn load-backtest [label id backtest-a]
  (let [url (str "/r/bruteforce/" label "/" id "-backtest.transit-json")
        _   (println "loading backtest from url: " url)
        load-promise (GET url)]
    (-> load-promise
        (p/then (fn [ds]
                  (println "ds from url " url " loaded successfully.")
                  (println ds)
                  (reset! backtest-a ds)
                  ds))
        (p/catch (fn [err]
                   (println "could not load ds from url " url " err: " err)
                   (reset! backtest-a nil))))
    nil))

(defn bruteforce-roundtrips [label id]
  (let [loaded-a (r/atom [nil nil])
        backtest-a (r/atom nil)]
    (fn [label id]
        (when (and label id
                   (not (= [label id] @loaded-a)))
          (println "loading backtest label: " label " id:  " id)
          (load-backtest label id backtest-a)
          (reset! loaded-a [label id])  
          nil)
        (if @backtest-a
          [roundtrip-stats-ui stats-opts @backtest-a]
          [:div "label:" label  " id: " id]))))

;; bruteforce label results

(def table-opts
  {:class "table-head-fixed padding-sm table-blue table-striped table-hover"
   :style {:width "100%"
           :height "100%"
           :border "3px solid green"}})

(defn make-render-id  [set-id]
  (fn [col-info row]
    (let [id (:id row)]
      [:div
       [:a {:on-click #(set-id id)}
  ; font awesome v5
        [:i {:class "fas fa-eye m-1"}]]
       [:a {;:on-click 
            }
        [:i {:class "far fa-stop-circle m-1"}]]
       [:span.text-blue-500 id]])))

(defn make-cols  [variations set-id]
  ; :variations [[0 :asset] ["BTCUSDT" "ETHUSDT"] 
  ;              [2 :day :atr-n] [20 50]]
  [{:path :id :header "id" :render-cell (make-render-id set-id)}
   ; [0 :asset] "BTCUSDT",
   ; [2 :day :atr-n] 50,
   {:path :target}
   {:path :trades}
   {:path :cum-pl}
   {:path :max-dd}])

(defn bruteforce-result-ui [{:keys [label template-id calculated algo variations result]}
                            set-id]
  [:div.flex.flex-col
   [:div.grid.grid-cols-2
    [:p "label"] [:p label]
    [:p "template-id"] [:p template-id]
    [:p "calculated"] [:p calculated]
    [:p "variations"] [:p (pr-str variations)]
    [:p "result"] [:p (count result)]]

   [rtable.rtable/rtable table-opts (make-cols variations set-id) result]])

;