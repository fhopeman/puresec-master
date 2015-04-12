(ns puresec-master-clojure.routes.alarm
  (:require [puresec-master-clojure.layout :as layout]
            [compojure.core :refer [defroutes GET POST context]]
            [ring.util.response :refer [response redirect content-type]]
            [puresec-master-clojure.service.detection :as detection-service]))

(defn api-register-detection-slave! [request]
  (let [zone_name (:zone_name (:params request))
        zone_description (:zone_description (:params request))]
    (response (detection-service/register-detection-slave zone_name zone_description))))

(defroutes alarm-routes
  (context "/alarm" []
    (GET  "/home" [] (layout/render "home.html" {:detection-slaves (detection-service/get-detection-slaves)}))
    (POST "/register" request (api-register-detection-slave! request))))
