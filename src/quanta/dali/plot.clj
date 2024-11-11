(ns quanta.dali.plot
  (:require
   [potemkin :refer [import-vars]]
   ;dali
   [dali.plot.exception]
   [dali.plot.anomaly]
   [dali.plot.hiccup]
   ; rtable
   [rtable.plot.aggrid]
   [rtable.plot.cheetah]
   [rtable.plot.rtable]
   [rtable.plot.highcharts]
   [rtable.plot.pixi]
   [rtable.plot.vega]
   ;quanta
   [quanta.dali.plot.backtest]
   ;[quanta.dali.transform.date]
   ))

; consolidate various namespaces to one "api" namespace.

; we might need to switch to expose-api to generate code that static code analyzers like clj-lint can use
; https://github.com/xadecimal/expose-api

(import-vars
 ; dali 
 dali.plot.exception/exception
 dali.plot.anomaly/anomaly
 dali.plot.hiccup/hiccup

 ; rtable
 rtable.plot.aggrid/aggrid-ds
 rtable.plot.cheetah/cheetah-ds
 rtable.plot.rtable/rtable
 rtable.plot.rtable/rtable-ds
 rtable.plot.highcharts/highstock-ds
 rtable.plot.pixi/pixi-ds
 rtable.plot.vega/vegalite-ds
 rtable.plot.vega/vegalite
 rtable.plot.vega/vega
 ; quanta
 quanta.dali.plot.backtest/backtest-ui-ds)

