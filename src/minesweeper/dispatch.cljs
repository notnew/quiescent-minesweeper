(ns minesweeper.dispatch
    (:require [minesweeper.state :refer [state]]
              [minesweeper.board]
              ))

(defn init-board!
  []
  (let [new-board (minesweeper.board/make-board 9 9)]
    (swap! state assoc :board new-board)))
