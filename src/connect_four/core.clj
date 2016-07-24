(ns connect-four.core
  (require [clojure.string :as str]))

(println "Connect Four v.0.1")

(def no-player "-")
(def player-one "X")
(def player-two "O")
(def turn-count (ref 0))
(def field-size 3)
(def win-count 2)
(def start-count 0)

(defn create2D
  "Creates a 2D vector.(game board)"
  [size]
  (vec (repeat size (vec (repeat size no-player)))))

(def field (ref (create2D field-size)))

(declare current-player)

(defn horizontal-count
  "Counts the number of stones from current player in horizontal line.
  It counts either the left or the right side from Point(y,x). n is a helper
  variable to count the stones. fn expects: inc(right side) or dec(left side)."
  [y x n fn]
  (if (and
    (<= 0 x (dec field-size))
    (= (current-player) (get-in @field [y x])))
      (horizontal-count y (fn x) (inc n) fn)
      (dec n)))

(defn vertical-count
  "Counts the number of stones from current player in vertical line.
  It counts either above or under the point(y,x). n is a helper variable to
  count the stones. fn expects: inc(under) or dec(above)."
  [y x n fn]
  (if (and
    (<= 0 y (dec field-size))
    (= (current-player) (get-in @field [y x])))
    (vertical-count (fn y) x (inc n) fn)
    (dec n)))

(defn diagonal-count
  "Counts the number of stones from current player in diagonal line.
  It counts either above or under the point(y,x). n is a helper variable to
  count the stones.
  Ascending count => fn-y:dec fn-x:inc(above) or fn-y:inc fn-x:dec(under)
  Descending count => fn-y:dec fn-x:dec(above) or fn-y:inc fn-x:inc(under)"
  [y x n fn-y fn-x]
  (if (and
    (and
      (<= 0 y (dec field-size))
      (<= 0 x (dec field-size)))
    (= (current-player) (get-in @field [y x])))
    (diagonal-count (fn-y y) (fn-x x) (inc n) fn-y fn-x)
    (dec n)))

(defn won?
  "Checks if a player won the game. Needs the last changed cell coordinates
  (y,x) as input."
  [y x]
  (cond
    (<= win-count (inc (+ (horizontal-count y x start-count inc) (horizontal-count y x start-count dec)))) true
    (<= win-count (inc (+ (vertical-count y x start-count inc) (vertical-count y x start-count dec)))) true
    (<= win-count (inc (+ (diagonal-count y x start-count inc inc) (diagonal-count y x start-count dec dec)))) true
    (<= win-count (inc (+ (diagonal-count y x start-count inc dec) (diagonal-count y x start-count dec inc)))) true
    :else false))

(defn update-field
  "Updates field."
  [new-field]
  (dosync (ref-set field new-field)))

(defn turn-count-alter
  "Executes fn on turn-count and saves result."
  [fn]
  (dosync (alter turn-count fn)))

(defn set-cell
  "Sets cell in field."
  [player y x]
  (if (= no-player (get-in @field [y x]))
    (update-field (assoc-in @field [y x] player))
    (turn-count-alter dec)))

(defn current-player
  "Returns sign of current player."
  []
  (if (= 0 (mod @turn-count 2)) player-one player-two))

(defn prepare-command-to-set
  "Prepares command to call setCell."
  [command]
  (turn-count-alter inc)
  (let [coords (str/split command #",")]
    (set-cell (current-player)
      (Integer. (get coords 0))
      (Integer. (get coords 1)))))

(defn show-commands
  "Outputs all possible commands."
  []
  (println "Commands: stop; Y,X; new"))

(defn draw-field
  "Draws the field."
  []
  (println @field)
  (map println @field) ; doesn't output anything
  (println @turn-count))

(defn wrap-draw
  "Draws the field after the command was executed."
  ([function] (function) (draw-field))
  ([function argument] (function argument) (draw-field)))

(defn reset-game
  "Resets current game."
  []
  (dosync
    (ref-set turn-count 0)
    (ref-set field (create2D field-size))))

(defn evaluate-command
  "Evaluates command from user input."
  [command]
  (cond
    (= command "new")(wrap-draw reset-game)
    (re-matches #"[0-9]+,[0-9]+" command)(wrap-draw prepare-command-to-set command)
    :else (println "command doesn't exist")))

(declare game-loop) ; feels dirty

(defn handle-input
  "Handles Input from user. input != stop"
  [input]
  (evaluate-command (str input))
  (game-loop))

(defn game-loop
  "This function represents the game loop."
  []
  (show-commands)
  (let [input (read-line)]
    (if (not= input "stop")
      (handle-input input)
      (println "I hope you enjoyed the game."))))

; (game-loop)
