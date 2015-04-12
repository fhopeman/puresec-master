(ns puresec-master-clojure.utils.response-test
  (:use clojure.test
        puresec-master-clojure.utils.response))

(deftest test-create-successful-result
  (testing "that a valid successful result is created"
    (is (= {:state "SUCCESS" :id 5}
           (create-successful-result 5)))))

(deftest test-create-error-result
  (testing "that a valid error result is created"
    (is (= {:state "ERROR"}
           (create-error-result)))))
