(ns puresec-master.routes.alarm_test
  (:use clojure.test
        ring.mock.request
        puresec-master.handler
        puresec-master.routes.alarm
        puresec-master.service.detector
        puresec-master.service.handler
        puresec-master.service.notification-dispatcher))

(deftest test-alarm-home-page
  (testing "that request to alarm home page returns 200"
    (with-redefs [get-detectors (fn [] [])
                  get-handlers (fn [] [])]
      (let [response (app (request :get "/alarm/home"))]
        (is (= 200 (:status response)))))))

(deftest test-api-register-detector
  (testing "that call to the detector registration api works"
    (with-redefs [register-detector (fn [_ _ _] true)]
      (let [response (app (request :post "/alarm/register/detector" {:name "some name" :description "some descr" :url "http://someUrl"}))]
        (is (= 200 (:status response)))
        (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type"))))))

  (testing "that call to the detector registration api returns error if description is missing"
    (with-redefs [register-detector (fn [_ _ _] true)]
      (let [response (app (request :post "/alarm/register/detector" {:name "some name" :url "http://someUrl"}))]
        (is (= 400 (:status response)))
        (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type"))))))

  (testing "that call to the detector registration api returns error if name is missing"
    (with-redefs [register-detector (fn [_ _ _] true)]
      (let [response (app (request :post "/alarm/register/detector" {:description "some descr" :url "http://someUrl"}))]
        (is (= 400 (:status response)))
        (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type"))))))

  (testing "that call to the detector registration api returns error if url is missing"
    (with-redefs [register-detector (fn [_ _ _] true)]
      (let [response (app (request :post "/alarm/register/detector" {:name "some name" :description "some descr"}))]
        (is (= 400 (:status response)))
        (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type")))))))

(deftest test-api-register-handler
  (testing "that call to the handler registration api works"
    (with-redefs [register-handler (fn [_ _ _] true)]
      (let [response (app (request :post "/alarm/register/handler" {:name "some name" :description "some descr" :url "http://someUrl"}))]
        (is (= 200 (:status response)))
        (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type"))))))

  (testing "that call to the handler registration api returns error if name, description and url are missing"
    (with-redefs [register-handler (fn [_ _ _] true)]
      (let [response (app (request :post "/alarm/register/handler"))]
        (is (= 400 (:status response)))
        (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type")))))))

(deftest test-api-notify-alarm
  (testing "that call to alarm notification api works"
    (with-redefs [dispatch-alarm-notification (fn [_] true)]
      (let [response (app (request :post "/alarm/notify" {:detector_id 7}))]
        (is (= 200 (:status response)))
        (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type"))))))

  (testing "that call to alarm notification api doesn't work if detector_id is missing"
    (with-redefs [dispatch-alarm-notification (fn [_] true)]
      (let [response (app (request :post "/alarm/notify"))]
        (is (= 400 (:status response)))
        (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type")))))))