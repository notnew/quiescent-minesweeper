(ns minesweeper.views
    (:require [quiescent.core :refer-macros [defcomponent]]
              [quiescent.dom :as dom]
              [minesweeper.dispatch :as dispatch]))

(defcomponent Tile
  :keyfn (fn [{:keys [x y]}]
           [x y])

  [{:keys [x y]}]
  (dom/rect {:x (* x 10)
             :y (* y 10)
             :width 9 :height 9
             :fill "grey"}))

(defcomponent Board
  [board]
  (apply dom/svg {}
         (map Tile (flatten (:tiles board)))))

(defcomponent Main-panel
  [state]
  (dom/div {}
     "Hello from minesweeper project"
     (Board (:board state))
     (dom/p)
     "Your state is: " (pr-str state)))
