(ns minesweeper.util)

(defonce counter (atom 0))

(defn- uniq-id []
  (swap! counter inc))

