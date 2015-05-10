(ns puresec-master.routes.admin
  (:require [compojure.core :refer [defroutes GET POST context]]
            [ring.util.response :refer [response status redirect content-type]]
            [puresec-master.service.settings :as settings]
            [puresec-master.utils.response :as response-util]
            [puresec-master.layout :as layout]
            [puresec-master.service.detector :as detector-service]
            [puresec-master.service.handler :as handler-service]))

(defn api-map-handler [request]
  (let [detector-id (:detector_id (:params request))
        handler-id (:handler_id (:params request))]
    (if (settings/map-handler detector-id handler-id)
      (response (response-util/create-successful-result))
      (response (response-util/create-error-result "mapping already exists")))))

(defn api-unmap-handler [request]
  (let [detector-id (:detector_id (:params request))
        handler-id (:handler_id (:params request))]
    (settings/unmap-handler detector-id handler-id)
    (response (response-util/create-successful-result))))

(defn api-switch-alarm-state [switch]
  (switch)
  (response (response-util/create-successful-result)))

(defn api-remove-slave! [request fn-remove-slave]
  (let [slave-id (:id (:params request))]
    (if (fn-remove-slave slave-id)
      (response (response-util/create-successful-result))
      (status (response (response-util/create-error-result "error while deleting slave")) 500))))

(defroutes admin-routes
  (context "/admin" []
    (GET  "/settings" [] (layout/render "settings.html" {:detectors (detector-service/get-detectors)
                                                         :handlers (handler-service/get-handlers)
                                                         :handler_mappings (settings/get-handler-mapping)}))
    (POST "/notification/map" request (api-map-handler request))
    (POST "/notification/unmap" request (api-unmap-handler request))
    (POST "/enable" request (api-switch-alarm-state settings/enable-alarm))
    (POST "/disable" request (api-switch-alarm-state settings/disable-alarm))
    (POST "/remove/detector" request (api-remove-slave! request detector-service/remove-detector))
    (POST "/remove/handler" request (api-remove-slave! request handler-service/remove-handler))))
