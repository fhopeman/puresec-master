(ns puresec-master-clojure.service.trigger
  (:require [puresec-master-clojure.db.core :as db]
            [puresec-master-clojure.utils.response :as response-utils]))

(defn get-trigger-if-exists [name]
  (first (db/load-trigger {:name name})))

(defn register-trigger [name description]
  "registers a trigger. If a slave with this name is already registered, the existing id is returned"
  (let [existing-trigger (get-trigger-if-exists name)]
    (if existing-trigger
      (response-utils/create-successful-result (:id existing-trigger))
      (if (db/save-trigger! {:name name :description description})
        (response-utils/create-successful-result (:id (first (db/load-trigger {:name name}))))
        (response-utils/create-error-result)))))

(defn get-triggers []
  "loads all triggers"
  (db/load-triggers))
