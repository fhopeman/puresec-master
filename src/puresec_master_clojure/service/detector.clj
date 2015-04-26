(ns puresec-master-clojure.service.detector
  (:require [puresec-master-clojure.db.core :as db]
            [puresec-master-clojure.service.slave :as slave-service]))

(def detector-cache (atom nil))

(defn update-detector-cache []
  (swap! detector-cache (fn [_] (slave-service/get-slaves db/load-detectors))))

(defn register-detector [name description url]
  "registers a detector. If a detector with this name is already registered, the existing id is returned"
  (update-detector-cache)
  (slave-service/register-slave name description url db/load-detector db/save-detector!))

(defn get-detectors []
  "loads all detectors"
  (if @detector-cache
    @detector-cache
    (update-detector-cache)))
