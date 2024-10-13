(ns dev.bruteforce-data
  (:require 
    [cquant.tmlds :refer [transit-json-file->ds]]
   )
  
  )

(transit-json-file->ds 
 ".data/public/bruteforce/demo-bruteforce/HyIPuo-backtest.transit-json"
 
 )