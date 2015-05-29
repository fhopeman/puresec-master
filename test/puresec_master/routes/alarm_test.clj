(ns puresec-master.routes.alarm_test
  (:use clojure.test
        ring.mock.request
        puresec-master.handler)
  (:require [puresec-master.service.settings :as settings]
            [puresec-master.service.detector :as detector-service]
            [puresec-master.service.handler :as handler-service]
            [puresec-master.service.notification-dispatcher :as dispatcher]
            [puresec-master.service.alarm-state :as alarm-state]))

(deftest test-alarm-home-page
  (testing "that request to alarm home page returns 200"
    (with-redefs [detector-service/get-detectors (fn [] [])
                  handler-service/get-handlers (fn [] [])]
      (let [response (app (request :get "/alarm/home"))]
        (is (= 200 (:status response)))))))

(deftest test-api-register-detector
  (testing "that call to the detector registration api works"
    (with-redefs [detector-service/register-detector (fn [_ _ _] true)
                  settings/update-handler-mapping-cache (fn [] [])]
      (let [response (app (request :post "/alarm/register/detector" {:name "some name" :description "some descr" :url "http://someUrl"}))]
        (is (= 200 (:status response)))
        (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type"))))))

  (testing "that call to the detector registration api returns error if description is missing"
    (with-redefs [detector-service/register-detector (fn [_ _ _] true)
                  settings/update-handler-mapping-cache (fn [] [])]
      (let [response (app (request :post "/alarm/register/detector" {:name "some name" :url "http://someUrl"}))]
        (is (= 400 (:status response)))
        (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type"))))))

  (testing "that call to the detector registration api returns error if name is missing"
    (with-redefs [detector-service/register-detector (fn [_ _ _] true)
                  settings/update-handler-mapping-cache (fn [] [])]
      (let [response (app (request :post "/alarm/register/detector" {:description "some descr" :url "http://someUrl"}))]
        (is (= 400 (:status response)))
        (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type"))))))

  (testing "that call to the detector registration api returns error if url is missing"
    (with-redefs [detector-service/register-detector (fn [_ _ _] true)
                  settings/update-handler-mapping-cache (fn [] [])]
      (let [response (app (request :post "/alarm/register/detector" {:name "some name" :description "some descr"}))]
        (is (= 400 (:status response)))
        (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type")))))))

(deftest test-api-register-handler
  (testing "that call to the handler registration api works"
    (with-redefs [handler-service/register-handler (fn [_ _ _] true)
                  settings/update-handler-mapping-cache (fn [] [])]
      (let [response (app (request :post "/alarm/register/handler" {:name "some name" :description "some descr" :url "http://someUrl"}))]
        (is (= 200 (:status response)))
        (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type"))))))

  (testing "that call to the handler registration api returns error if name, description and url are missing"
    (with-redefs [handler-service/register-handler (fn [_ _ _] true)
                  settings/update-handler-mapping-cache (fn [] [])]
      (let [response (app (request :post "/alarm/register/handler"))]
        (is (= 400 (:status response)))
        (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type")))))))

(deftest test-api-notify-alarm
  (testing "that call to alarm notification api works"
    (with-redefs [dispatcher/dispatch-alarm-notification (fn [_] true)
                  alarm-state/add-fired (fn [_] nil)
                  settings/is-alarm-enabled (fn [] true)]
      (let [response (app (request :post "/alarm/notify" {:detector_id 7}))]
        (is (= 200 (:status response)))
        (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type"))))))

  (testing "that call to alarm notification api doesn't work if detector_id is missing"
    (with-redefs [dispatcher/dispatch-alarm-notification (fn [_] true)
                  alarm-state/add-fired (fn [_] nil)
                  settings/is-alarm-enabled (fn [] true)]
      (let [response (app (request :post "/alarm/notify"))]
        (is (= 400 (:status response)))
        (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type"))))))

  (testing "that an alarm notification saves the fired state of the detector if the system is enabled"
    (reset! alarm-state/detectors-fired {})
    (with-redefs [dispatcher/dispatch-alarm-notification (fn [_] true)
                  settings/is-alarm-enabled (fn [] true)]
      (app (request :post "/alarm/notify" {:detector_id 11}))
      (is (= {11 true} @alarm-state/detectors-fired))))

  (testing "that an alarm notification doesn't save the fired state of the detector if the system is disabled"
    (reset! alarm-state/detectors-fired {})
    (with-redefs [dispatcher/dispatch-alarm-notification (fn [_] true)
                  settings/is-alarm-enabled (fn [] false)]
      (app (request :post "/alarm/notify" {:detector_id 9}))
      (is (= {} @alarm-state/detectors-fired)))))
