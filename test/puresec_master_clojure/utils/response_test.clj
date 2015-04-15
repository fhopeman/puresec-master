(ns puresec-master-clojure.utils.response-test
  (:use clojure.test
        puresec-master-clojure.utils.response))

(deftest test-create-successful-result
  (testing "that a valid success result is created"
    (is (= {:state "SUCCESS"}
           (create-successful-result))))

  (testing "that a valid success result with id field is created"
    (is (= {:state "SUCCESS" :id 7}
           (create-successful-result 7)))))

(deftest test-create-error-result
  (testing "that a valid error result is created"
    (is (= {:state "ERROR" :message "unknown error"}
           (create-error-result))))

  (testing "that a valid error result with defined message is created"
    (is (= {:state "ERROR" :message "some error message"}
           (create-error-result "some error message")))))
