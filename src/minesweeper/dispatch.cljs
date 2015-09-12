(ns minesweeper.dispatch
    (:require [minesweeper.state :refer [state]]
              [minesweeper.board]
              ))

(defn init-board
  [{:keys [width height] :as state}]
  (into state {:board (minesweeper.board/make-board width height)}))

(defn init-board!
  []
  (swap! state init-board))
