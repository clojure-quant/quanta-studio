(ns quanta.template-test
  (:require [clojure.test :refer :all]
            [quanta.template :as t]
            [quanta.studio :as s]
            ))

(def template {:id :test/sample-algo
               :algo {:type :trailing-bar
                      :calendar [:crypto :h]
                      :source :dynamic-compressing
                      :trailing-n 1440
                      :import :bybit
                      :asset "BTCUSDT"
                      :algo 'test/sample-algo

                      :band-type :atr
                      :band-len 20
                      :band1-mult 5.5

                      ; position management
                      :entry [:fixed-amount 100000]
                      :exit [:loss-percent 2.0
                             :profit-percent 1.0
                             :time 5]}
               :options [{:type :select
                          :path [:asset]
                          :name "asset"
                          :spec ["BTCUSDT" "ETHUSDT"]}
                         {:type :select
                          :path [:trailing-n]
                          :name "trailing-n"
                          :spec [720 1440 2880 5000 10000]}

                         {:type :select
                          :path [:band-type]
                          :name "Band type"
                          :spec [:stddev :atr :tr :rsi]}
                         {:type :string
                          :coerce :int
                          :path :band-len
                          :name "Band length"}
                         {:type :string
                          :coerce :double
                          :path :band1-mult
                          :name "Band 1 multiplicator"}
                         {:type :string
                          :coerce :double
                          :path :band2-mult
                          :name "Band 2 multiplicator"}
                         {:type :string
                          :path :band3-mult
                          :name "Band 3 multiplicator"}]})


(deftest coerce-options
  (let [coerced-options (t/coerce-options template
                                          {:band-type :rsi
                                           :band-len "20"
                                           :band1-mult "1.5"
                                           :band2-mult "2"
                                           :band3-mult "3.5"})]
    (testing "coerce int"
      (is (= (:band-len coerced-options) 20))
      (is (not= (:band-len coerced-options) "20")))
    (testing "coerce double"
      (is (= (:band1-mult coerced-options) 1.5))
      (is (not= (:band1-mult coerced-options) "1.5"))
      (is (= (:band2-mult coerced-options) 2.0))
      (is (not= (:band2-mult coerced-options) "2"))
      (is (not= (:band2-mult coerced-options) "2.0")))
    (testing "without coerce"
      (is (= (:band-type coerced-options) :rsi))
      (is (not= (:band-type coerced-options) :atr))
      (is (= (:band3-mult coerced-options) "3.5"))
      (is (not= (:band3-mult coerced-options) 3.5)))
    ))

;; option keys can be mixed: keyword or vector
(deftest coerce-options-array-key
  (let [coerced-options (t/coerce-options template
                                          {[:band-type] :rsi
                                           [:band-len] "20"
                                           [:band1-mult] "1.5"
                                           [:band2-mult] "2"
                                           [:band3-mult] "3.5"})
        band-type (get coerced-options [:band-type])
        band-len (get coerced-options [:band-len])
        band1-mult (get coerced-options [:band1-mult])
        band2-mult (get coerced-options [:band2-mult])
        band3-mult (get coerced-options [:band3-mult])]
    (testing "coerce int"
      (is (= band-len 20))
      (is (not= band-len "20")))
    (testing "coerce double"
      (is (= band1-mult 1.5))
      (is (not= band1-mult "1.5"))
      (is (= band2-mult 2.0))
      (is (not= band2-mult "2"))
      (is (not= band2-mult "2.0")))
    (testing "without coerce"
      (is (= band-type :rsi))
      (is (not= band-type :atr))
      (is (= band3-mult "3.5"))
      (is (not= band3-mult 3.5)))
    ))

(deftest coerce-options-non-string-values
  (let [coerced-options (t/coerce-options template
                                          {:band-type :rsi
                                           :band-len 20
                                           :band1-mult 1.5
                                           :band2-mult 2
                                           :band3-mult 3.5})]
    (testing "coerce int"
      (is (= (:band-len coerced-options) 20))
      (is (not= (:band-len coerced-options) "20")))
    (testing "coerce double"
      (is (= (:band1-mult coerced-options) 1.5))
      (is (not= (:band1-mult coerced-options) "1.5"))
      (is (= (:band2-mult coerced-options) 2))            ; TODO: coerce non string values too? int => double etc.
      (is (not= (:band2-mult coerced-options) "2.0")))
    (testing "without coerce"
      (is (= (:band3-mult coerced-options) 3.5))
      (is (not= (:band3-mult coerced-options) "3.5")))
    ))



(comment

  ;(s/load-with-options nil template)

  (t/coerce-options template
                    {:band-type :rsi
                     :band-len "20"
                     :band1-mult "1.5"
                     :band2-mult "2"
                     :band3-mult "3.5"})

  (t/coerce-options template
                    {[:band-type] :rsi
                     [:band-len] "20"
                     [:band1-mult] "1.5"
                     [:band2-mult] "2"
                     [:band3-mult] "3.5"})

  (t/coerce-options template
                    {:band-type :rsi
                     :band-len 20
                     :band1-mult 1.5
                     :band2-mult 2
                     :band3-mult 3.5})

 )