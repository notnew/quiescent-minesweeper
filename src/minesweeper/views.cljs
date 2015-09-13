(ns minesweeper.views
    (:require [quiescent.core :refer-macros [defcomponent]]
              [quiescent.dom :as dom]
              [minesweeper.dispatch :as dispatch]))

(def tile-width 100)

(defcomponent TileLabel
  [text color]
  (dom/text {:y (* 0.8 tile-width) :x (* 0.2 tile-width)
             :fontSize (* 0.8 tile-width)
             :fill color}
            text))

(defcomponent Exploded
  []
  (dom/g {} (TileLabel "@@" "red")
            (TileLabel "##" "black")
            (TileLabel "$%" "yellow")
            (TileLabel "X" "darkred")))

(defcomponent Tile
  :keyfn (fn [{:keys [x y]}]
           [x y])

  [tile]
  (let [x (* tile-width (:x tile))
        y (* tile-width (:y tile))]
    (dom/g {:transform (str "translate(" x "," y ")")}
     (dom/rect {:width tile-width
                :height tile-width
                :fill (if (:cleared? tile) "lightgrey" "grey")
                :stroke "darkgrey"
                :strokeWidth (* 0.05 tile-width)
                :onClick (dispatch/clear-tile! tile)})
     (when (:detonated? tile)
       (Exploded))
     (when-let [bomb-count (:cleared? tile)]
       (when (not= bomb-count 0)
         (TileLabel bomb-count "black"))))))

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
