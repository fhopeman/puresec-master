(ns puresec-master.routes.home
  (:require [compojure.core :refer [defroutes GET]]
            [ring.util.response :refer [redirect]]))

(defn page-home []
  (redirect "/alarm/home"))

(defroutes home-routes
    (GET "/" [] (page-home)))
