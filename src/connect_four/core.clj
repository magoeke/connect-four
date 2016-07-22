(ns connect-four.core)

(println "Connect Four v.0.1")

(defn showCommands
  "Outputs all possible commands."
  []
  (println "Commands: stop; Y,X; new"))

(defn gameloop
  "This function represents the game loop."
  []
  (showCommands)
  (let [input (read-line)]
    (println input)
    (if (not= input "stop")
      (gameloop))))

(gameloop)
