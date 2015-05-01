(ns puresec-master-clojure.service.health
  (:require [puresec-master-clojure.service.detector :as detector-service]
            [puresec-master-clojure.service.trigger :as trigger-service]
            [clj-http.client :as client]
            [schejulure.core :as cron]))

(def detector-health-cache (atom {}))
(def trigger-health-cache (atom {}))

(defn get-health [slave]
  (let [url (:url slave)]
    (try
      (= 200 (:status (client/get (str url "/health"))))
      (catch Exception e
        false))))

(defn check-health-and-update-cache [slaves cache]
  (swap! cache
    (fn [_] (into {} (map (fn [s] {(:id s) (get-health s)}) slaves)))))

(defn check-health []
  (println "check health now ..")
  (check-health-and-update-cache (detector-service/get-detectors) detector-health-cache)
  (check-health-and-update-cache (trigger-service/get-triggers) trigger-health-cache))

(defn get-detector-health []
  @detector-health-cache)

(defn get-trigger-health []
  @trigger-health-cache)

(def health-check-scheduler
  (cron/schedule {:minute (range 0 60 5)} check-health))
