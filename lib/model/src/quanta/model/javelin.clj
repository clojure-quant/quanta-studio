(ns quanta.model.javelin
  (:require
   [modular.system]
   [quanta.model.protocol :refer [model]]
   [quanta.model.javelin.cell :as j-cell]
   [quanta.model.javelin.calendar :as j-cal]))

(defrecord model-javelin [calendars]
  model
  ; cell
  (calendar-cell [this time-fn calendar]
    (j-cell/calendar-cell this time-fn calendar))
  (formula-cell [this formula-fn cell-seq]
    (j-cell/formula-cell this formula-fn cell-seq))
  (value-cell [this v]
    (j-cell/value-cell this v))
  (destroy-cell [this v]
    (j-cell/destroy-cell this v))
  ; calendar
  (create-calendar [this calendar]
    (j-cal/create-calendar this calendar))
  (get-calendar [this calendar]
    (j-cal/get-calendar this calendar))
  (set-calendar! [this data]
    (j-cal/set-calendar! this data))
  (active-calendars [this]
    (j-cal/active-calendars this)))

(defn create-model-javelin []
  (let [calendars (atom {})]
    (model-javelin. calendars)))
