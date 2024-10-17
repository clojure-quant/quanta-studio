(ns dev.bruteforce-data
  (:require
   [cquant.tmlds :refer [transit-json-file->ds]]))

(transit-json-file->ds
 ".data/public/bruteforce/eth-atr-50-exception-test/CvpVmA-backtest.transit-json")




