(ns puresec-master-clojure.routes.admin-test
  (:use clojure.test
        ring.mock.request
        puresec-master-clojure.handler
        puresec-master-clojure.service.settings))

(deftest test-api-notify-alarm
  (testing "that call to alarm notification api workd"
    (with-redefs [map-trigger (fn [_ _] true)]
      (let [response (app (request :post "/admin/notification/map" {:detector_id 7 :trigger_id 9}))]
        (is (= 200 (:status response)))
          (is (= "application/json; charset=utf-8" (get (:headers response) "Content-Type")))))))
