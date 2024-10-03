


(defn start-variations
  "starts template variations with the same mode"
  [this template-id mode variation-spec]
  (info "starting template: " template-id " mode: " mode " variations: " variation-spec)
  (let [template  (template-db/load-template this template-id)
        template-seq (qtempl/create-template-variations template variation-spec)]
    (doall (map #(start-template this % mode) template-seq))))
