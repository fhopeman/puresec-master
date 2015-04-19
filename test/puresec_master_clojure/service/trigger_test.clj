(ns puresec-master-clojure.service.trigger-test
  (:use clojure.test
        puresec-master-clojure.service.trigger
        puresec-master-clojure.service.slave
        puresec-master-clojure.db.core))

(deftest test-register-trigger
  (testing "that the registration is successful if slave already exists"
    (with-redefs [register-slave (fn [_ _ _ _ _] {:state "SUCCESS" :id 9})]
      (is (= {:state "SUCCESS" :id 9}
             (register-trigger "some name" "some name descr" "http://someUrl"))))))

(deftest test-get-triggers
  (testing "that list of triggers is returned"
    (with-redefs [get-slaves (fn [_] [{:id 5 :name "name 0" :description "name 0 descr"}
                                      {:id 7 :name "name 1" :description "name 1 descr"}])]
      (is (= [{:id 5 :name "name 0" :description "name 0 descr"}
              {:id 7 :name "name 1" :description "name 1 descr"}]
             (get-triggers))))))
