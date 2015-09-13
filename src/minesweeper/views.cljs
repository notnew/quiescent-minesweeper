(ns minesweeper.views
    (:require [quiescent.core :refer-macros [defcomponent]]
              [quiescent.dom :as dom]
              [minesweeper.dispatch :as dispatch]))

(def tile-width 100)

(defcomponent Tile
  :keyfn (fn [{:keys [x y]}]
           [x y])

  [{:keys [x y]}]
  (dom/rect {:x (* x tile-width)
             :y (* y tile-width)
             :width (* 0.9 tile-width)
             :height (* 0.9 tile-width)
             :fill "grey"}))

(defcomponent Background
  []
  (dom/rect {:width "100%" :height "100%" :fill "lightgrey"}))

(defcomponent Board
  [{:keys [tiles width height]}]
  (dom/svg {:width 400 :height 400
            :viewBox (str "0 0 " (* width tile-width) " " (* height tile-width))
            }
   (Background)
   (apply dom/g {}
           (map Tile (flatten tiles)))))

(defcomponent Main-panel
  [state]
  (dom/div {}
     "Hello from minesweeper project"
     (Board (:board state))
     (dom/p)
     "Your state is: " (pr-str state)))
