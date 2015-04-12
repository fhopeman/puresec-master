(ns puresec-master-clojure.service.slave
  (:require [puresec-master-clojure.utils.response :as response-utils]))

(defn get-slave-if-exists [name fn-load-slave]
  (first (fn-load-slave {:name name})))

(defn register-slave [name description fn-load-slave fn-save-slave]
  "registers a slave of specific type. If a slave with this name is already registered, the existing id is returned"
  (let [existing-slave (get-slave-if-exists name fn-load-slave)]
    (if existing-slave
      (response-utils/create-successful-result (:id existing-slave))
      (if (= 1 (fn-save-slave {:name name :description description}))
        (response-utils/create-successful-result (:id (first (fn-load-slave {:name name}))))
        (response-utils/create-error-result)))))

(defn get-slaves [fn-load-slaves]
  "loads all slaves of specific type"
  (fn-load-slaves))
