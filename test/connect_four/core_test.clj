(ns connect-four.core-test
  (:require [clojure.test :refer :all]
            [connect-four.core :refer :all]))

; default values vor vars in core
(def field-default [["-" "-"]["-" "-"]])
(def turn-count-default 0)

; fixture
(defn set-var-to-default
  "Sets vars to default."
  [f]
  (intern 'connect-four.core 'turn-count turn-count-default)
  (intern 'connect-four.core 'field field-default)
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


(deftest set-cell-test
  (testing "Set cell 1,1 in a 2x2 field the first time."
    (set-cell "X" 1 1)
    (is (= field new-test-field))
    (is (= turn-count 0)))

  (testing "Set cell 1,1 in a 2x2 field the second time."
    (set-cell "O" 1 1)
    (is (= field new-test-field))
    (is (= turn-count -1))))
