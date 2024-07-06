(ns quanta.studio.page.dev
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
    [link-dispatch [:bidi/goto 'quanta.studio.page.dev.layout/page-layout-1] "layout-1"]
    [link-dispatch [:bidi/goto 'quanta.studio.page.dev.layout/page-layout-2] "layout-2"]
    [link-dispatch [:bidi/goto 'quanta.studio.page.dev.highchart/highchart-page] "highchart"]
    [link-dispatch [:bidi/goto 'quanta.studio.page.dev.highchart/highstock-page] "highstock"]
    [link-dispatch [:bidi/goto 'quanta.studio.page.dev.color/color-page] "color"]
    [link-dispatch [:bidi/goto 'quanta.studio.page.dev.assetpicker/assetpicker-page] "asset-picker"]]

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