(ns ta.wh-test
  (:require
   [clojure.test :refer :all]
   [ta.db.bars.random :refer [random-dataset]]
   [ta.warehouse :as wh]
   [ta.config]))

(defn series-generate-save-reload
  [size name]
  (let [ts-original (random-dataset size)
        symbol (str "_test_" name "_")
        _ (wh/save-ts :test-wh ts-original symbol)
        ts-reloaded (wh/load-ts :test-wh symbol)]
    (is (= (count ts-original) (count ts-reloaded)))
    (is (= ts-original ts-reloaded))))

(deftest test-wh
  (series-generate-save-reload 2000 "small")
  (series-generate-save-reload 20000 "big")) ; tradingview limit




