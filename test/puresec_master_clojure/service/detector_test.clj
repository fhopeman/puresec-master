(ns puresec-master-clojure.service.detector_test
  (:use clojure.test
        puresec-master-clojure.service.detector
        puresec-master-clojure.db.core))

(deftest test-register-detector-slave
   (testing "that the registration is successful if slave already exists"
      (with-redefs [get-detector-if-exists (fn [_] {:id 7 :zone_name "some zone" :zone_description "some zone descr"})]
        (is (= {:state "SUCCESS" :id 7}
               (register-detector-slave "some zone" "some zone descr")))))

   (testing "that the registration is successful if slave doesn't exist"
      (with-redefs [get-detector-if-exists (fn [_] nil)
                    save-detector! (fn [_] true)
                    load-detector (fn [_] [{:id 9}])]
        (is (= {:state "SUCCESS" :id 9}
               (register-detector-slave "some zone" "some zone descr")))))

   (testing "that the registration returns an error if it fails"
     (with-redefs [get-detector-if-exists (fn [_] nil)
                   save-detector! (fn [_] false)]
       (is (= {:state "ERROR"}
              (register-detector-slave "some zone" "some zone descr"))))))

(deftest test-get-detector-if-exists
  (testing "that the first (and only) detector slave is returned"
    (with-redefs [load-detector (fn [_] [{:id 3 :zone_name "some zone" :zone_description "some zone descr"}])]
      (is (= {:id 3 :zone_name "some zone" :zone_description "some zone descr"}
             (get-detector-if-exists "some zone")))))

  (testing "that nil is returned if slave doesn't exist"
    (with-redefs [load-detector (fn [_] [])]
      (is (= nil
             (get-detector-if-exists "some zone"))))))

(deftest test-get-detectors
  (testing "that list of detector slaves is returned"
    (with-redefs [load-detectors (fn [] [{:id 11 :zone_name "zone 0" :zone_description "zone 0 descr"}
                                                 {:id 13 :zone_name "zone 1" :zone_description "zone 1 descr"}])]
      (is (= [{:id 11 :zone_name "zone 0" :zone_description "zone 0 descr"}
              {:id 13 :zone_name "zone 1" :zone_description "zone 1 descr"}]
             (get-detector-slaves))))))
