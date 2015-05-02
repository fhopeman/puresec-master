(ns puresec-master-clojure.service.health
  (:require [puresec-master-clojure.service.detector :as detector-service]
            [puresec-master-clojure.service.trigger :as trigger-service]
            [clj-http.client :as client]
            [schejulure.core :as cron]
            [clojure.tools.logging :as log]))

(def detector-health-cache (atom {}))
(def trigger-health-cache (atom {}))

(defn get-health [slave]
  (let [url (:url slave)]
    (try
      (log/debug "checking health of " url)
      (= 200 (:status (client/get (str url "/health"))))
      (catch Exception e
        (log/error "health check error " url e)
        false))))

(defn check-health-and-update-cache [slaves cache]
  (swap! cache
    (fn [_] (into {} (map (fn [s] {(:id s) (get-health s)}) slaves)))))

(defn check-health []
  (log/debug "start to check health of slaves ..")
  (check-health-and-update-cache (detector-service/get-detectors) detector-health-cache)
  (check-health-and-update-cache (trigger-service/get-triggers) trigger-health-cache))

(defn enhance-slaves-with-health [slaves cache]
  (map
    (fn [slave] (assoc slave :healthy (.get @cache (:id slave))))
    slaves))

(defn enhance-detectors-with-health [detectors]
  (enhance-slaves-with-health detectors detector-health-cache))

(defn enhance-triggers-with-health [triggers]
  (enhance-slaves-with-health triggers trigger-health-cache))

(def health-check-scheduler
  (cron/schedule {:minute (range 0 60 5)} check-health))
