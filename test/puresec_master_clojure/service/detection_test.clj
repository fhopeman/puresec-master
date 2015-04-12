(ns puresec-master-clojure.service.detection-test
  (:use clojure.test
        puresec-master-clojure.service.detection
        puresec-master-clojure.db.core))

(deftest test-detection-slave-registration
         (testing "that the registration of a detection slave is successful"
                  (with-redefs [register-slave-detect! (fn [_] true)
                                load-slave-detect (fn [_] [{:id 7}])]
                    (is (= {:id 7 :state "SUCCESS"}
                           (register-detection-slave "some zone" "some zone descr")))))

         (testing "that the registration of a detection slave fails"
                  (with-redefs [register-slave-detect! (fn [_] false)]
                    (is (= {:state "ERROR"}
                           (register-detection-slave "some zone" "some zone descr"))))))
