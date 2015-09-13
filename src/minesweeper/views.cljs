(ns minesweeper.views
    (:require [quiescent.core :refer-macros [defcomponent]]
              [quiescent.dom :as dom]
              [minesweeper.dispatch :as dispatch]))

(def tile-width 100)

(defcomponent Tile
  :keyfn (fn [{:keys [x y]}]
           [x y])

  [{:keys [x y] :as tile}]
  (dom/g
   {}
   (dom/rect {:x (* x tile-width)
              :y (* y tile-width)
              :width tile-width
              :height tile-width
              :fill (if (:cleared? tile) "lightgrey" "grey")
              :stroke "darkgrey"
              :strokeWidth (* 0.05 tile-width)
              :onClick (dispatch/clear-tile! tile)
              }
             )
   (dom/text {:x (* (+ 0.25 x) tile-width)
              :y (* (+ 0.8 y) tile-width)
              :fontSize (* 0.8 tile-width) }
             (when-let [count (:cleared? tile)]
               (when (not= count 0)
                 count)))))

(defcomponent Board
  [{:keys [tiles width height]}]
  (dom/svg {:width 400 :height 400
            :viewBox (str "0 0 " (* width tile-width) " " (* height tile-width))
            }
   (apply dom/g {}
           (map Tile (flatten tiles)))))

(defcomponent Main-panel
  [state]
  (dom/div {}
     "Hello from minesweeper project"
     (Board (:board state))
     (dom/p)
     "Your state is: " (pr-str state)))
