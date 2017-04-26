(ns minesweeper.dispatch
    (:require [minesweeper.state :refer [state new-game clear-tile]]
              [minesweeper.board :refer [set-tile get-tile update-tile]]))

(defn init-board! []
  (swap! state new-game))

(defn detonate!
  [tile]
  (swap! state update-in [:board]
         set-tile tile :detonated? true)
  (swap! state assoc :mode :dead))

(defn clear-tile!
  "return an event handler that clears the provided tile when the event fires"
  [{:keys [x y] :as tile}]
  (fn [_]
    (if (:bomb? tile)
        (detonate! tile)
        (swap! state clear-tile tile))))

(defn flag-tile!
  "return an event handler that clears the provided tile when the event fires"
  [{:keys [x y] :as tile}]
  (fn [event]
    (.preventDefault event)
    (let [new-state (swap! state update-in [:board] update-tile tile :flagged? not)
          added-flag? (:flagged? (get-tile (:board new-state) tile))
          update-count (if added-flag? inc dec)]
      (swap! state update :flag-count update-count))))

