(ns quanta.notebook.viz
  (:require
   [tablecloth.api :as tc]
   [tick.core :as t]
   [quanta.viz.plot :as plot]))

(def now (t/instant))

(def ds
  (tc/dataset [{:date now :open 1 :high 2 :low 3 :close 4 :volume 5}
               {:date now :open 1 :high 2 :low 3 :close 4 :volume 5}
               {:date now :open 1 :high 2 :low 3 :close 4 :volume 5}
               {:date now :open 1 :high 2 :low 3 :close 4 :volume 5}]))

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
              :columns [{:path :date :max-width "60px"}
                        {:path :close}
                        :volume]}
             ds)
