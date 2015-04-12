(ns puresec-master-clojure.service.detector
  (:require [puresec-master-clojure.db.core :as db]
            [puresec-master-clojure.utils.response :as response-utils]))

(defn get-detector-if-exists [name]
  (first (db/load-detector {:name name})))

(defn register-detector [name description]
  "registers a detector. If a detector with this name is already registered, the existing id is returned"
  (let [existing-detector (get-detector-if-exists name)]
    (if existing-detector
      (response-utils/create-successful-result (:id existing-detector))
      (if (db/save-detector! {:name name :description description})
        (response-utils/create-successful-result (:id (first (db/load-detector {:name name}))))
        (response-utils/create-error-result)))))

(defn get-detectors []
  "loads all detectors"
  (db/load-detectors))
