(ns minesweeper.views
    (:require [quiescent.core :refer-macros [defcomponent]]
              [quiescent.dom :as dom]
              [minesweeper.state :refer [won? flagged-tiles]]
              [minesweeper.board :refer [get-tiles]]
              [minesweeper.dispatch :as dispatch]))

(def tile-width 100)

(defcomponent Label
  [{:keys [text font-size text-color
           width height x y fill opacity
           attrs]}]
  (dom/svg {:width width :height height :x x :y y}
              (dom/rect {:width "100%" :height "100%"
                         :fill fill
                         :opacity opacity})
              (when text
                (dom/text {:y "80%" :x "50%"
                           :textAnchor "middle"
                           :alignmentBaseline "middle"
                           :fontSize font-size
                           :fill text-color}
                          text))
              (dom/rect (merge {:width "100%" :height "100%" :opacity 0}
                               attrs))))
(defcomponent Tile
  :keyfn (fn [[{:keys [x y]} _]]
           [x y])

  [[tile mode]]
  (let [x (* tile-width (:x tile))
        y (* tile-width (:y tile))
        spec-opts (cond (and (:cleared? tile)
                             (> (:neighboring-bombs tile) 0))
                        {:text (:neighboring-bombs tile) :attrs nil}

                        (:cleared? tile)
                        {:attrs nil}

                        (and (= mode :dead) (:flagged? tile) (not (:bomb? tile)))
                        {:text "F" :text-color "green"}

                        (:flagged? tile)
                        {:text "F" :text-color "red"
                         :attrs {:onContextMenu (dispatch/flag-tile! tile)}}

                        (:detonated? tile)
                        {:text "\\b/"
                         :text-color "darkred"
                         :font-size (* 0.6 tile-width)})]
    (Label (merge {:x x :y y
                   :width (* 0.9 tile-width)
                   :height (* 0.9 tile-width)
                   :fill "grey"
                   :opacity (if spec-opts 0.3 1)
                   :font-size (* 0.8 tile-width)
                   :attrs {:onClick (dispatch/clear-tile! tile)
                           :onContextMenu (dispatch/flag-tile! tile)}}
                  spec-opts))))

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
