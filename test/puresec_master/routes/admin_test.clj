(ns puresec-master.routes.admin-test
  (:use clojure.test
        ring.mock.request
        puresec-master.handler)
  (:require [puresec-master.service.settings :as settings-service]
            [puresec-master.service.detector :as detector-service]
            [puresec-master.service.handler :as handler-service]
            [puresec-master.service.settings :as settings]))

(deftest test-api-map-handler
  (testing "that call to the handler map api works"
    (with-redefs [settings-service/map-handler (fn [_ _] true)]
      (let [response (app (request :post "/admin/notification/map" {:detector_id 7 :handler_id 9}))]
        (is (= 200 (:status response)))
          (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type")))))))

(deftest test-api-unmap-handler
  (testing "that call to the handler unmap api works"
    (with-redefs [settings-service/unmap-handler (fn [_ _] true)]
      (let [response (app (request :post "/admin/notification/unmap" {:detector_id 11 :handler_id 13}))]
        (is (= 200 (:status response)))
        (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type")))))))

(deftest test-api-switch-alarm-state-to
  (testing "that the api to enable the alarm system works"
    (with-redefs [settings/enable-alarm (fn [] true)]
      (let [response (app (request :post "/admin/enable"))]
        (is (= 200 (:status response))))))

  (testing "that the api to disable the alarm system works"
    (with-redefs [settings/disable-alarm (fn [] false)]
      (let [response (app (request :post "/admin/disable"))]
        (is (= 200 (:status response)))))))

(deftest test-settings-page
  (testing "that call to settings page works"
    (with-redefs [detector-service/get-detectors (fn [] [])
                  handler-service/get-handlers (fn [] [])]
      (let [response (app (request :get "/admin/settings"))]
        (is (= 200 (:status response)))))))
