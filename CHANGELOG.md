



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


  