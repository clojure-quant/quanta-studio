(ns quanta.studio.view.date
  (:require
   [tick.core :as t]
   [tick.editor :refer [instant-editor]]))

(def h2 (t/new-duration 2 :hours))

(defn move-back-h2 [dt]
  (t/<< dt h2))

(defn move-forward-h2 [dt]
  (t/>> dt h2))

(def h8 (t/new-duration 8 :hours))

(defn move-back-h8 [dt]
  (t/<< dt h8))

(defn move-forward-h8 [dt]
  (t/>> dt h8))

(def day1 (t/new-duration 1 :days))

(defn move-back-day [dt]
  (t/<< dt day1))

(defn move-forward-day [dt]
  (t/>> dt day1))

(def day7 (t/new-duration 7 :days))

(defn move-back-week [dt]
  (t/<< dt day7))

(defn move-forward-week [dt]
  (t/>> dt day7))

(def day30 (t/new-duration 30 :days))

(defn move-back-month [dt]
  (t/<< dt day30))

(defn move-forward-month [dt]
  (t/>> dt day30))

(defn dt-scroller [dt-a]
  [:div
   [:span (str @dt-a)]
   [instant-editor {:instant-a dt-a}]
   [:button.m-1.border.border-round {:on-click #(swap! dt-a move-back-h2)} "<2H"]
   [:button.m-1.border.border-round {:on-click #(swap! dt-a move-forward-h2)} "2H>"]
   [:button.m-1.border.border-round {:on-click #(swap! dt-a move-back-h8)} "<8H"]
   [:button.m-1.border.border-round {:on-click #(swap! dt-a move-forward-h8)} "8H>"]
   [:button.m-1.border.border-round {:on-click #(swap! dt-a move-back-day)} "<D"]
   [:button.m-1.border.border-round {:on-click #(swap! dt-a move-forward-day)} "D>"]
   [:button.m-1.border.border-round {:on-click #(swap! dt-a move-back-week)} "<W"]
   [:button.m-1.border.border-round {:on-click #(swap! dt-a move-forward-week)} "W>"]
   [:button.m-1.border.border-round {:on-click #(swap! dt-a move-back-month)} "<M"]
   [:button.m-1.border.border-round {:on-click #(swap! dt-a move-forward-month)} "M>"]])

