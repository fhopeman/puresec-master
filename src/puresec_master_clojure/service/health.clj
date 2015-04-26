(ns puresec-master-clojure.service.health
  (:require [puresec-master-clojure.service.detector :as detector-service]
            [puresec-master-clojure.service.trigger :as trigger-service]
            [clj-http.client :as client]))

(def detector-health-cache (atom {}))
(def trigger-health-cache (atom {}))

(defn get-status [slave]
  (let [url (:url slave)]
    (try
      (= 200 (:status (client/get (str url "/health"))))
      (catch Exception e
        false))))

(defn check-slave-health [slaves cache]
  (swap! cache
    (fn [_] (into {} (map (fn [s] {(:id s) (get-status s)}) slaves)))))

(defn get-detector-health []
  @detector-health-cache)

(defn get-trigger-health []
  @trigger-health-cache)
