(ns puresec-master-clojure.routes.alarm_test
  (:use clojure.test
        ring.mock.request
        puresec-master-clojure.handler
        puresec-master-clojure.routes.alarm
        puresec-master-clojure.service.detector
        puresec-master-clojure.service.trigger))

(deftest test-alarm-home-page
  (testing "that request to alarm home page returns 200"
    (let [response (app (request :get "/alarm/home"))]
      (is (= 200 (:status response))))))

(deftest test-api-register-detector
  (testing "that call to the detector registration api works"
    (with-redefs [register-detector (fn [_ _] true)]
      (let [response (app (request :post "/alarm/register/detector" {:name "some name" :description "some descr"}))]
        (is (= 200 (:status response)))
        (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type"))))))

  (testing "that call to the detector registration api returns error if description is missing"
    (with-redefs [register-detector (fn [_ _] true)]
      (let [response (app (request :post "/alarm/register/detector" {:description "some descr"}))]
        (is (= 400 (:status response)))
        (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type")))))))

(deftest test-api-register-trigger
  (testing "that call to the trigger registration api works"
    (with-redefs [register-trigger (fn [_ _] true)]
      (let [response (app (request :post "/alarm/register/trigger" {:name "some name" :description "some descr"}))]
        (is (= 200 (:status response)))
        (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type"))))))

  (testing "that call to the trigger registration api returns error if name and description are missing"
    (with-redefs [register-trigger (fn [_ _] true)]
      (let [response (app (request :post "/alarm/register/trigger"))]
        (is (= 400 (:status response)))
        (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type")))))))

(deftest test-api-notify-alarm
  (testing "that call to alarm notification api workd"
    (let [response (app (request :post "/alarm/notify" {:id 7}))]
      (is (= 200 (:status response)))
      (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type"))))))
