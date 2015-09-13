(ns minesweeper.state
  (:require
   [minesweeper.board :refer [make-board get-neighbors
                              set-tile update-tile map-tiles]]))

(defonce state (atom {:mode :start
                      :bomb-count 10
                      :board {:width 9 :height 9}}))

(defn- count-neighboring
  [board tile]
  (let [neighbors (get-neighbors board tile)]
    (count (keep :bomb? neighbors))))

(defn- mark-neighbor-counts
  [board]
  (let [mark-neighbor-count (fn [tile]
                              (assoc tile :neighboring-bombs
                                     (count-neighboring board tile)))]
    (map-tiles board mark-neighbor-count)))

(defn new-game
  [state]
  (let [{:keys [width height]} (:board state)
        fresh-board (make-board width height)
        bomb-positions [{:x 3, :y 3} {:x 7, :y 3}
                        {:x 1, :y 1} {:x 6, :y 4}
                        {:x 5, :y 7} {:x 0, :y 0}]
        add-bomb (fn [board position]
                   (set-tile board position :bomb? true))
        board (reduce add-bomb fresh-board bomb-positions)
        board (mark-neighbor-counts board)
        ]
    (assoc state :board board :mode :playing)))

(defn clear-tile
  [board tile]
  (if (:cleared? tile)
    board
    (let [flood-safe  ; flood-fill to find `safe` tiles to clear
          (fn flood-safe [acc tile]
            (cond
             (acc tile) acc

             (= 0 (:neighboring-bombs tile)) ; safe: apply to all neighbors
             (reduce flood-safe (merge acc tile)
                                (get-neighbors board tile))

             :else (merge acc tile)))

          clear (fn [board tile]
                  (set-tile board tile :cleared? true))]
      (reduce clear board (flood-safe #{} tile)))))
