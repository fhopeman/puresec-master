(ns puresec-master.service.detector_test
  (:use clojure.test
        puresec-master.service.detector
        puresec-master.service.slave
        puresec-master.db.core))

(deftest test-register-detector
  (testing "that the registration is successful"
    (with-redefs [register-slave (fn [_ _ _ _ _] {:state "SUCCESS" :id 7})
                  update-detector-cache (fn [] nil)]
      (is (= {:state "SUCCESS" :id 7}
             (register-detector "some name" "some name descr" "http://someUrl")))))

  (testing "that the registration of a detector updates the cache properly"
    (with-redefs [register-slave (fn [_ _ _ _ _] {:state "SUCCESS" :id 9})
                  get-slaves (fn [_] [{:id 9 :name "some name" :description "some descr"}])]
      (swap! detector-cache (fn [_] nil))
      (register-detector "some name" "some descr" "http://someUrl")
      (is (= [{:id 9 :name "some name" :description "some descr"}]
             @detector-cache)))))

(deftest test-get-detectors
  (testing "that list of detectors is returned"
    (with-redefs [update-detector-cache (fn [] [{:id 11 :name "name 0" :description "name 0 descr"}
                                      {:id 13 :name "name 1" :description "name 1 descr"}])]
      (swap! detector-cache (fn [_] nil))
      (is (= [{:id 11 :name "name 0" :description "name 0 descr"}
              {:id 13 :name "name 1" :description "name 1 descr"}]
             (get-detectors))))))

(deftest test-update-cache
  (testing "that the cache is properly updated"
    (with-redefs [get-slaves (fn [_] [{:id 11 :name "name 0" :description "name 0 descr"}
                                      {:id 13 :name "name 1" :description "name 1 descr"}])]
      (swap! detector-cache (fn [_] nil))
      (is (= nil
             @detector-cache))
      (is (= [{:id 11 :name "name 0" :description "name 0 descr"}
              {:id 13 :name "name 1" :description "name 1 descr"}]
             (update-detector-cache)))
      (is (= [{:id 11 :name "name 0" :description "name 0 descr"}
              {:id 13 :name "name 1" :description "name 1 descr"}]
             @detector-cache)))))
