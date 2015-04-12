(ns puresec-master-clojure.service.detector_test
  (:use clojure.test
        puresec-master-clojure.service.detector
        puresec-master-clojure.db.core))

(deftest test-register-detector
   (testing "that the registration is successful if slave already exists"
      (with-redefs [get-detector-if-exists (fn [_] {:id 7 :name "some name" :description "some name descr"})]
        (is (= {:state "SUCCESS" :id 7}
               (register-detector "some name" "some name descr")))))

   (testing "that the registration is successful if slave doesn't exist"
      (with-redefs [get-detector-if-exists (fn [_] nil)
                    save-detector! (fn [_] true)
                    load-detector (fn [_] [{:id 9}])]
        (is (= {:state "SUCCESS" :id 9}
               (register-detector "some name" "some name descr")))))

   (testing "that the registration returns an error if it fails"
     (with-redefs [get-detector-if-exists (fn [_] nil)
                   save-detector! (fn [_] false)]
       (is (= {:state "ERROR"}
              (register-detector "some name" "some name descr"))))))

(deftest test-get-detector-if-exists
  (testing "that the first (and only) detector slave is returned"
    (with-redefs [load-detector (fn [_] [{:id 3 :name "some name" :description "some descr"}])]
      (is (= {:id 3 :name "some name" :description "some descr"}
             (get-detector-if-exists "some name")))))

  (testing "that nil is returned if slave doesn't exist"
    (with-redefs [load-detector (fn [_] [])]
      (is (= nil
             (get-detector-if-exists "some name"))))))

(deftest test-get-detectors
  (testing "that list of detector slaves is returned"
    (with-redefs [load-detectors (fn [] [{:id 11 :name "name 0" :description "name 0 descr"}
                                                 {:id 13 :name "name 1" :description "name 1 descr"}])]
      (is (= [{:id 11 :name "name 0" :description "name 0 descr"}
              {:id 13 :name "name 1" :description "name 1 descr"}]
             (get-detectors))))))
