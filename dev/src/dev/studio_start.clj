(ns dev.studio-start
  (:require
   [quanta.studio :refer [start-studio]]
   [extension :refer [discover]]))

(def exts (discover))

exts

(def  env-calculation {:log-dir ".data/"
                       :env  {}
                      ;{quanta.algo.env.bars/*bar-db* (clip/ref :bar-engine)}
                       })

(start-studio
 {:exts exts
  :clj nil
  :role nil
  :calculate env-calculation})