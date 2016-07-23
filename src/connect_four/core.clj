(ns connect-four.core
  (require [clojure.string :as str]))

(println "Connect Four v.0.1")

(defn create2D
  "Creates a 2D vector.(game board)"
  [size]
  (vec (repeat size (vec (repeat size "-")))))


(def turn-count 0)
(def field-size 3)
(def field (create2D field-size)) ;


(defn update-field
  "Updates field."
  [new-field]
  (def field new-field))

(defn turn-count-down
  "Decreases value of turn-count."
  []
  (def turn-count (dec turn-count)))

(defn turn-count-up
  "Increases value of turn-count."
  []
  (def turn-count (inc turn-count)))

(defn setCell
  "Sets cell in field."
  [player y x]
  (if (= "-" (get-in field [y x]))
    (update-field (assoc-in field [y x] player))
    (turn-count-down)))

(defn currentPlayer
  "Returns sign of current player."
  []
  (if (= 0 (mod turn-count 2)) "X" "O"))

(defn prepareCommandToSet
  "Prepares command to call setCell."
  [command]
  (turn-count-up)
  (let [coords (str/split command #",")]
    (setCell (currentPlayer)
      (Integer. (get coords 0))
      (Integer. (get coords 1)))))

(defn showCommands
  "Outputs all possible commands."
  []
  (println "Commands: stop; Y,X; new"))

(defn draw-field
  "Draws the field."
  []
  (println field)
  (map println field) ; doesn't output anything
  (println turn-count))

(defn wrap-draw
  "Draws the field after the command was executed."
  [function argument]
  (function argument)
  (draw-field))

(defn evaluateCommand
  "Evaluates command from user input."
  [command]
  (cond
    (= command "new")(wrap-draw println "not implemented yet")
    (re-matches #"[0-9]+,[0-9]+" command)(wrap-draw prepareCommandToSet command)
    :else (println "command doesn't exist")))

(defn handleInput
  "Handles Input from user. input != stop"
  [input]
  (evaluateCommand (str input))
  (gameloop))

(defn gameloop
  "This function represents the game loop."
  []
  (showCommands)
  (let [input (read-line)]
    (println input)
    (if (not= input "stop")
      (handleInput input)
      (println "I hope you enjoyed the game."))))

(gameloop)
