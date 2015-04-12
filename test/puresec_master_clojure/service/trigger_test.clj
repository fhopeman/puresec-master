(ns puresec-master-clojure.service.trigger-test
  (:use clojure.test
        puresec-master-clojure.service.trigger
        puresec-master-clojure.db.core))

(deftest test-register-trigger
  (testing "that the registration is successful if trigger already exists"
    (with-redefs [get-trigger-if-exists (fn [_] {:id 7 :name "some name" :description "some descr"})]
      (is (= {:state "SUCCESS" :id 7}
             (register-trigger "some name" "some descr")))))

  (testing "that the registration is successful if trigger doesn't exist"
    (with-redefs [get-trigger-if-exists (fn [_] nil)
                  save-trigger! (fn [_] true)
                  load-trigger (fn [_] [{:id 9}])]
      (is (= {:state "SUCCESS" :id 9}
             (register-trigger "some name" "some descr")))))

  (testing "that the registration returns an error if it fails"
    (with-redefs [get-trigger-if-exists (fn [_] nil)
                  save-trigger! (fn [_] false)]
      (is (= {:state "ERROR"}
             (register-trigger "some name" "some descr"))))))

(deftest test-get-trigger-if-exists
  (testing "that the first trigger is returned"
    (with-redefs [load-trigger (fn [_] [{:id 3 :name "some name" :description "some descr"}])]
      (is (= {:id 3 :name "some name" :description "some descr"}
             (get-trigger-if-exists "some name")))))

  (testing "that nil is returned if slave doesn't exist"
    (with-redefs [load-trigger (fn [_] [])]
      (is (= nil
             (get-trigger-if-exists "some name"))))))

(deftest test-get-triggers
  (testing "that list of triggers is returned"
    (with-redefs [load-triggers (fn [] [{:id 11 :name "name 0" :description "name 0 descr"}
                                               {:id 13 :name "name 1" :description "name 1 descr"}])]
      (is (= [{:id 11 :name "name 0" :description "name 0 descr"}
              {:id 13 :name "name 1" :description "name 1 descr"}]
             (get-triggers))))))
