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

(defcomponent Overlay
  []
  (dom/g
   {}
   (dom/text {:y "50%" :x "7%"
              :fontSize (* 0.75 tile-width )}
       "Click for a new game")
   (dom/rect {:width "100%" :height "100%"
              :fill "grey"
              :opacity 0.5
              :onClick #(dispatch/init-board!)})))

(defcomponent Board
  [{:keys [board mode]}]
  (let [tiles (flatten (:tiles board))
        width  (* tile-width (:width board))
        height (* tile-width (:height board))
        viewBox (str "0 0 " width " " height)]
    (dom/svg {:width 400 :height 400
              :viewBox viewBox
              }
      (apply dom/g {}
             (map Tile tiles))
      (when (not= mode :playing)
        (Overlay)))))

(defcomponent Main-panel
  [{:keys [mode board]:as state}]
  (dom/div {}
     "Hello from minesweeper project"
     (Board {:board board :mode mode})
     (dom/p)
     "Your state is: " (pr-str state)))
