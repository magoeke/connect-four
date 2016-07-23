(ns connect-four.core
  (require [clojure.string :as str]))

(println "Connect Four v.0.1")

(defn create2D
  "Creates a 2D vector.(game board)"
  [size]
  (vec (repeat size (vec (repeat size "-")))))


(def turn-count (ref 0))
(def field-size 3)
(def field (ref (create2D field-size)))


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
  (if (= "-" (get-in @field [y x]))
    (update-field (assoc-in @field [y x] player))
    (turn-count-alter dec)))

(defn current-player
  "Returns sign of current player."
  []
  (if (= 0 (mod @turn-count 2)) "X" "O"))

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

(game-loop)
