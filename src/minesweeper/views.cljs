(ns minesweeper.views
    (:require [quiescent.core :refer-macros [defcomponent]]
              [quiescent.dom :as dom]
              [minesweeper.state :refer [get-tiles won? flagged-tiles]]
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
  (dom/g {} (TileLabel "@" "red")
            (TileLabel "#" "black")
            (TileLabel "$" "yellow")
            (TileLabel "X" "darkred")))

(defcomponent Tile
  :keyfn (fn [[{:keys [x y]} _]]
           [x y])

  [[tile mode]]
  (let [x (* tile-width (:x tile))
        y (* tile-width (:y tile))
        cleared? (:cleared? tile)
        labeled? ((some-fn :flagged? :cleared? :detonated?) tile)]
    (dom/g {:transform (str "translate(" x "," y ")")}
     (when (:flagged? tile)
       (if (and (= mode :dead) (not (:bomb? tile)))
         (dom/g {}
                (TileLabel "F" "red")
                (TileLabel "x" "green"))
         (TileLabel "F" "red")))
     (when (:detonated? tile)
       (Exploded))
     (when (and cleared? (not= (:neighboring-bombs tile) 0))
       (TileLabel (:neighboring-bombs tile) "black"))
     (dom/rect {:width tile-width
                :height tile-width
                :fill "grey"
                :opacity (if labeled? 0.3 1)
                :stroke "darkgrey"
                :strokeWidth (* 0.05 tile-width)
                :onClick (when (and (not cleared?) (not (:flagged? tile)))
                           (dispatch/clear-tile! tile))
                :onContextMenu (when-not cleared?
                                 (dispatch/flag-tile! tile))}))))

(defcomponent Label
  [{:keys [text font-size text-color
           width height x y fill opacity
           attrs]}]
  (dom/svg {:width width :height height :x x :y y}
              (dom/rect {:width "100%" :height "100%"
                         :fill fill
                         :opacity opacity})
              (when text
                (dom/text {:y "70%" :x "50%"
                           :textAnchor "middle"
                           :alignmentBaseline "middle"
                           :fontSize font-size
                           :fill text-color}
                          text))
              (dom/rect (merge {:width "100%" :height "100%" :opacity 0}
                               attrs))))

(defcomponent Overlay
  [mode]
  (let [winner? (= mode :won)
        msg (if winner? "!!! You Win :) !!!" "You Lost :(")]
    (dom/g
     {}
     (dom/rect {:width "100%" :height "100%"
                :opacity 0})
     (Label {:x "25%" :y "25%" :width "50%" :height "15%"
             :fill "#cbb" :opacity 0.8
             :text msg
             :font-size "500%"
             :text-color (if winner? "green" "black")})

     (Label {:x "30%" :y "40%" :width "40%" :height "15%"
             :fill "lightgrey" :opacity 0.8
             :text "Replay"
             :font-size "400%"
             :attrs {:onClick #(dispatch/init-board!)}}))))

(defcomponent Board
  [{:keys [board mode]}]
  (let [tiles (get-tiles board)
        width  (* tile-width (:width board))
        height (* tile-width (:height board))
        viewBox (str "0 0 " width " " height)]
    (dom/svg {:width 650 :height 800
              :viewBox viewBox
              }
      (apply dom/g {}
             (map #(Tile [% mode]) tiles))
      (when (not= mode :playing)
        (Overlay mode)))))

(defcomponent Main-panel
  [{:keys [mode board]:as state}]
  (let [flagged (flagged-tiles state)
        total-bombs (:bomb-count state)]
    (dom/div {}
     flagged "/" total-bombs " mines flagged"
     (dom/p)
     (Board {:board board :mode mode})
     (dom/p)
     "Your state is: " (pr-str (update state :board dissoc :tiles)))))
