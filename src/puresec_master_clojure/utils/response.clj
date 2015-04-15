(ns puresec-master-clojure.utils.response)

(defn create-successful-result
  ([]
    {:state "SUCCESS"})
  ([id]
   (assoc (create-successful-result) :id id)))

(defn create-error-result
  ([]
    (create-error-result "unknown error"))
  ([message]
    {:state "ERROR" :message message}))
