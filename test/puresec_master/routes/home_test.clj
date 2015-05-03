(ns puresec-master.routes.home_test
  (:use clojure.test
        ring.mock.request
        puresec-master.handler))

(deftest test-home
  (testing "that the root route response 302"
    (let [response (app (request :get "/"))]
      (is (= 302 (:status response)))))

  (testing "that invalid request returns 404"
    (let [response (app (request :get "/invalid"))]
      (is (= 404 (:status response))))))
