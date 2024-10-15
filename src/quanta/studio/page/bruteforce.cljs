(ns quanta.studio.page.bruteforce
  (:require
   [reagent.core :as r]
   [promesa.core :as p]
   [spaces.core]
   [rtable.rtable]
   [options.core :as o]
   [frontend.notification :refer [show-notification]]
   [goldly.service.core :refer [clj]]
   [ajax.promise :refer [GET]]
   [clojure.edn :refer [read-string]]
   [quanta.studio.view.bruteforce :refer [bruteforce-result-ui bruteforce-roundtrips]]))

(defn load-label [label result-a]
  (let [url (str "/r/bruteforce/" label ".edn")
        _ (println "loading url: " url)
        get-p (GET url)]
    (-> get-p
        (p/then (fn [txt]
                  (println "load-label success ") ;  txt
                  (try
                    (reset! result-a (read-string txt))
                    ;(reset! result-a txt)
                    (catch js/Exception ex
                      (println "parse-exception: " ex)))))
        (p/catch (fn [err]
                   (println "error: could not load result for label: " label "url: " url " err: " err)
                   (reset! result-a nil))))))

(defn get-labels [labels-a]
  (let [rp (clj 'quanta.studio.bruteforce/show-available)]
    (-> rp
        (p/then (fn [labels]
                  (println "get-labels success ")  ; labels
                  (reset! labels-a (into [] labels))))
        (p/catch (fn [err]
                   (println "get-labels error: " err)
                   (show-notification :error  "could not get labels summary!"))))
    nil))

(def table-opts
  {:class "table-head-fixed padding-sm table-blue table-striped table-hover"
   :style {:width "100vw"
           :height "100vh"
           :border "3px solid green"}})

(defn keyword->spec [kw]
  {:id  kw
   :name (str kw)})

(defn keywords->spec [kws]
  (map keyword->spec kws))

(defn template-spec [template-list]
  (let [template-spec  (keywords->spec template-list)]
    (into []
          (concat [{:id :no-label :name ""}]
                  template-spec))))

(defn header [{:keys [options-a labels-a] :as state}]
  [o/options-ui {:class "bg-blue-300 options-label-left" ; options-debug
                 :style {:width "100%"
                         :height "40px"}}
   {:state options-a
    :options  [{:type :select
                :path :label
                :name "Label"
                :spec (template-spec @labels-a)}]
    :current {:label nil}}])

(defn bruteforce-ui []
  (let [state {:labels-a (r/atom [])
               :options-a (r/atom {:label nil})
               :result-a (r/atom nil)
               :id-a (r/atom nil)}
        set-id (fn [id]
                 (when-not (= id @(:id-a state))
                   (reset! (:id-a state) id)))]
    (get-labels (:labels-a state))
    (add-watch (:options-a state) :watcher
               (fn [key atom old-state new-state]
                 (prn "-- Atom Changed --")
                 (prn "key" key)
                 (prn "atom" atom)
                 (prn "old-state" old-state)
                 (prn "new-state" new-state)
                 (when-let [label (:label new-state)]
                   (println "requesting result for : " label)
                   (load-label label (:result-a state)))))
    (fn []
      [spaces.core/viewport
       [spaces.core/top-resizeable {:size 50}
        [header state]]
       [spaces.core/fill
        [spaces.core/left-resizeable {:size 500}
         #_[:div.bg-red-500.p-5.m-5
            [:p (pr-str @(:labels-a state))
             (pr-str @(:options-a state))
             (pr-str @(:result-a state))
             (pr-str (:template-id @(:result-a state)))]]
         [bruteforce-result-ui @(:result-a state) set-id]]
        [spaces.core/fill
         [bruteforce-roundtrips (-> state :options-a deref :label)
          (-> state :id-a deref)]]]])))

(defn page [_route]
  [bruteforce-ui])

(def data [:a 1 :b "xxx"])

(def ddd (pr-str data))

(println "edn as such: " ddd)

(println "parsed edn: " (read-string ddd))
