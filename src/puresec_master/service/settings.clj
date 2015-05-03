(ns puresec-master.service.settings
  (:require [puresec-master.db.core :as db]
            [clojure.tools.logging :as log]))

(def alarm-enabled (atom false))

(defn enable-alarm []
  (log/info "enabling the system")
  (swap! alarm-enabled (fn [_] true)))

(defn disable-alarm []
  (log/info "disabling the system")
  (swap! alarm-enabled (fn [_] false)))

(defn is-alarm-enabled []
  @alarm-enabled)

(defn map-handler [detector-id handler-id]
  ;; dont use detector-id and handler-id as map keys, there is a bug in the query translation code ..
  (if (= 0 (count (db/load-handler-mapping {:detector_id detector-id :handler_id handler-id})))
    (= 1 (db/save-handler-mapping! {:detector_id detector-id :handler_id handler-id}))
    false))

(defn unmap-handler [detector-id handler-id]
  (= 1 (db/remove-handler-mapping! {:detector_id detector-id :handler_id handler-id})))

(defn in? [seq elm]
  (not
    (= nil (some #(= elm %) seq))))

(defn get-handler-mapping []
  (map (fn [detector]
         (let [matching-handlers (db/load-matching-handlers {:detector_id (:id detector)})
               handlers (db/load-handlers)]
           (assoc detector :handlers
                           (map (fn [handler]
                                  (assoc handler :mapped (in? matching-handlers handler)))
                                handlers))))
       (db/load-detectors)))
