(ns quanta.studio.dev.page.viewer.vegads
  (:require
   [promesa.core :as p]
   [reagent.core :as r]
   [tech.v3.dataset :as tds]
   [cquant.tmlds :refer [GET]]
   [rtable.viewer.vega :refer [vegalite]]))

(defn load-dataset [url]
  (println "loading dataset from url: " url)
  (let [load-promise (GET url)]
    (-> load-promise
        (p/then (fn [ds]
                  (println "ds from url " url " loaded successfully. rows: " (tds/row-count ds) "cols: " (tds/column-names ds))
                  ds))
        (p/catch (fn [err]
                   (println "could not load ds from url " url " err: " err))))
    ; give back the original promise
    load-promise))

(defonce ds-a (r/atom nil))

(def data
  {:table [{:a "A" :b 28} {:a "B" :b 55} {:a "C" :b 43} {:a "D" :b 91}
           {:a "E" :b 81} {:a "F" :b 53} {:a "G" :b 19} {:a "H" :b 87}
           {:a "I" :b 52} {:a "J" :b 127}]})

(defn transform [ds]
  (->> (tds/rows ds)
       (into [])))

(-> (load-dataset "/r/data/LWhgL6-roundtrips.transit-json")
    (p/then (fn [ds]
              (reset! ds-a {:table (transform ds)}))))

(def bar
  {;:$schema "https://vega.github.io/schema/vega-lite/v4.json"
   :description "A simple bar chart with embedded data."
   :height "800"
   :width "1200"
   :vconcat [{:height 700 ; js/null  ; Allows the line chart to take all available space
              :width "1200" ; Makes the line chart full width
              :mark {:type "line"
                         ;:tooltip true
                     :tooltip {:content "data"}}
              :encoding {:x {:field "entry-date" :type "temporal"}
                         :y {:field "nav" :type "quantitative"}}}
             {:height "100"
              :width "1200" ; Makes the line chart full width
              :mark {:type "bar"
                     :tooltip {:content "data"}}
              :encoding {:x {:field "entry-date" :type "temporal"}
                         :y {:field "nav" :type "quantitative"}}}]

   :data {:name "table"}})

(defn vega-ds []
  (fn []
    (if @ds-a
      [vegalite {:spec bar :data @ds-a}]
      [:p "loading data.."])))

(defn page [_route]
  [:div.h-screen.w-screen.bg-red-500
   [vega-ds]])