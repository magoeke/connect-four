(ns connect-four.core)

(println "Connect Four v.0.1")

(defn showCommands
  "Outputs all possible commands."
  []
  (println "Commands: stop; Y,X; new"))

(defn evaluateCommand
  "Evaluates command from user input."
  [command]
  (cond
    (= command "new")(println "not implemented yet")
    ;insert cond REGEX Y,X
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
