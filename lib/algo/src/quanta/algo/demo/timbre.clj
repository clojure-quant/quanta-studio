(ns quanta.algo.demo.timbre
  (:require
   ;[modular.log]
   ;[taoensso.timbre :as timbre :refer [info]]
   [taoensso.telemere :as t]
   ))

(def config
  {:min-level [[#{"org.apache.http.*"
                  "org.eclipse.aether.*"
                  "org.eclipse.jetty.*"
                  "modular.oauth2.*"
                  "modular.oauth2.token.refresh.*"
                  "modular.ws.*"
                  "webly.web.*"} :warn] ; webserver stuff - warn only
                                      ; [#{"modular.ws.*"} :debug]
               [#{"modular.persist.*"} :warn]
               [#{"goldly.service.core"} :warn] ; goldly services - less logging
               [#{"re-flow.core"} :warn] ; goldly services - less logging

                          ; quanta specific:
                          ;[#{"ta.import.provider.bybit.*"} ;bybit is already stable
                          ; :warn]
               [#{"*"} :info]] ; default -> info
   :hostname_ ""
   :appenders {:default {:type :console-color}
               :rolling {:type :file-rolling
                         :path ".data/quanta.log"
                         :pattern :monthly}}})

(modular.log/timbre-config! config)

(info "hello")

(timbre/set-config!
 (merge timbre/default-config
        {:hostname_ ""}))

(t/log! :info "Hello world!")



