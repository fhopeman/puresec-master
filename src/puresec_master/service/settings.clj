(ns puresec-master.service.settings
  (:require [puresec-master.db.core :as db]
            [clojure.tools.logging :as log]))

(def alarm-enabled (atom false))
(def handler-mapping-cache (atom nil))

(defn enable-alarm []
  (log/info "enabling the system")
  (swap! alarm-enabled (fn [_] true)))

(defn disable-alarm []
  (log/info "disabling the system")
  (swap! alarm-enabled (fn [_] false)))

(defn is-alarm-enabled []
  @alarm-enabled)

(defn in? [seq elm]
  (not
    (= nil (some #(= elm %) seq))))

(defn update-handler-mapping-cache []
  (swap! handler-mapping-cache
         (fn [_] (map (fn [detector]
                        (let [matching-handlers (db/load-matching-handlers {:detector_id (:id detector)})
                              handlers (db/load-handlers)]
                          (assoc detector :handlers
                                          (map (fn [handler]
                                                 (assoc handler :mapped (in? matching-handlers handler)))
                                                handlers))))
                 (db/load-detectors)))))

(defn map-handler [detector-id handler-id]
  ;; dont use detector-id and handler-id as map keys, there is a bug with '-' in the query translation code ..
  (let [result (if (= 0 (count (db/load-handler-mapping {:detector_id detector-id :handler_id handler-id})))
                 (= 1 (db/save-handler-mapping! {:detector_id detector-id :handler_id handler-id}))
                 false)]
    (update-handler-mapping-cache)
    result))

(defn unmap-handler [detector-id handler-id]
  (let [result (= 1 (db/remove-handler-mapping! {:detector_id detector-id :handler_id handler-id}))]
    (update-handler-mapping-cache)
    result))

(defn get-handler-mapping []
  (if @handler-mapping-cache
    @handler-mapping-cache
    (update-handler-mapping-cache)))
