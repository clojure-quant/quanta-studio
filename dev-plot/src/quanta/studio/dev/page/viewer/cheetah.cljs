(ns quanta.studio.dev.page.viewer.cheetah
  (:require
   [dali.viewer :refer [viewer]]
   [quanta.viz.format :as f]
   [quanta.viz.cheetah-style :refer [blue-color]]))

(defn cheetah-table []
  [viewer
   {:viewer-fn 'rtable.viewer.cheetah/cheetah-ds
    :transform-fn 'rtable.transform.cheetah/load-and-transform-cheetah
    :data {:load {:url "/r/data/LWhgL6-roundtrips.transit-json"}
           :style {:width "100%" :height "100%"}
           :columns [; bar
                     {:field "asset" :caption "a" :width 90}
                     {:field "id" :caption "id" :width 50}
                     {:field "side" :caption "side" :width 50}
                     {:field "qty" :caption "qty" :width 50}
                         ; entry
                     {:field "entry-date" :caption "entry-dt" :width 160
                      :format f/dt-yyyymmdd-hhmm}
                     {:field "entry-idx" :caption "entry-idx" :width 50 :style {:bgColor "#5f5"}}
                     {:field "entry-price" :caption "entry-p" :width 90 #_:style #_'demo.page.cheetah/red-color
                      :format f/nr-format-auto}
                         ; exit
                     {:field "exit-date" :caption "exit-dt" :width 160
                      :format f/dt-yyyymmdd-hhmm}
                     {:field "exit-idx" :caption "exit-idx" :width 50 :style {:bgColor "#5f5"}}
                     {:field "exit-price" :caption "exit-p" :width 50
                      :format f/nr-format-auto}
                     {:field "exit-reason" :caption "exit-reason" :width 90}
                         ; metrics
                     {:field "bars" :caption "bars" :width 50}
                     {:field "win?" :caption "win?" :width 50
                      :style blue-color
                      :format f/format-bool}
                     {:field "ret-abs" :caption "ret-abs" :width 50}
                     {:field "ret-prct" :caption "ret-prct" :width 50
                      :format f/prct}
                     {:field "ret-log" :caption "ret-log" :width 50}
                         ;; nav
                     {:field "cum-ret-volume" :caption "cum-ret-volume" :width 50}
                     {:field "cum-ret-abs" :caption "cum-ret-abs" :width 50}
                     {:field "cum-ret-log" :caption "cum-ret-log" :width 50}
                     {:field "nav" :caption "nav" :width 50
                      :format f/nr-format-auto}]}}])

(defn page [_route]
  [:div.h-screen.w-screen.bg-red-500
   [cheetah-table]])