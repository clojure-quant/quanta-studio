(ns quanta.alert
  (:require
   [tick.core :as t]
   [quanta.viz.plot.edn :as plot]))

(defn report-data
  "provides data of the current state of an algo.
   visualised in the web-ui.
   - algo-opts: the opts from the algo
   - data: optional data to display"
  [algo-opts data]
  (plot/edn :alert/data
            {:alert false
             :opts algo-opts
             :data data}))

(defn trade-alert
  "creates a trade alert
   - side:long or :short
   - algo-opts: the opts from the algo
   - data: optional data to display"
  [side algo-opts data]
  (plot/edn :alert
            {:alert true
             :side side
             :opts algo-opts
             :data data}))

(defn alert? [{:keys [data spec]}]
  (= spec :alert))

(defn alert-data? [{:keys [data spec]}]
  (= spec :alert/data))

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

(defn render-spec->alert [{:keys [data spec]}]
  data)

(defn alert->telegram-message [algo-render-spec]
  (let [a (render-spec->alert algo-render-spec)
        {:keys [alert side opts data]} a
        {:keys [asset calendar]} opts
        [market interval] calendar
        side (if alert side :debug)]
    {:html (str (header side asset interval)
                (italic " opts:")
                (edn opts)
                (italic " data:")
                (edn data))}))

(defn test-alert
  "this function is to be used as a telegram command.
   it returns a test alert."
  [_]
  (let [a (trade-alert :buy
                       {:asset "TEST"
                        :calendar [:crypto :m]
                        :date (t/instant)}
                       {:x 1
                        :y [2 3 4]
                        :z 5})]
    (alert->telegram-message a)))

(comment

  (def a (trade-alert :buy
                      {:asset "TEST"
                       :calendar [:crypto :m]
                       :date (t/instant)}
                      {:x 1
                       :y [2 3 4]
                       :z 5}))

  (render-spec->alert a)

  (alert->telegram-message a)

  (test-alert nil)

; 
  )


