(ns puresec-master-clojure.service.notification-dispatcher
  (:require [puresec-master-clojure.db.core :as db]
            [clj-http.client :refer [post]]))

(defn notify-trigger [trigger]
  (post (str (:url trigger) "/trigger") {:form-params {:foo "bar"}})
  (:id trigger))

(defn dispatch-alarm-notification [detector_id]
  (let [triggers (db/load-matching-trigger {:detector_id detector_id})]
    (map (fn [trigger] (notify-trigger trigger)) triggers)))
