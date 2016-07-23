(ns connect-four.core-test
  (:require [clojure.test :refer :all]
            [connect-four.core :refer :all]))

; fixture
(defn set-var-to-default
  "Sets turn-count to 0."
  [f]
  (intern 'connect-four.core 'turn-count 0)
  (f))

(use-fixtures :each set-var-to-default)

(def new-test-field [["-" "-"]["-" "X"]])

(deftest field-test
  (testing "Create a 2D vector with size 2."
    (is (= [["-" "-"]["-" "-"]] (create2D 2))))

  (testing "Update field."
    (update-field new-test-field)
    (is (= field new-test-field))))

(deftest turn-count-test
  (testing "Initial turn-count."
    (is (= turn-count 0)))

  (testing "Increase turn-count."
    (turn-count-up)
    (is (= 1 turn-count)))

  (testing "Decrease turn-count."
    (turn-count-down)
    (is (= 0 turn-count))))

(deftest current-player-test
  (testing "Get current player X."
    (is (= "X" (current-player))))

  (testing "Get current player O."
    (turn-count-up)
    (is (= "O" (current-player)))))
