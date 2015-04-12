(ns puresec-master-clojure.service.detector
  (:require [puresec-master-clojure.db.core :as db]
            [puresec-master-clojure.service.slave :as slave-service]))

(defn register-detector [name description]
  "registers a detector. If a detector with this name is already registered, the existing id is returned"
  (slave-service/register-slave name description db/load-detector db/save-detector!))

(defn get-detectors []
  "loads all detectors"
  (slave-service/get-slaves db/load-detectors))
