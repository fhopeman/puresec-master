(ns puresec-master-clojure.routes.admin
  (:require [compojure.core :refer [defroutes GET POST context]]
            [ring.util.response :refer [response status redirect content-type]]
            [puresec-master-clojure.service.settings :as settings]
            [puresec-master-clojure.utils.response :as response-util]
            [puresec-master-clojure.layout :as layout]
            [puresec-master-clojure.service.notification-dispatcher :as dispatcher]))

(defn api-map-trigger [request]
  (let [detector-id (:detector_id (:params request))
        trigger-id (:trigger_id (:params request))]
    (if (settings/map-trigger detector-id trigger-id)
      (response (response-util/create-successful-result))
      (response (response-util/create-error-result "mapping already exists")))))

(defn api-unmap-trigger [request]
  (let [detector-id (:detector_id (:params request))
        trigger-id (:trigger_id (:params request))]
    (settings/unmap-trigger detector-id trigger-id)
    (response (response-util/create-successful-result))))

(defn api-switch-alarm-state [switch]
  (switch)
  (response-util/create-successful-result))

(defroutes admin-routes
  (context "/admin" []
    (GET  "/settings" [] (layout/render "settings.html" {:trigger_mappings (settings/get-trigger-mapping)}))
    (POST "/notification/map" request (api-map-trigger request))
    (POST "/notification/unmap" request (api-unmap-trigger request))
    (POST "/enable" request (api-switch-alarm-state settings/enable-alarm))
    (POST "/disable" request (api-switch-alarm-state settings/disable-alarm))))
