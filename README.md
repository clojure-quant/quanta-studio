# quanta-studio [![GitHub Actions status |clojure-quant/quanta-studio](https://github.com/clojure-quant/quanta-studio/workflows/CI/badge.svg)](https://github.com/clojure-quant/quanta-studio/actions?workflow=CI)[![Clojars Project](https://img.shields.io/clojars/v/io.github.clojure-quant/quanta-studio.svg)](https://clojars.org/io.github.clojure-quant/quanta-studio)


Quanta Studio 
- uses 
  - quanta (technical indicators, calendars (exchange and interval))
  - quanta-market (connections with the market: asset-db, bar download, 
    quotefeeds, trading-apis)
  - quanta-bar-warehouse (timeseries storage in nippy/duckdb and bar importer)
  - quanta-dag-algo (a spreadsheet engine (a dag) that can add algos)
  - quanta-trade (algo backtest, trading-robot, reporting)
- provides
  - web ui (and plot and render functions)
  - task runner
  - template database
  - alert engine (via telegram)


# developers

```
cd dev
clj -X:preload     (to load data)
clj -X:quanta-backtest (to run studio)
```

*ports*
- 8080 webserver
       /quanta is the user ui
       /quanta/dev is the developer ui
- 9100 nrepl





