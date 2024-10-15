(ns quanta.studio.dev.page.core
  (:require
   [ta.viz.lib.ui :refer [link-dispatch link-href]]))

; main page 

(defn dev-page [{:keys [_route-params _query-params _handler] :as _route}]
  [:div

   ; trateg web ui
   [:div.bg-blue-300.m-5
    [:h1.text-xl.text-red-600 "quanta studio (developer page)"]
    ; tradingview
    [:p.text-blue.text-xl "tradingview"]
    [link-dispatch [:bidi/goto 'ta.tradingview.goldly.page.tradingview-algo/tvalgo-page] "tradingview-algo"]
    [link-dispatch [:bidi/goto 'ta.tradingview.goldly.page.tradingview-udf/tradingview-page] "tradingview-udf"]
    ; backtest
    [:p.text-blue.text-xl "backtest"]
    [link-href "/algo/backtest" "backtester"]
    [link-href "/publish" "published result-views"]
    ; warehouse  
    [:p.text-blue.text-xl "warehouse"]
    [link-href "/warehouse" "warehouse"]
    [link-href "/series" "series"]]

   ; test
   [:div.bg-blue-300.m-5
    [:p.text-blue.text-xl "test"]
    [link-dispatch [:bidi/goto 'quanta.studio.dev.page.layout/page-layout-1] "layout-1"]
    [link-dispatch [:bidi/goto 'quanta.studio.dev.page.layout/page-layout-2] "layout-2"]
    [link-dispatch [:bidi/goto 'quanta.studio.dev.page.color/color-page] "color"]
    [link-dispatch [:bidi/goto 'quanta.studio.dev.page.assetpicker/assetpicker-page] "asset-picker"]]

   [:div.bg-blue-300.m-5
    [:p.text-blue.text-xl "ui-raw"]
    [link-dispatch [:bidi/goto 'quanta.studio.dev.page.raw.highchart/highchart-page] "highchart"]
    [link-dispatch [:bidi/goto 'quanta.studio.dev.page.raw.highchart/highstock-page] "highstock"]
    [link-dispatch [:bidi/goto 'quanta.studio.dev.page.raw.cheetah/page] "cheetah"]
    [link-dispatch [:bidi/goto 'quanta.studio.dev.page.raw.vega/page] "vega"]
    [link-dispatch [:bidi/goto 'quanta.studio.dev.page.raw.vegads/page] "vega-ds"]

    ; vegaplot does not work from cljs 
    ;[link-dispatch [:bidi/goto 'quanta.studio.dev.page.raw.vegaplot/page] "vegaplot"]
    ]

   [:div.bg-blue-300.m-5
    [:p.text-blue.text-xl "ui-quanta"]
    [link-dispatch [:bidi/goto 'quanta.studio.dev.page.render.vega/page] "vega"]
    [link-dispatch [:bidi/goto 'quanta.studio.dev.page.render.rtable/page] "rtable"]
    [link-dispatch [:bidi/goto 'quanta.studio.dev.page.render.highstock/page] "highstock"]
    [link-dispatch [:bidi/goto 'quanta.studio.dev.page.render.roundtripmetrics/page] "backtest-report"]
    [link-dispatch [:bidi/goto 'quanta.studio.dev.page.render.roundtripmetrics/page-stock] "stock-future-report"]]

; reval developer tools
   [:div.bg-blue-300.m-5
    [:p.text-blue.text-xl "developer tools"]
    [link-dispatch [:bidi/goto 'reval.page.viewer/viewer-page :query-params {}] "notebook viewer"]
    [link-dispatch [:bidi/goto 'reval.page.repl/repl-page :query-params {}] "repl"]
    ;[link-dispatch [:bidi/goto 'scratchpad.page.scratchpad/scratchpad] "scratchpad"]
    ;[link-dispatch [:bidi/goto 'goldly.devtools.page.runtime/runtime-page] "environment"]
    ;[link-dispatch [:bidi/goto 'goldly.devtools.page.help/devtools-page] "devtools help"] 
    ]
;
   ])