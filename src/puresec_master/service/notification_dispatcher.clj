(ns puresec-master.service.notification-dispatcher
  (:require [puresec-master.db.core :as db]
            [clj-http.client :refer [post]]
            [clojure.tools.logging :as log]
            [puresec-master.service.settings :as settings]))

(defn notify-handler [handler detector]
  (let [url-handler (str (:url handler) "/notify")]
    (log/info "dispatching signal to " handler)
    (post url-handler {:form-params {:detector_name (:detector_name detector)
                                     :detector_description (:detector_description detector)}}))
  (:id handler))

(defn dispatch-alarm-notification [detector_id]
  (let [detector (first (db/load-detector-by-id {:detector_id detector_id}))
        handlers (db/load-matching-handlers {:detector_id detector_id})]
    (if (settings/is-alarm-enabled)
      (do
        (log/info "alarm signal received " detector)
        (map (fn [handler] (notify-handler handler detector)) handlers))
      [])))
