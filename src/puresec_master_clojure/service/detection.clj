(ns puresec-master-clojure.service.detection
  (:require [puresec-master-clojure.db.core :as db]))


(defn register-detection-slave
  "registers a alarm detection slave. If a slave with this name is already registered, the existing id is returned"
  [zone_name zone_description]
  (if (db/register-slave-detect! {:zone_name zone_name :zone_description zone_description})
    {:state "SUCCESS" :id (:id (first (db/load-slave-detect {:zone_name zone_name})))}
    {:state "ERROR"}))
