(ns puresec-master.service.health
  (:require [puresec-master.service.detector :as detector-service]
            [puresec-master.service.handler :as handler-service]
            [clj-http.client :as client]
            [clojure.tools.logging :as log]
            [environ.core :refer [env]]
            [schejulure.core :as cron]))

(def detector-health-cache (atom {}))
(def handler-health-cache (atom {}))

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
  (check-health-and-update-cache (handler-service/get-handlers) handler-health-cache))

(defn enhance-slaves-with-health [slaves cache]
  (map
    (fn [slave] (assoc slave :healthy (.get @cache (:id slave))))
    slaves))

(defn enhance-detectors-with-health [detectors]
  (enhance-slaves-with-health detectors detector-health-cache))

(defn enhance-handlers-with-health [handlers]
  (enhance-slaves-with-health handlers handler-health-cache))

(def health-check-scheduler
  (if (or (env :prod) (env :dev))
    (do
      (log/info "starting up health check scheduler")
      (cron/schedule {:minute (range 0 60 1)} check-health))))
