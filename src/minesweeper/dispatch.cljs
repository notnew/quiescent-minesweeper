(ns minesweeper.dispatch
    (:require [minesweeper.state :refer [state]]
              [minesweeper.board]
              ))

(defn init-board!
  []
  (let [new-board (minesweeper.board/make-board 9 9)]
    (swap! state assoc :board new-board)))

(defn clear-tile!
  "return an event handler that clears the provided tile when the event fires"
  [{:keys [x y]}]
  (fn [_]
    (swap! state assoc-in [:board :tiles y x :cleared?] 0)))

