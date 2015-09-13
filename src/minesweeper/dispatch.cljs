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
        add-bomb (fn [board {:keys [x y]}]
                   (assoc-in board [:tiles y x :bomb?] true))
        board (reduce add-bomb fresh-board bomb-positions)]
    (swap! state assoc :board board
                       :mode :playing)))

(defn detonate!
  [tile]
  (js/alert "Boom!"))

(defn clear-tile!
  "return an event handler that clears the provided tile when the event fires"
  [{:keys [x y] :as tile}]
  (fn [_]
    (if (:bomb? tile)
        (detonate! tile)
        (let [neighbors (minesweeper.board/get-neighbors tile (:board @state))
              bomb-count (count (keep :bomb? neighbors))]
          (swap! state assoc-in [:board :tiles y x :cleared?]
                 bomb-count)))))

