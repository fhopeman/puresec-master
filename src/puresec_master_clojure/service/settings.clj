(ns puresec-master-clojure.service.settings
  (:require [puresec-master-clojure.db.core :as db]))

(defn map-trigger [detector-id trigger-id]
  ;; dont use detector-id and trigger-id as map keys, there is a bug in the query translation code ..
  (if (= 0 (count (db/load-trigger-mapping {:detector_id detector-id :trigger_id trigger-id})))
    (= 1 (db/save-trigger-mapping! {:detector_id detector-id :trigger_id trigger-id}))
    false))

(defn unmap-trigger [detector-id trigger-id]
  (= 1 (db/remove-trigger-mapping! {:detector_id detector-id :trigger_id trigger-id})))

(defn in? [seq elm]
  (not
    (= nil (some #(= elm %) seq))))

(defn get-trigger-mapping []
  (map (fn [detector]
         (let [matching-triggers (db/load-matching-triggers {:detector_id (:id detector)})
               triggers (db/load-triggers)]
           (assoc detector :triggers
                           (map (fn [trigger]
                                  (assoc trigger :mapped (in? matching-triggers trigger)))
                                triggers))))
       (db/load-detectors)))
