(ns quanta.dali.viewer.bruteforce
  (:require
   [reagent.core :as r]
   [promesa.core :as p]
   [rtable.rtable]
   [cquant.tmlds :refer [GET]]
   [dali.viewer.exception :refer [exception]]
   [quanta.dali.viewer.trade.backtest :refer [backtest-ui]]))

;; bruteforce roundtrip-ui

(def stats-opts
  {:intraday? false
   :style {:height "100%"
           :min-height "100%"
           :width "100%"
           :min-width "100%"}
   :class "bg-blue-100"})

(defn load-backtest [label id backtest-a]
  (let [url (str "/r/bruteforce/" label "/" id "-backtest.transit-json")
        _   (println "loading backtest from url: " url)
        load-promise (GET url)]
    (-> load-promise
        (p/then (fn [data]
                  (println "ds from url " url " loaded successfully.")
                  (println "backtest data keys: " (keys data))
                  (println "backtest data: " data)
                  (reset! backtest-a data)
                  data))
        (p/catch (fn [err]
                   (println "could not load ds from url " url " err: " err)
                   (reset! backtest-a nil))))
    nil))

(defn bruteforce-roundtrips [label {:keys [id error]}]
  (let [loaded-a (r/atom [nil nil])
        backtest-a (r/atom nil)]
    (fn [label {:keys [id error]}]
      (when (and label id
                 (not error)
                 (not (= [label id] @loaded-a)))
        (println "loading backtest label: " label " id:  " id)
        (load-backtest label id backtest-a)
        (reset! loaded-a [label id])
        nil)
      (if error
        [:div [exception error]]
        (if @backtest-a
          [backtest-ui (merge @backtest-a stats-opts)]
          [:div "label:" label  " id: " id "error:" (pr-str error)])))))

;; bruteforce label results

(def table-opts
  {:class "table-head-fixed padding-sm table-blue table-striped table-hover"
   :style {:width "100%"
           :height "100%"
           :border "3px solid green"}})

(defn make-render-id  [set-id]
  (fn [col-info row]
    (let [id (:id row)
          error (:error row)]
      [:div
       [:a {:on-click #(set-id {:id id :error error})}
        [:i {:class "fas fa-eye m-1"}]]

       [:span.text-blue-500 id]])))

(defn make-cols  [variation-cols set-id]
  ; :variations [[0 :asset] ["BTCUSDT" "ETHUSDT"] 
  ;              [2 :day :atr-n] [20 50]]
  (concat
   [{:path :id :header "id" :render-cell (make-render-id set-id)}
    {:path :target}
    {:path :trades}
      ; {:path :cum-pl}
      ; {:path :max-dd}
    ]
   (map (fn [path]
          {:path [path]}) variation-cols)))

(defn bruteforce-result-ui [{:keys [label template-id calculated
                                    algo
                                    variations variation-cols
                                    result]}
                            set-id]
  [:div.flex.flex-col.w-full.h-full
   [:div.grid.grid-cols-2
    [:p "label"] [:p label]
    [:p "template-id"] [:p template-id]
    [:p "calculated"] [:p calculated]
    [:p "variations"] [:p (pr-str variations)]
    [:p "variation-cols"] [:p (pr-str variation-cols)]
    [:p "result"] [:p (count result)]]
   [rtable.rtable/rtable table-opts (make-cols variation-cols set-id) result]])

;