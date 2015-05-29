(ns puresec-master.service.alarm-state)

(def detectors-fired (atom {}))

(defn add-fired [detector_id]
  (swap! detectors-fired (fn [fired] (assoc fired detector_id true))))

(defn enhance-detector-with-state [detector]
  (let [fired (.get @detectors-fired (:id detector))]
    (assoc detector :fired (if fired
                            fired
                            false))))

(defn enhance-detectors-with-state [detectors]
  (map
    (fn [detector] (enhance-detector-with-state detector))
    detectors))

(defn clear-state []
  (reset! detectors-fired {}))
