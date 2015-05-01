(ns puresec-master-clojure.service.trigger-test
  (:use clojure.test
        puresec-master-clojure.service.trigger
        puresec-master-clojure.service.slave
        puresec-master-clojure.db.core))

(deftest test-register-trigger
  (testing "that the registration of a trigger is successful"
    (with-redefs [register-slave (fn [_ _ _ _ _] {:state "SUCCESS" :id 9})
                  update-trigger-cache (fn [] nil)]
      (swap! trigger-cache (fn [_] nil))
      (is (= {:state "SUCCESS" :id 9}
             (register-trigger "some name" "some descr" "http://someUrl")))))

  (testing "that the registration of a trigger updates the cache properly"
    (with-redefs [register-slave (fn [_ _ _ _ _] {:state "SUCCESS" :id 9})
                  get-slaves (fn [_] [{:id 9 :name "some name" :description "some descr"}])]
      (swap! trigger-cache (fn [_] nil))
      (register-trigger "some name" "some descr" "http://someUrl")
      (is (= [{:id 9 :name "some name" :description "some descr"}]
             @trigger-cache)))))

(deftest test-get-triggers
  (testing "that list of triggers is returned"
    (with-redefs [update-trigger-cache (fn [] [{:id 5 :name "name 0" :description "name 0 descr"}
                                      {:id 7 :name "name 1" :description "name 1 descr"}])]
      (swap! trigger-cache (fn [_] nil))
      (is (= [{:id 5 :name "name 0" :description "name 0 descr"}
              {:id 7 :name "name 1" :description "name 1 descr"}]
             (get-triggers))))))

(deftest test-update-cache
  (testing "that the cache is properly updated"
    (with-redefs [get-slaves (fn [_] [{:id 11 :name "name 0" :description "name 0 descr"}
                                      {:id 13 :name "name 1" :description "name 1 descr"}])]
      (swap! trigger-cache (fn [_] nil))
      (is (= nil
             @trigger-cache))
      (is (= [{:id 11 :name "name 0" :description "name 0 descr"}
              {:id 13 :name "name 1" :description "name 1 descr"}]
             (update-trigger-cache)))
      (is (= [{:id 11 :name "name 0" :description "name 0 descr"}
              {:id 13 :name "name 1" :description "name 1 descr"}]
             @trigger-cache)))))
