(ns minesweeper.dispatch
    (:require [minesweeper.state :refer [state]]
              [minesweeper.board]
              ))

(defn init-board!
  []
  (let [fresh-board (minesweeper.board/make-board 9 9)
        bomb-positions [{:x 3, :y 3} {:x 7, :y 3}
                        {:x 1, :y 1} {:x 6, :y 4}
                        {:x 5, :y 7} {:x 0, :y 0}]
        add-bomb (fn [board position]
                   (minesweeper.board/set-tile board position :bomb? true))
        board (reduce add-bomb fresh-board bomb-positions)]
    (swap! state assoc :board board
                       :mode :playing)))

(defn detonate!
  [tile]
  (swap! state update-in [:board]
         minesweeper.board/set-tile tile :detonated? true)
  (swap! state assoc :mode :dead))

(defn count-neighboring
  [board tile]
  (let [neighbors (minesweeper.board/get-neighbors board tile)]
    (count (keep :bomb? neighbors))))

(defn clear-tile
  [board tile]
  (if (:cleared? tile)
    board
    (minesweeper.board/set-tile board tile :cleared?
                                (count-neighboring board tile))))
(defn clear-tile!
  "return an event handler that clears the provided tile when the event fires"
  [{:keys [x y] :as tile}]
  (fn [_]
    (if (:bomb? tile)
        (detonate! tile)
        (swap! state update-in [:board] clear-tile tile))))

