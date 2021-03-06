(ns puresec-master.service.detector
  (:require [puresec-master.db.core :as db]
            [puresec-master.service.slave :as slave-service]
            [clojure.tools.logging :as log])
  (:import (java.sql BatchUpdateException)))

(def detector-cache (atom nil))

(defn update-detector-cache []
  (swap! detector-cache (fn [_] (slave-service/get-slaves db/load-detectors))))

(defn register-detector [name description url]
  "registers a detector. If a detector with this name is already registered, the existing id is returned"
  (let [response (slave-service/register-slave name description url db/load-detector db/save-detector!)]
    (update-detector-cache)
    response))

(defn remove-detector [id]
  (try
    (let [response (db/delete-detector! {:detector_id id})]
      (update-detector-cache)
      (= 1 response))
  (catch BatchUpdateException e
    (log/error "error while deleting detector" e)
    false)))

(defn get-detectors []
  "loads all detectors"
  (if @detector-cache
    @detector-cache
    (update-detector-cache)))
