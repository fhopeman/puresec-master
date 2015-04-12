(ns puresec-master-clojure.service.detection
  (:require [puresec-master-clojure.db.core :as db]))

(defn create-successful-result [id]
  {:state "SUCCESS" :id id})

(defn get-slave-if-exists [zone_name]
  (first (db/load-slave-detect {:zone_name zone_name})))

(defn register-detection-slave [zone_name zone_description]
  "registers a alarm detection slave. If a slave with this name is already registered, the existing id is returned"
  (let [existing-slave (get-slave-if-exists zone_name)]
    (if existing-slave
      (create-successful-result (:id existing-slave))
      (if (db/register-slave-detect! {:zone_name zone_name :zone_description zone_description})
        (create-successful-result (:id (first (db/load-slave-detect {:zone_name zone_name}))))
        {:state "ERROR"}))))
