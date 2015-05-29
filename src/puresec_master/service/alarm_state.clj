(ns puresec-master.service.alarm-state)

(def handler-fired (atom {}))

(defn add-fired [detector_id]
  (swap! handler-fired (fn [fired] (assoc fired detector_id true))))

(defn enhance-handler-with-state [handler]
  (let [fired (.get @handler-fired (:id handler))]
    (assoc handler :fired (if fired
                            fired
                            false))))

(defn enhance-handlers-with-state [handlers]
  (map
    (fn [handler] (enhance-handler-with-state handler))
    handlers))
