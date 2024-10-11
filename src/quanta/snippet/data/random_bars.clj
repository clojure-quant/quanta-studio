(ns quanta.snippet.data.random-bars
  (:require
   [tablecloth.api :as tc]
   [tick.core :as t]
   [quanta.snippet.data.random-series :as rand]))

(defn days-ago-instant [n]
  (-> (t/instant)
      (t/<< (t/new-duration n :days))))

(defn higher [v]
  (+ v (rand/random-float 0.5 1.5)))

(defn lower [v]
  (- v (rand/random-float 0.5 1.5)))

(defn volume [_v]
  (rand/random-float 10000 100000))

(defn jitter [v]
  (+ v (rand/random-float -0.1 0.1)))

(defn add-open-high-low-volume [ds]
  (tc/add-columns ds
                  {:open #(map jitter (% :close))
                   :high #(map higher (% :close))
                   :low #(map lower (% :close))
                   :volume #(map volume (% :close))}))

(defn random-bar-ds [n]
  (-> (tc/dataset
       {:date (->> (range n)
                   (map days-ago-instant)
                   reverse)
        :close (rand/random-series n)})
      add-open-high-low-volume))

(comment
  (days-ago-instant 500)
  (rand/random-float 5 10)
  (rand/random-float -3 3)
  (random-bar-ds 10)
;
  )