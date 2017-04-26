(ns minesweeper.state
  (:require
   [minesweeper.board :refer [make-board get-neighbors
                              set-tile update-tile map-tiles]]
   [minesweeper.random :refer [random-combination floor]]))

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

(defn- place-bombs
  [{:keys [width height] :as board} bomb-count]
  (let [int->coordinate (fn [n]
                          (let [x (mod n width)
                                y (floor (/ n width))]
                            {:x x :y y}))

        tile-count (* width height)
        positions (random-combination tile-count bomb-count)
        bomb-coordinates (map int->coordinate positions)

        add-bomb (fn [board position]
                   (set-tile board position :bomb? true))]
    (reduce add-bomb board bomb-coordinates)))

(defn new-game
  [state]
  (let [{:keys [width height]} (:board state)
        fresh-board (make-board width height)
        board (place-bombs fresh-board (:bomb-count state))
        board (mark-neighbor-counts board)]
    (assoc state :board board :mode :playing :flag-count 0)))

(defn- clear-board-tile
  [board tile]
  (if (:cleared? tile)
    state
    (let [flood-safe  ; flood-fill to find `safe` tiles to clear
          (fn flood-safe [acc tile]
            (cond
             (acc tile) acc

             (= 0 (:neighboring-bombs tile)) ; safe: apply to all neighbors
             (reduce flood-safe (merge acc tile)
                                (get-neighbors board tile))

             :else (merge acc tile)))

          clear (fn [board tile]
                  (-> board
                      (set-tile tile :cleared? true)
                      (set-tile tile :flagged? false)))]
      (reduce clear board (flood-safe #{} tile)))))

(declare won?)

(defn clear-tile
  [{:keys [board] :as state} tile]
  (let [cleared-board (clear-board-tile board tile)
        new-state (assoc state :board cleared-board)]
    (if (won? new-state)
      (assoc new-state :mode :won)
      new-state)))

(def get-tiles minesweeper.board/get-tiles)

(defn total-tiles
  [board]
  (* (:width board) (:height board)))

(defn flagged-tiles
  [{:keys [board] :as state}]
  (:flag-count state))

(defn cleared-tiles
  [{:keys [board] :as state}]
  (count (filter :cleared? (get-tiles board))))

(defn won?
  [state]
  (let [cleared (cleared-tiles state)
        total (total-tiles (:board state))]
    (= cleared (- total (:bomb-count state)))))

