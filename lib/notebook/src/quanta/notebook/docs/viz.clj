(ns quanta.notebook.docs.viz
  (:require
   [tablecloth.api :as tc]
   [tick.core :as t]
   [quanta.viz.plot :as plot]))

(def now (t/instant))

(defn add-days [dt days]
  (t/>> dt (t/new-duration days :days)))

(def ds
  (tc/dataset [{:date now :open 1 :high 2 :low 3 :close 4 :volume 5}
               {:date (add-days now 1) :open 1 :high 2 :low 0.9 :close 4 :volume 5}
               {:date (add-days now 2) :open 2 :high 3 :low 1.5 :close 3 :volume 10}
               {:date (add-days now 3) :open 1 :high 4 :low 0.8 :close 2 :volume 5}
               {:date (add-days now 4) :open 2 :high 5 :low 1.5 :close 3 :volume 15}
               {:date (add-days now 5) :open 1 :high 6 :low 0.7 :close 4 :volume 5}
               {:date (add-days now 6) :open 2 :high 7 :low 1.8 :close 5 :volume 15}]))

ds

(plot/highstock {:charts  [{:open "line"
                            :low "line"
                            ;:close :flags
                            }
                           {:volume "column"}]}
                ds)

(plot/rtable {:class "table-head-fixed padding-sm table-red table-striped table-hover"
              :style {:width "50vw"
                      :height "40vh"
                      :border "3px solid green"}
              :columns [{:path :date :format 'ta.viz.lib.format-date/dt-yyyymmdd-hhmm :max-width "80px"}
                        {:path :close :format 'ta.viz.lib.format-number/fmt-nodigits}
                        {:path :volume}]}
             ds)
