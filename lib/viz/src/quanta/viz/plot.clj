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

; hiccup
; algo-astro/src/astro/hiccup.clj

; rtable
; algo-juan/src/juan/template.clj
; 33:   :table {:viz 'ta.viz.ds.rtable/rtable-render-spec
; algo-astro/src/astro/template.clj
; 34:   :table {:viz 'ta.viz.ds.rtable/rtable-render-spec
; algo-demo/src/algodemo/sma_crossover/template.clj
; 49:   :table {:viz 'ta.viz.ds.rtable/rtable-render-spec

; vega
; algo-demo/src/algodemo/sentiment_spread/template.clj
; 27:   :chart {:viz 'ta.viz.ds.vega/vega-render-spec
; algo-demo/src/quanta/notebook/study/asset_compare.clj
; 57:  (require '[ta.viz.ds.vega :refer [convert-data]])
; quanta-demo/src/quanta/notebook/study/ma_compare.clj
; 7:   [ta.viz.ds.vega :refer [vega-render-spec ds-stacked]]

;; highchart
; algo-alex/src/alex/template.clj
; 78:   :chart {:viz 'ta.viz.ds.highchart/highstock-render-spec
; algo-juan/src/juan/notebook/study/backtest_raw.clj
; 174:(require '[ta.viz.ds.highchart :refer [highstock-render-spec]])
; algo-gann/src/ta/gann/template.clj
; 30:   :chart {:viz 'ta.viz.ds.highchart/highstock-render-spec
; algo-astro/src/astro/template.clj
; 27:   :chart {:viz 'ta.viz.ds.highchart/highstock-render-spec
; quanta-demo/src/quanta/notebook/study/live/crypto.clj
; 8:   [ta.viz.ds.highchart :refer [highstock-render-spec]]))
; algo-demo/src/algodemo/sma_crossover/template.clj
; 31:   :chart {:viz 'ta.viz.ds.highchart/highstock-render-spec

(import-vars
 quanta.viz.plot.edn/edn
 quanta.viz.plot.hiccup/hiccup
 quanta.viz.plot.rtable/rtable
 quanta.viz.plot.vega/vega
 quanta.viz.plot.highchart/highstock
 quanta.viz.plot.anomaly/anomaly)

