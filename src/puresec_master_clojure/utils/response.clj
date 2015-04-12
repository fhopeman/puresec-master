(ns puresec-master-clojure.utils.response)

(defn create-successful-result [id]
  {:state "SUCCESS" :id id})

(defn create-error-result []
  {:state "ERROR"})
