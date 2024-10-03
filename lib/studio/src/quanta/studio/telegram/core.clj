(ns quanta.studio.telegram.core
  (:require
   [taoensso.timbre :refer [info warn error]]
   [tablecloth.core :as tc]
   [telegram.service]
   [telegram.pubsub :as tpubsub]
   [quanta.studio.telegram.alert :refer [alert? alert-data? alert->telegram-message
                                         report-data trade-alert]]))

(def ^:dynamic *telegram* nil)

(defn text [s]
  {:html [:p {pr-str s}]})

(defn ping [_]
  {:text "pong"})

 ; (tpubsub/publish this topic msg)

(defn publish [result]
  (when (alert? result)
    (let [msg (alert->telegram-message result)]
      (warn "sending telegram-trade-signal: " msg)
      (tpubsub/publish *telegram*  "trade-signal" msg)))

  (when (alert-data? result)
    (let [msg (alert->telegram-message result)]
      (warn "sending telegram-signal-debug: " msg)
      (tpubsub/publish *telegram*  "trade-signal-debug" msg))))

(defn telegram-pusher-start [telegram-config]
  (telegram.service/telegram-bot-start
   telegram-config
   {:commands [{:command "ping" :description "service ok?"
                :rpc-fn 'quanta.studio.telegram/ping}
               {:command "testalert" :description "test alert"
                :rpc-fn 'quanta.alert/test-alert}
               {:command "subscribe" :description "subscribe topic"
                :rpc-fn 'telegram.pubsub/subscribe
                :opts [{:title "topic" :options ["error" "trade-signal" "trade-signal-debug"]}]}
               {:command "unsubscribe" :description "unsubscribe topic"
                :rpc-fn 'telegram.pubsub/unsubscribe
                :opts [{:title "topic" :options ["error" "trade-signal" "trade-signal-debug"]}]}
               {:command "subscriptions" :description "current subscriptions"
                :rpc-fn 'telegram.pubsub/my-subscriptions}]}))

;:alert {:viz         'alex.algo.powersignal/bollinger-alert
;        :viz-options {:a "we really want the algo options here"}}

(defn bollinger-alert [viz-opts bar-ds]
  (let [last-row (-> (tc/last bar-ds)
                     (tc/rows :as-maps)
                     last)
        data (select-keys last-row [:asset :date :close
                                    :entry
                                    :bars-above-b1h :carried-b2l
                                    :bars-below-b1l :carried-b2l])
        {:keys [asset entry-bool entry]} data
        opts {:asset asset}]
    (if entry-bool
      (trade-alert entry opts data)
      (report-data opts data))))

