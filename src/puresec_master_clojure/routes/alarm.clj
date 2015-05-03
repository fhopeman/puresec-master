(ns puresec-master-clojure.routes.alarm
  (:require [puresec-master-clojure.layout :as layout]
            [compojure.core :refer [defroutes GET POST context]]
            [ring.util.response :refer [response status redirect content-type]]
            [puresec-master-clojure.service.detector :as detector-service]
            [puresec-master-clojure.service.trigger :as trigger-service]
            [puresec-master-clojure.utils.response :as response-utils]
            [puresec-master-clojure.service.health :as health-service]
            [puresec-master-clojure.service.notification-dispatcher :as dispatcher]
            [puresec-master-clojure.service.settings :as settings]))

(defn api-register-slave! [request fn-register-slave]
  (let [name (:name (:params request))
        description (:description (:params request))
        url (:url (:params request))]
    (if (and name (and description url))
      (response (fn-register-slave name description url))
      (status (response (response-utils/create-error-result "missing parameter name, description or url")) 400))))

(defn api-notify-alarm [request]
  (let [detector_id (:detector_id (:params request))]
    (if detector_id
      (response (response-utils/create-successful-result (dispatcher/dispatch-alarm-notification detector_id)))
      (status (response (response-utils/create-error-result "missing parameter detector_id")), 400))))

(defroutes alarm-routes
  (context "/alarm" []
    (GET  "/home" [] (layout/render "home.html" {:detectors (health-service/enhance-detectors-with-health (detector-service/get-detectors))
                                                 :triggers  (health-service/enhance-triggers-with-health (trigger-service/get-triggers))
                                                 :system    {:enabled (settings/is-alarm-enabled)}}))
    (POST "/register/detector" request (api-register-slave! request detector-service/register-detector))
    (POST "/register/handler" request (api-register-slave! request trigger-service/register-trigger))
    (POST "/notify" request (api-notify-alarm request))))
