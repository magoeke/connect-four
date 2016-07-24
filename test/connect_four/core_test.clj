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
  (dosync
    (ref-set turn-count turn-count-default)
    (ref-set field field-default))
  (f))

(use-fixtures :each set-var-to-default)

(def new-test-field [["-" "-"]["-" "X"]])

(deftest field-test
  (testing "Create a 2D vector with size 2."
    (is (= [["-" "-"]["-" "-"]] (create2D 2))))

  (testing "Update field."
    (update-field new-test-field)
    (is (= @field new-test-field))))


(deftest turn-count-test
  (testing "Initial turn-count."
    (is (= @turn-count 0)))

  (testing "Increase turn-count."
    (turn-count-alter inc)
    (is (= 1 @turn-count)))

  (testing "Decrease turn-count."
    (turn-count-alter dec)
    (is (= 0 @turn-count))))


(deftest current-player-test
  (testing "Get current player X."
    (is (= "X" (current-player))))

  (testing "Get current player O."
    (turn-count-alter inc)
    (is (= "O" (current-player)))))


(deftest set-cell-test
  (testing "Set cell 1,1 in a 2x2 field the first time."
    (set-cell "X" 1 1)
    (is (= @field new-test-field))
    (is (= @turn-count 0)))

  (testing "Set cell 1,1 in a 2x2 field the second time."
    (set-cell "O" 1 1)
    (is (= @field new-test-field))
    (is (= @turn-count -1))))


(deftest prepare-command-to-set-test
  (testing "Valid command 1,1 for the first time."
    (turn-count-alter inc) ; only necessary to get new-test-field
    (prepare-command-to-set "1,1")
    (is (= @field new-test-field))
    (is (= @turn-count 2)))

  (testing "Valid command 1,1 for second time."
    (prepare-command-to-set "1,1")
    (is (= @field new-test-field))
    (is (= @turn-count 2)))

  (testing "Valid command 0,0"
    (prepare-command-to-set "0,0")
    (is (not= field new-test-field))
    (is (= @turn-count 3))))


(deftest horizontal-win-test

  ; set up test
  (dosync (ref-set field [["X" "X"]["O" "-"]]))
  (intern 'connect-four.core 'field-size 2)

  (testing "Horizontal right returns 0 in first line."
    (is (= 0 (horizontal-win 0 1 0 inc))))

  (testing "Horizontal left returns 1 in first line."
    (is (= 1 (horizontal-win 0 1 0 dec))))

  ; change field for test
  (dosync (ref-set field (assoc-in @field [0 0] "-")))

  (testing "Horizontal left returns 0 in first line.")
    (is (= 0 (horizontal-win 0 1 0 dec))))


(deftest vertical-win-test

  ; set up
  (dosync (ref-set field [["X" "O"]["X" "-"]]))
  (intern 'connect-four.core 'field-size 2)

  (testing "Vertical up returns 0 in first column."
    (is (= 0 (vertical-win 0 0 0 dec))))

  (testing "Vertical down returns 1 in first column.")
    (is (= 1 (vertical-win 0 0 0 inc)))

  ; cahnge field for test
  (dosync (ref-set field (assoc-in @field [1 0] "-")))

  (testing "Vertical down returns 0 in first column.")
    (is (= 0 (vertical-win 0 0 0 inc))))



; TODO: mocking in clojure ?
; (deftest evaluate-command-test)
