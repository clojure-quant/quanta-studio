(ns ta.import.provider.compress
  (:require
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [de.otto.nom.core :as nom]
   [tablecloth.api :as tc]
   [ta.calendar.validate :as cal]
   [ta.calendar.compress :refer [compress-to-calendar]]
   [ta.db.bars.protocol :refer [barsource] :as b]))

(defn get-source-interval [interval-config interval]
  (let [source-interval (get interval-config interval)]
    (info "requested interval: " interval "source interval: " source-interval)
    source-interval))

(defrecord compressing-provider [provider interval-config]
  barsource
  (get-bars [this opts window]
    (let [calendar  (:calendar opts)
          market (cal/exchange calendar)
          interval (cal/interval calendar)
          interval-source (get-source-interval (:interval-config this) interval)]
      (if interval-source
        (let [calendar-source [market interval-source]
              opts-source (assoc opts :calendar calendar-source)
              _ (warn "compressing " interval-source " to: " interval " opts: " opts-source)
              ds-higher (b/get-bars (:provider this) opts-source window)]
          (case
           (not ds-higher)
            (nom/fail ::compress {:message "cannot compress dataset, ds-higher is nil"
                                  :opts opts
                                  :range window})

            (nom/anomaly? ds-higher)
            ds-higher

            :else
            (compress-to-calendar ds-higher calendar)))
        (do
          (warn "compressing-provider no compression for: " interval " -forwarding request to import-provider: " opts window)
          (b/get-bars (:provider this) opts window))))))

(defn start-compressing-provider  [provider interval-config]
  (assert (not (nil? interval-config)) "interval-config needs to be a map. currently it is nil.")
  (assert (map? interval-config) "compressing-provider interval-config must be a map")
  (assert (not (nil? provider)) "provider needs to be a map. currently it is nil.")
  (assert (satisfies? barsource provider) "compressing-provider must be keyword")
  (compressing-provider. provider interval-config))

(comment
  (def interval-config
    {:h :m
     :5m :m
     :10m :m
     :30m :m
     :month :d
     :year :d})

  (contains? interval-config :m)
  (contains? interval-config :h)

;  
  )

