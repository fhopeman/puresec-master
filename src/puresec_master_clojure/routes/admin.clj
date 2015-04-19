(ns puresec-master-clojure.routes.admin
  (:require [compojure.core :refer [defroutes GET POST context]]
            [ring.util.response :refer [response status redirect content-type]]
            [puresec-master-clojure.service.settings :as settings]
            [puresec-master-clojure.utils.response :as response-util]
            [puresec-master-clojure.layout :as layout]))

(defn api-map-trigger [request]
  (let [detector-id (:detector_id (:params request))
        trigger-id (:trigger_id (:params request))]
    (if (settings/map-trigger detector-id trigger-id)
      (response (response-util/create-successful-result))
      (response (response-util/create-error-result "mapping already exists")))))

(defroutes admin-routes
  (context "/admin" []
    (GET  "/settings" [] (layout/render "settings.html" {}))
    (POST "/notification/map" request (api-map-trigger request))))
