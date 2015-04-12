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
    (with-redefs [register-detector-slave (fn [_ _] true)]
      (let [response (app (request :post "/alarm/register/detector"))]
        (is (= 200 (:status response)))
        (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type")))))))

(deftest test-api-register-trigger
  (testing "that call to the trigger registration api works"
    (with-redefs [register-trigger (fn [_ _] true)]
      (let [response (app (request :post "/alarm/register/trigger"))]
        (is (= 200 (:status response)))
        (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type")))))))
