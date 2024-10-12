(ns quanta.studio.dev.page.raw.cheetah
  (:require
   [rtable.render.cheetah :refer [cheetah-ds]]))

(defn cheetah-table []
  [cheetah-ds {:style {:width "100%" :height "100%"}
               :columns [; bar
                         {:field "asset" :caption "a" :width 90}
                         {:field "entry-date" :caption "d" :width 220}
                         {:field "entry-price" :caption "o" :width 90
                          ;:style 'demo.page.cheetah/red-color
                          }
                         ;{:field "close" :caption "c" :width 90 :style blue-color}
                         {:field  "entry-idx" :caption "B?" :width 50 :style {:bgColor "#5f5"}}
                         {:field  "cross-down" :caption "XD" :width 50}
                         {:field  "cross-down-c" :caption "XD_" :width 50}
                         {:field  "long-signal" :caption "LS" :width 50}

                         {:field  "above-band" :caption "A?" :width 50}
                         {:field  "cross-up" :caption "XU" :width 50}
                         ;{:field  "cross-up-c" :caption "XU_" :width 50 :style demo.page.cheetah/bool-color}
                         {:field  "short-signal" :caption "SS" :width 50}
                         {:field "entry" :caption "entry" :width 50}]
               :url "/r/data/LWhgL6-roundtrips.transit-json"}])

(defn page [_route]
  [:div.h-screen.w-screen.bg-red-500
   [cheetah-table]])

