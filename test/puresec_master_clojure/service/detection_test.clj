(ns puresec-master-clojure.service.detection-test
  (:use clojure.test
        puresec-master-clojure.service.detection
        puresec-master-clojure.db.core))

(deftest test-register-detection-slave
   (testing "that the registration is successful if slave already exists"
      (with-redefs [get-slave-if-exists (fn [_] {:id 7 :zone_name "some zone" :zone_description "some zone descr"})]
        (is (= {:state "SUCCESS" :id 7}
               (register-detection-slave "some zone" "some zone descr")))))

   (testing "that the registration is successful if slave doesn't exist"
      (with-redefs [get-slave-if-exists (fn [_] nil)
                    register-slave-detect! (fn [_] true)
                    load-slave-detect (fn [_] [{:id 9}])]
        (is (= {:state "SUCCESS" :id 9}
               (register-detection-slave "some zone" "some zone descr")))))

   (testing "that the registration returns an error if it fails"
     (with-redefs [get-slave-if-exists (fn [_] nil)
                   register-slave-detect! (fn [_] false)]
       (is (= {:state "ERROR"}
              (register-detection-slave "some zone" "some zone descr"))))))

(deftest test-create-successful-result
  (testing "that a valid successful result is created"
    (is (= {:state "SUCCESS" :id 5}
           (create-successful-result 5)))))

(deftest test-get-slave-if-exists
  (testing "that the first (and only) detection slave is returned"
    (with-redefs [load-slave-detect (fn [_] [{:id 3 :zone_name "some zone" :zone_description "some zone descr"}])]
      (is (= {:id 3 :zone_name "some zone" :zone_description "some zone descr"}
             (get-slave-if-exists "some zone")))))

  (testing "that nil is returned if slave doesn't exist"
    (with-redefs [load-slave-detect (fn [_] [])]
      (is (= nil
             (get-slave-if-exists "some zone"))))))
