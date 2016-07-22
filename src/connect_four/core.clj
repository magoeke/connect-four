(ns connect-four.core
  (require [clojure.string :as str]))

(println "Connect Four v.0.1")

(def turn-count 0)

(defn setCell
  "Sets cell in field."
  [player y x]
  (println player )
  (println y)
  (println x))

(defn currentPlayer
  "Returns sign of current player."
  []
  (if (= 0 (mod turn-count 2)) "X" "O"))

(defn prepareCommandToSet
  "Prepares command to call setCell."
  [command]
  (def turn-count (inc turn-count)) ; update turn-count
  (let [coords (str/split command #",")]
    (setCell (currentPlayer)
      (Integer. (get coords 0))
      (Integer. (get coords 1)))))

(defn showCommands
  "Outputs all possible commands."
  []
  (println "Commands: stop; Y,X; new"))

(defn evaluateCommand
  "Evaluates command from user input."
  [command]
  (cond
    (= command "new")(println "not implemented yet")
    (re-matches #"[0-9]+,[0-9]+" command)(prepareCommandToSet command)
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
