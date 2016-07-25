# connect-four

>  Connect Four (also known as Captain's Mistress, Four Up, Plot Four, Find Four, Fourplay[citation needed], Four in a Row, Four in a Line and Gravitrips (in Soviet Union) ) is a two-player connection game

Source: [wikipedia](https://en.wikipedia.org/wiki/Connect_Four)

This connect four is build in clojure. Currently there exists only a TUI. Building a GUI is the next goal.

## Usage

At the moment you only can start the game through the repl (or it's the way I do it). You need to launch the game-loop function. Following Snippet shows the steps I do to start the game in the repl:

```
user=> (require 'connect-four.core)
user=> (connect-four.core/game-loop)
```

## Tests

Yes, I wrote test but I don't have a 100% coverage. The next box shows the output of cloverage.

```
|             :name | :forms_percent | :lines_percent |
|-------------------+----------------+----------------|
| connect-four.core |        91.89 % |        88.89 % |
Files with 100% coverage: 0

Forms covered: 91.89 %
Lines covered: 88.89 %
```

## License

Use it if you're brave enough ;)
