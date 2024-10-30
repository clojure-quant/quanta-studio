(ns quanta.dali.viewer.trade.roundtrip-table
  (:require
   [rtable.viewer.cheetah :refer [cheetah-ds]]
   [quanta.viz.format :as f]
   [quanta.viz.cheetah-style :refer [blue-color]]))

; non displayed columns:
;
; :volume-trading	:cum-log :volume-exit	:equity
;	 :cum-prct	:pl-log	:pl-gross		:pl-points	
;  :cum-points	

(defn roundtrips-cheetah [ds]
  [cheetah-ds
   {:style {:width "100%" :height "100%"}
    :columns [; bar
              {:field "asset" :caption "asset" :width 90}
              ;{:field "id" :caption "id" :width 50}
              {:field "side" :caption "side" :width 50}
              {:field "qty" :caption "qty" :width 50}
              {:field "volume-entry" :caption "vol" :width 50 :format f/nr-format-0-digits}
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
               ; pl
              {:field "pl" :caption "pl" :width 50 :format f/nr-format-0-digits}
              {:field "pl-prct" :caption "pl%" :width 50 :format f/nr-format :format-args ["%.1f"]}
              {:field "fee" :caption "fee" :width 50 :format f/nr-format-0-digits}
              {:field "equity" :caption "equity" :width 50 :format f/nr-format-0-digits}
              {:field "drawdown" :caption "drawdown" :width 50 :format f/nr-format-0-digits}
              {:field "drawdown-prct" :caption "ddl%" :width 50 :format f/nr-format-0-digits}
              ; metrics
              {:field "bars" :caption "bars" :width 50}
              {:field "win?" :caption "win?" :width 50 :style blue-color :format f/format-bool}]
    :ds ds}])