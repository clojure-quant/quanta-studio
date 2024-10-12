(ns quanta.studio.dev.page.raw.vegaplot
  (:require
   [ui.vega.plot.core :refer [list-plot bar-chart compose histogram plot]]))

(def d
  [1 3 5 7 9 5 4 6 9 8 3 5 6])

;; **
;;; List plot plots data in the style of a scatter plot by default.
;; **

(list-plot d)

;;; There are a number of options that can be given to the plot. At the moment your best bet is to peek at the source code to see what they are.

(list-plot d :joined true
           :plot-size 400
           :aspect-ratio 1.6
           :opacity 0.5
           :plot-range [:all :all])

;;; You can also plot bar charts. The first argument is the list of category names, the second the category values.

(bar-chart (range (count d)) d)

;;; It's interesting to look at the structure of a plot.

(def p (list-plot d))

;;; Another key feature of gorilla-plot, is that because plots are values, it's straighforward to compose them. Here's another plot

(def p2 (list-plot  d
                    :joined true
                    :plot-range [1 5]))

;;; And here we compose them to form a single plot. The axes and plot range are taken from the first plot given to compose.

(compose p p2)

;;; You can also plot functions.

(plot (fn [x] (.sin js/Math x)) [0 10] :color "orange" :plot-points 50)

;;; And, of course, these plots compose too.

(compose
 (list-plot (map #(vector % (rand %)) (range 0 10 0.01)) :opacity 0.3 :symbol-size 50)
 (plot (fn [x] (* x (.pow js/Math (.sin js/Math x) 2))) [0 10]))

;;; There's a histogram plot type as well. As above, it composes with list-plots well.

(compose
 (histogram (repeatedly 1000 #(rand 10)) :color "steelblue" :bins 100 :normalize :probability-density)
 (plot (constantly 0.1) [0 10]))

;;; Something a little fancier!

(defn gaussian
  [x sigma mu]
  (* (/ 0.4 sigma) (.exp js/Math (- (/ (* (- x mu) (- x mu)) (* 2 (* sigma sigma)))))))

(defn vegaplot-demo []
  [:div
   (compose
    (histogram (repeatedly 10000 #(- (apply + (repeatedly 50 rand)) 25))
               :bins 100
               :normalise :probability-density)
    (plot #(gaussian % 2 0) [-10 10]))

   (compose
    (histogram (repeatedly 10000 #(- (apply + (repeatedly 50 rand)) 25))
               :bins 100
               :normalise :probability-density)
    (histogram (repeatedly 10000 #(- (apply + (repeatedly 50 rand)) 23))
               :bins 100
               :normalise :probability-density
               :color "green")
    (plot #(gaussian % 2 0) [-10 10])
    (plot #(gaussian % 2 2) [-10 10] :color "orange"))])

(defn page [_route]
  [:div.h-screen.w-screen.bg-red-500
   [vegaplot-demo]])

