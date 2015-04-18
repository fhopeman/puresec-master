(ns puresec-master-clojure.routes.admin
  (:require [compojure.core :refer [defroutes GET POST context]]
            [ring.util.response :refer [response status redirect content-type]]
            [puresec-master-clojure.service.settings :as settings]
            [puresec-master-clojure.utils.response :as response-util]))

(defn api-map-trigger [request]
  (let [detector-id (:detector_id (:params request))
        trigger-id (:trigger_id (:params request))]
    (settings/map-trigger detector-id trigger-id)
    (response (response-util/create-successful-result))))

(defroutes admin-routes
  (context "/admin" []
    (POST "/trigger/map" request (api-map-trigger request))))