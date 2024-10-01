(ns quanta.dag.algo.spec)

(defn spec->op [spec]
  (let [{:keys [calendar formula value]} spec]
    (cond
      ; time-algo
      calendar
      {:calendar calendar
       :fn (:algo spec) 
       :opts (dissoc spec :algo)}
      ; formula (uses other cells)
      formula
      {:formula formula 
       :fn (:algo spec) 
       :opts (dissoc spec :algo)}
      ; value (create imput cell)
      value
      {:value value}
      ; bad spec syntax
      :else
      (throw (ex-info "unsupported cell-type" {:spec spec})))))

(defn spec->ops
  "returns ops or throws"
  [spec]
  (if (map? spec)
    ; convert map syntax to vector syntax
    (spec->ops [1 spec]) ; [[1 (spec->op env spec)]]
    ; process vector syntax
    (let [global-opts? (and (odd? (count spec))
                            (map? (first spec)))
          [global-opts spec] (if global-opts?
                               [(first spec) (rest spec)]
                               [{} spec])]
      ;(info "global-opts: " global-opts)
      (->> (reduce (fn [r [id spec]]
                     (let [spec (merge global-opts spec)
                           op (spec->op spec)]
                       ;(info "merged spec: " spec)
                       (conj r [id op])))
                   []
                   (partition 2 spec))
           ;(into [])
           ))))

