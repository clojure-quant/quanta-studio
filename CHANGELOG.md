
2024-09-21
- studio template ui always has a dt selector
  this is more logical, as the historic-calculation always requires a date

  ther is one drawdown: if the current date is part of the algo-opts, 
  then it is easier to make a link to an algo at a specific date.
- studio template ui visualizations are now 100% arbitrary.
  the keywords used to give names to vizualisations do not have any requirements.
- viz/highstock: date col of zoned-date-time supported.

2024-10-01
- NEW DAG ENGINE.
- not yet integrated to ui.
- ALGO-SPEC differences:
  - algo-fn is a FUNCTION, not a symbol, and a processing chain is not supported
    reason: if symbols are applied in a template, then this can mean the algo-fn
            cannot be compiled. so by having symbols, we eliminate one source of
            errors. the new dag engine powered by missionary is so powerful, that
            we can have thousands of individual cells, and still not have to worry
            about performance. 
  - :type :trailing-bar is no longer needed, to load bars, you should use 
    inside the code of the algo [quanta.dag.env.bars :refer [get-trailing-bars]]
    see: dev.bollinger-algo
  - ENV removed, this is now via BINDINGS *bar-db* etc
    algo-fn expects [opts dt]
    formula-fn expects [opts & args]


2024-10-02 
- remove javelin engine, going forward: only missionary dag
  0.4.xxx will no longer contain javelin

2024-10-13
- quanta.trade new backtest engine
- sub-projects studio-dev and dev for developers


2024-10-29 BREAKING CHANGES
- dali viewer system for ui
- quanta.dali.plot now is the only plot namespace you need. (forget quanta.viz)
- chart-spec highchart 
  - {:type :ohlc :mode :candle}  type candlestick no longer supported
  - :chart {:box :fl} is no longer used. Use style and class instead. Default is 100% width/height, so not required to be set.


2024-11-03 BREAKING CHANGES
- ns quanta.bar.env  was formerly called quanta.studio.bar.env 
- get-bars in template always needs to be separate because it is now async.
- ALGO:
  - in the template :algo stays (might get renamed to :cells)
  - algo-spec: :algo -> :fn
- algo-env: needs to be defined as keyword
  SERVICES.edn - STUDIO
- LOGGING in ALGOS:
  - log now requires ENV to be passed.
  - so algos that want to log need to have (fn [env opts & args])
    in template add :env? true
- PLOT SPEC highchart-ds 
  - for candles:  :bar {:type :ohlc :mode :candle}
  - no longer needed: :chart {:box :fl}
                 

2024-11-03 BREAKING CHANGES TEMPLATE TOPOLOGY
- change [] to {}
- add :* key for global options
- Making this changes is REALLY FAST. 
- Negative side: single algo needs {:algo {xxx}} (or whatever name you want to put) instead of {xxx}
- Of course, it requires the Template Option Paths to be adjusted, but they become much easier 
  to reason about, which was the reason for this change.
