(ns minesweeper.dispatch
    (:require [minesweeper.state :refer [state]]
              [minesweeper.board
               :refer [make-board set-tile get-neighbors map-tiles]]
              ))

(declare mark-neighbor-counts)

(defn init-board!
  []
  (let [fresh-board (make-board 9 9)
        bomb-positions [{:x 3, :y 3} {:x 7, :y 3}
                        {:x 1, :y 1} {:x 6, :y 4}
                        {:x 5, :y 7} {:x 0, :y 0}]
        add-bomb (fn [board position]
                   (set-tile board position :bomb? true))
        board (reduce add-bomb fresh-board bomb-positions)
        board (mark-neighbor-counts board)]
    (swap! state assoc :board board
                       :mode :playing)))

(defn detonate!
  [tile]
  (swap! state update-in [:board]
         set-tile tile :detonated? true)
  (swap! state assoc :mode :dead))

(defn count-neighboring
  [board tile]
  (let [neighbors (get-neighbors board tile)]
    (count (keep :bomb? neighbors))))

(defn mark-neighbor-counts
  [board]
  (let [mark-neighbor-count (fn [tile]
                              (assoc tile :neighboring-bombs
                                     (count-neighboring board tile)))]
    (map-tiles board mark-neighbor-count)))

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

(defn clear-tile!
  "return an event handler that clears the provided tile when the event fires"
  [{:keys [x y] :as tile}]
  (fn [_]
    (if (:bomb? tile)
        (detonate! tile)
        (swap! state update-in [:board] clear-tile tile))))

