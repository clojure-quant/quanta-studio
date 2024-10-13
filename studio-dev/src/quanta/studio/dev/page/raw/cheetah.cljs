(ns quanta.studio.dev.page.raw.cheetah
  (:require
   [rtable.render.cheetah :refer [cheetah-ds]]))

(defn cheetah-table []
  [cheetah-ds {:style {:width "100%" :height "100%"}
               :columns [; bar
                         {:field "asset" :caption "a" :width 90}
                         {:field "id" :caption "id" :width 50}
                         {:field "side" :caption "side" :width 50}
                         {:field "qty" :caption "qty" :width 50}
                         ; entry
                         {:field "entry-date" :caption "entry-dt" :width 200}
                         {:field "entry-idx" :caption "entry-idx" :width 50 :style {:bgColor "#5f5"}}
                         {:field "entry-price" :caption "entry-p" :width 90 #_:style #_'demo.page.cheetah/red-color}
                         ; exit
                         {:field "exit-date" :caption "exit-dt" :width 200}
                         {:field "exit-idx" :caption "exit-idx" :width 50 :style {:bgColor "#5f5"}}
                         {:field "exit-price" :caption "exit-p" :width 50}
                         {:field "reason" :caption ":reason" :width 90}
                         ; metrics
                         {:field "bars" :caption "bars" :width 50}
                         {:field "win?" :caption "win?" :width 50}
                         {:field "ret-abs" :caption "ret-abs" :width 50}
                         {:field "ret-prct" :caption "ret-prct" :width 50}
                         {:field "ret-log" :caption "ret-log" :width 50}
                         ;; nav
                         {:field "cum-ret-volume" :caption "cum-ret-volume" :width 50}
                         {:field "cum-ret-abs" :caption "cum-ret-abs" :width 50}
                         {:field "cum-ret-log" :caption "cum-ret-log" :width 50}
                         {:field "nav" :caption "nav" :width 50}]
               :url "/r/data/LWhgL6-roundtrips.transit-json"}])

(defn page [_route]
  [:div.h-screen.w-screen.bg-red-500
   [cheetah-table]])