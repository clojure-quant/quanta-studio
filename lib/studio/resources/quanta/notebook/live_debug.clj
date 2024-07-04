(ns quanta.notebook.live-debug
  (:require
    [modular.system]
    [ta.algo.env.protocol :as algo]))
  
  ;; 1. get algo-env form clip
  
(def env-live (modular.system/system :live))



(-> env-live 
    :model
    :calendars
    deref
    ;keys
    )



(def tp (modular.system/system :tickerplant))

tp

(-> tp 
    :time-generator
    :calendars
    deref
    keys
    )


