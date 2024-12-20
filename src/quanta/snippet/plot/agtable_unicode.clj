(ns quanta.snippet.plot.agtable-unicode
  (:require
   [reval.core :refer [*env*]]
   [quanta.snippet.data.random-bars :refer [random-bar-ds]]
   [quanta.dali.plot :as plot]
   [tablecloth.api :as tc]))

(def ds
  (-> (random-bar-ds 10)
      (tc/add-column :action ["😀"
                              ""
                              ""
                              "★"
                              ""
                              ""
                              "❂"
                              ""
                              ""
                              ""])))

ds

(def opts
  {:style {;:width "800px" :height "600px"
           :width "800px" :height "600px"
           ;:width "100%" :height "100%"
           }
   :timezone "America/Panama"
   :columns [{:field :date}
             {:field :open}
             {:field :action}]})

(plot/aggrid-ds *env* opts ds)



