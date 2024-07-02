
(ns ta.db.bars.shuffle
  (:require
   [tech.v3.datatype.functional :as dfn]
   [tablecloth.api :as tc]
   [ta.indicator.returns :refer [forward-shift-col]]))

(defn shuffle-bar-series [ds]
  (let [open  (-> ds :open dfn/log)
        close (-> ds :close dfn/log)
        high (-> ds :high dfn/log)
        low (-> ds :low dfn/log)
        open-1 (forward-shift-col open 1)
        open-chg (dfn/- open open-1)
        close-chg (dfn/- close open)
        high-chg (dfn/- high open)
        low-chg (dfn/- low open)
        ds-log (tc/add-columns ds {:open open
                                   :open-1 open-1
                                   :close close
                                   :open-chg open-chg
                                   :close-chg close-chg
                                   :high high
                                   :high-chg high-chg
                                   :low low
                                   :low-chg low-chg})
        ds-log-first (tc/first ds-log)
        index-shuffled (shuffle (range 1 (tc/row-count ds-log)))
        ds-log-rest (tc/select-rows ds-log index-shuffled)
        open-log-0 (-> ds-log-first :open first)
        ds-shuffled (tc/concat ds-log-first ds-log-rest)
        open-s  (reductions + open-log-0 (:open-chg ds-log-rest))
        close-s (dfn/+ (:close-chg ds-shuffled) open-s)
        high-s (dfn/+ (:high-chg ds-shuffled) open-s)
        low-s (dfn/+ (:low-chg ds-shuffled) open-s)
        open-p (dfn/exp open-s)
        close-p (dfn/exp close-s)
        high-p (dfn/exp high-s)
        low-p (dfn/exp low-s)]
    ;(println ds-log-first)
    (-> ds-shuffled
        (tc/add-columns {:open-s open-s
                         :close-s close-s
                         :high-s high-s
                         :low-s low-s
                         :open-p open-p
                         :close-p close-p
                         :high-p high-p
                         :low-p low-p})
        (tc/drop-columns [:open :close :high :low :open-1])
        (tc/drop-columns [:low-chg :open-chg :close-chg :high-chg])
        (tc/drop-columns [:open-s  :close-s :high-s :low-s])
        (tc/rename-columns {:open-p :open
                            :close-p :close
                            :high-p :high
                            :low-p :low})
        (tc/add-column :date (:date ds)))
    ;open-log-0
    ))
(comment
  (as-> (tc/dataset {:date (range 11)
                     :open [2.0  3 4 5 6 7 8 9 10 11 10]
                     :close [2.5  3.5 4.6 5.5 6.4 7.5 8.5 9.2 10.3 11.4 12]
                     :high [3.5  4.5 5.6 6.5 7.4 8.5 9.5 10 11 14 15]
                     :low [1.5  2.5 3.6 4.5 5.4 6.5 7.5 8 9 10 11.1]}) x

    (shuffle-bar-series x))
;
  )






