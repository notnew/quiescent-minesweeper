(ns minesweeper.board
  (:require [minesweeper.util :refer [uniq-id]]))

(defn- make-tile [i j]
  {:id (uniq-id)
   :x i
   :y j})

(defn- make-row [width j]
  (vec (for [i (range width)]
         (make-tile i j))))

(defn make-tiles
  [width height]
  (vec (for [j (range height)]
         (make-row width j))))

(defn make-board
  [width height]
  {:width width
   :height height
   :tiles (make-tiles width height)
   })

(defn get-tiles
  "return a sequence of all the tiles"
  [board]
  (flatten (:tiles board)))

(defn get-tile
  [board {:keys [x y]}]
  (get-in board [:tiles y x]))

(defn set-tile
  [board {:keys [x y] :as tile} key value]
  (assoc-in board [:tiles y x key] value))

(defn update-tile
  [board {:keys [x y] :as tile} key f]
  (update-in board [:tiles y x key] f))

(defn map-tiles
  "map a function f over a given board
   f takes a tile and returns a tile"
  [board f]
  (let [map-row (fn [row]
                  (vec (map f row)))
        tiles (vec (map map-row (:tiles board)))]
    (assoc board :tiles tiles)))

(defn count-tiles
  "Count the number of tiles on the board that satisfy the given predicate"
  [board pred?]
  (let [xf (comp cat
                 (filter pred?)
                 (map (constantly 1)))]
    (transduce xf + (:tiles board))))

(defn get-neighbors
  [board tile]
  (let [{:keys [x y]} tile
        neighbor-positions (for [i (range (- x 1) (+ x 2))
                                 j (range (- y 1) (+ y 2))]
                             {:x i :y j})]
    (when (and tile (get-tile board tile))
      (sequence (comp (keep #(get-tile board %))
                      (filter #(not= % tile)))
                neighbor-positions))))

