(ns puresec-master-clojure.routes.admin-test
  (:use clojure.test
        ring.mock.request
        puresec-master-clojure.handler
        puresec-master-clojure.service.settings
        puresec-master-clojure.service.detector
        puresec-master-clojure.service.trigger))

(deftest test-api-map-trigger
  (testing "that call to the trigger map api works"
    (with-redefs [map-trigger (fn [_ _] true)]
      (let [response (app (request :post "/admin/notification/map" {:detector_id 7 :trigger_id 9}))]
        (is (= 200 (:status response)))
          (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type")))))))

(deftest test-api-unmap-trigger
  (testing "that call to the trigger unmap api works"
    (with-redefs [unmap-trigger (fn [_ _] true)]
      (let [response (app (request :post "/admin/notification/unmap" {:detector_id 11 :trigger_id 13}))]
        (is (= 200 (:status response)))
        (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type")))))))

(deftest test-settings-page
  (testing "that call to settings page works"
    (with-redefs [get-detectors (fn [] [])
                  get-triggers (fn [] [])]
      (let [response (app (request :get "/admin/settings"))]
        (is (= 200 (:status response)))))))
