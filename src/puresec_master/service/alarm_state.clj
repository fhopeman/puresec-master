(ns puresec-master.service.alarm-state)

(def detector-fired (atom {}))

(defn add-fired [detector_id]
  (swap! detector-fired (fn [fired] (assoc fired detector_id true))))

(defn enhance-detector-with-state [detector]
  (let [fired (.get @detector-fired (:id detector))]
    (assoc detector :fired (if fired
                            fired
                            false))))

(defn enhance-detectors-with-state [detectors]
  (map
    (fn [detector] (enhance-detector-with-state detector))
    detectors))
