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


(deftest horizontal-count-test

  ; set up test
  (dosync (ref-set field [["X" "X"]["O" "-"]]))
  (intern 'connect-four.core 'field-size 2)

  (testing "Horizontal right returns 0 in first line."
    (is (= 0 (horizontal-count 0 1 0 inc))))

  (testing "Horizontal left returns 1 in first line."
    (is (= 1 (horizontal-count 0 1 0 dec))))

  ; change field for test
  (dosync (ref-set field (assoc-in @field [0 0] "-")))

  (testing "Horizontal left returns 0 in first line.")
    (is (= 0 (horizontal-count 0 1 0 dec)))

  ; additional test case with bigger field
  (dosync (ref-set field [["X" "X" "X"] ["-" "-" "-"] ["-" "-" "-"]]))
  (intern 'connect-four.core 'field-size 3)

  (testing "left"
    (is (= 0 (horizontal-count 0 0 0 dec))))

  (testing "right"
    (is (= 2 (horizontal-count 0 0 0 inc)))))


(deftest vertical-count-test

  ; set up
  (dosync (ref-set field [["X" "O"]["X" "-"]]))
  (intern 'connect-four.core 'field-size 2)

  (testing "Vertical up returns 0 in first column."
    (is (= 0 (vertical-count 0 0 0 dec))))

  (testing "Vertical down returns 1 in first column.")
    (is (= 1 (vertical-count 0 0 0 inc)))

  ; cahnge field for test
  (dosync (ref-set field (assoc-in @field [1 0] "-")))

  (testing "Vertical down returns 0 in first column.")
    (is (= 0 (vertical-count 0 0 0 inc))))


(deftest descending-diagonal-count-test

  ;set up
  (dosync (ref-set field [["X" "O"]["-" "X"]]))
  (intern 'connect-four.core 'field-size 2)

  (testing "Diagonal up returns 0."
    (is (= 0 (diagonal-count 0 0 0 dec dec))))

  (testing "Diagonal down returns 1."
  (is (= 1 (diagonal-count 0 0 0 inc inc))))

  ;change field for test
  (dosync (ref-set field (assoc-in @field [1 1] "-")))

  (testing "Diagonal down returns 0."
    (is (= 0 (diagonal-count 0 0 0 inc inc)))))


(deftest ascending-diagonal-count-test

  ; set up
  (dosync (ref-set field [["O" "X"]["X" "-"]]))
  (intern 'connect-four.core 'field-size 2)

  (testing "Diagonl up returns 0."
    (is (= 0 (diagonal-count 0 1 0 dec inc))))

  (testing "Digonal down returns 1."
    (is (= 1 (diagonal-count 0 1 0 inc dec))))

  ; change field for test
  (dosync (ref-set field (assoc-in @field [1 0] "-")))

  (testing "Diagonal down returns 0."
    (is (= 0 (diagonal-count 0 1 0 dec inc)))))


(deftest won?-test

  ;set up
  (intern 'connect-four.core 'win-count 3)
  (intern 'connect-four.core 'field-size 3)

  (testing "won? should return false."
    (dosync (ref-set field [["X" "-" "-"] ["-" "-" "-"] ["-" "-" "-"]]))
    (is (false? (won? 0 0))))

  (testing "won? should return true. (horizontal)"
    (dosync (ref-set field [["X" "X" "X"] ["-" "-" "-"] ["-" "-" "-"]]))
    (is (won? 0 0)))

  (testing "won? should return true. (vertical)"
    (dosync (ref-set field [["X" "-" "-"] ["X" "-" "-"] ["X" "-" "-"]]))
    (is (won? 0 0)))

  (testing "won? should return true. (descending diagonal)"
    (dosync (ref-set field [["X" "-" "-"] ["-" "X" "-"] ["-" "-" "X"]]))
    (is (won? 0 0)))

  (testing "won? should return true. (ascending diagonal)"
    (dosync (ref-set field [["-" "-" "X"] ["-" "X" "-"] ["X" "-" "-"]]))
    (is (won? 0 2))))


(deftest evaluate-command-test
  (testing "Should be false. (returns nil)"
    (is (not (true? (Boolean. (evaluate-command "a"))))))

  (testing "Should return false."
    (is (= false (evaluate-command "1,1"))))

  (testing "Should return false."
    (is (= false (evaluate-command "new"))))

  (testing "Should return true."
    (dosync (ref-set field [["-" "O"] ["-" "-"]]))
    (is (evaluate-command "1,1"))))
