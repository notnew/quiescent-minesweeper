(ns minesweeper.dispatch
    (:require [minesweeper.state :refer [state new-game clear-tile]]
              [minesweeper.board :refer [set-tile update-tile]]))

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
    (swap! state update-in [:board] update-tile tile :flagged? not)))

(defn no-op
  "return an event handler that clears the provided tile when the event fires"
  []
  (fn [event &_]
    (.preventDefault event)))

