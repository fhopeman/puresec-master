(ns puresec-master-clojure.routes.alarm
  (:require [puresec-master-clojure.layout :as layout]
            [compojure.core :refer [defroutes GET POST context]]
            [ring.util.response :refer [response status redirect content-type]]
            [puresec-master-clojure.service.detector :as detector-service]
            [puresec-master-clojure.service.trigger :as trigger-service]
            [puresec-master-clojure.utils.response :as response-utils]))

;(defn api-register-slave! [request fn-register-slave]
;  (let [name (:name (:params request))
;        description (:description (:params request))]
;    (if (and name description)
;      (response (fn-register-slave name description))
;      (status (response (response-utils/create-error-result)) 400))))

(defn api-register-detector! [request]
  (let [name (:name (:params request))
        description (:description (:params request))]
    (if (and name description)
      (response (detector-service/register-detector name description))
      (status (response (response-utils/create-error-result)) 400))))

(defn api-register-trigger! [request]
  (let [name (:name (:params request))
        description (:description (:params request))]
    (if (and name description)
      (response (trigger-service/register-trigger name description))
      (status (response (response-utils/create-error-result)) 400))))

(defroutes alarm-routes
  (context "/alarm" []
    (GET  "/home" [] (layout/render "home.html" {:detectors (detector-service/get-detectors)}))
    (POST "/register/detector" request (api-register-detector! request))
    (POST "/register/trigger" request (api-register-trigger! request))
    ;(POST "/trigger" request (api-fire-alarm request))
    ))
