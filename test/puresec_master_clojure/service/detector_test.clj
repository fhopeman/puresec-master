(ns puresec-master-clojure.service.detector_test
  (:use clojure.test
        puresec-master-clojure.service.detector
        puresec-master-clojure.service.slave
        puresec-master-clojure.db.core))

(deftest test-register-detector
   (testing "that the registration is successful if slave already exists"
      (with-redefs [register-slave (fn [_ _ _ _] {:state "SUCCESS" :id 7})]
        (is (= {:state "SUCCESS" :id 7}
               (register-detector "some name" "some name descr"))))))

(deftest test-get-detectors
  (testing "that list of detectors is returned"
    (with-redefs [get-slaves (fn [_] [{:id 11 :name "name 0" :description "name 0 descr"}
                                      {:id 13 :name "name 1" :description "name 1 descr"}])]
      (is (= [{:id 11 :name "name 0" :description "name 0 descr"}
              {:id 13 :name "name 1" :description "name 1 descr"}]
             (get-detectors))))))
