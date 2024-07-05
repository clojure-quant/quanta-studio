(ns quanta.alert
  (:require
   [ta.viz.ds.edn :refer [edn-render-spec]]))

(defn report-data  [opts data]
  (edn-render-spec :alert/data
                   {:alert false
                    :opts opts
                    :data data}))

(defn trade-alert [opts data side]
  (edn-render-spec :alert
                   {:alert true
                    :side side
                    :opts opts
                    :data data}))

(defn alert? [{:keys [data spec]}]
  (= spec :alert))

(defn code [text]
  (str "<code>" text "</code>"))

(defn bold [text]
  (str "<b>" text "</b>"))

(defn italic [text]
  (str "<i>" text "</i>"))

(defn edn [d]
  (-> d pr-str code))

(defn header [side asset interval]
  (str (bold (str side " " asset))
       (italic (str " " interval))))

(defn alert->telegram-message [{:keys [data] :as render-data}]
  (let [{:keys [opts data side]} data
        {:keys [asset calendar data side]} data
        [market interval] calendar]
    {:html (str (header side asset interval)
                (edn data)
                (edn opts))}))


