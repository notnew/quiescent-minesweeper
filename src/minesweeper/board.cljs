(ns minesweeper.board
  (:require [minesweeper.util :refer [uniq-id]]))

(defn- make-tile [i j]
  {:id (uniq-id)
   :x i
   :y j})

(defn- make-row [width j]
  (vec (for [i (range width)]
         (make-tile i j))))

(defn make-board
  [width height]
  (vec (for [j (range height)]
         (make-row width j))))

(defn get-tile
  [{:keys [x y]} board]
  (get-in board [y x]))

