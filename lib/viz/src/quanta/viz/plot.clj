(ns quanta.viz.plot
  (:require
   [potemkin :refer [import-vars]]
   [quanta.viz.plot.edn]
   [quanta.viz.plot.hiccup]
   [quanta.viz.plot.rtable]
   [quanta.viz.plot.vega]
   [quanta.viz.plot.highchart]
   [quanta.viz.plot.anomaly]))

; consolidate various namespaces to one "api" namespace.

; we might need to switch to expose-api to generate code that static code analyzers like clj-lint can use
; https://github.com/xadecimal/expose-api

(import-vars
 quanta.viz.plot.edn/edn
 quanta.viz.plot.hiccup/hiccup
 quanta.viz.plot.rtable/rtable
 quanta.viz.plot.vega/vega
 quanta.viz.plot.highchart/highstock
 quanta.viz.plot.anomaly/anomaly)

