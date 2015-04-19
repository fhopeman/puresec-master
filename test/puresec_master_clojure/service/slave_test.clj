(ns puresec-master-clojure.service.slave-test
  (:use clojure.test
        puresec-master-clojure.service.slave))

(defn load-slave-which-doesnt-exist [_] [])
(defn load-slave-which-exists [_] [{:id 9 :name "some name" :description "some descr" :url "http://someUrl"}])

(defn save-slave-with-success [_] 1)
(defn save-slave-with-error [_] 0)

(defn load-two-slaves [] [{:id 11 :name "name 0" :description "name 0 descr" :url "http://someUrl/1"}
                          {:id 13 :name "name 1" :description "name 1 descr" :url "http://someUrl/2"}])

(deftest test-register-slave
  (testing "that the registration is successful if slave already exists"
    (with-redefs [get-slave-if-exists (fn [_ _] {:id 7 :name "some name" :description "some name descr" :url "http://someUrl"})]
      (is (= {:state "SUCCESS" :id 7}
        (register-slave "some name" "some name descr" "http://someUrl" load-slave-which-exists save-slave-with-success)))))

  (testing "that the registration is successful if slave doesn't exist"
    (with-redefs [get-slave-if-exists (fn [_ _] nil)]
      (is (= {:state "SUCCESS" :id 9}
        (register-slave "some name" "some name descr" "http://someUrl" load-slave-which-exists save-slave-with-success)))))

  (testing "that the registration returns an error if it fails"
    (with-redefs [get-slave-if-exists (fn [_ _] nil)]
      (is (= {:state "ERROR" :message "unknown error"}
        (register-slave "some name" "some name descr" "http://someUrl" load-slave-which-doesnt-exist save-slave-with-error))))))

(deftest test-get-slave-if-exists
  (testing "that the first slave is returned"
    (is (= {:id 9 :name "some name" :description "some descr" :url "http://someUrl"}
      (get-slave-if-exists "some name" load-slave-which-exists))))

(testing "that nil is returned if slave doesn't exist"
  (is (= nil
    (get-slave-if-exists "some name" load-slave-which-doesnt-exist)))))

(deftest test-get-slaves
  (testing "that list of slaves is returned"
    (is (= [{:id 11 :name "name 0" :description "name 0 descr" :url "http://someUrl/1"}
            {:id 13 :name "name 1" :description "name 1 descr" :url "http://someUrl/2"}]
           (get-slaves load-two-slaves)))))
