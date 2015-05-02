(ns puresec-master-clojure.service.notification-dispatcher
  (:require [puresec-master-clojure.db.core :as db]
            [clj-http.client :refer [post]]
            [clojure.tools.logging :as log]
            [puresec-master-clojure.service.settings :as settings]))

(defn notify-trigger [trigger detector]
  (let [url-trigger (str (:url trigger) "/notify")]
    (log/info "dispatching signal to " trigger)
    (post url-trigger {:form-params {:detector_name (:detector_name detector)
                                     :detector_description (:detector_description detector)}}))
  (:id trigger))

(defn dispatch-alarm-notification [detector_id]
  (let [detector (first (db/load-detector-by-id {:detector_id detector_id}))
        triggers (db/load-matching-triggers {:detector_id detector_id})]
    (if (settings/is-alarm-enabled)
      (do
        (log/info "alarm signal received " detector)
        (map (fn [trigger] (notify-trigger trigger detector)) triggers))
      [])))
