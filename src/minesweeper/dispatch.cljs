(ns minesweeper.dispatch
    (:require [minesweeper.state :refer [state]]
              ))

(defonce counter (atom 0))

(defn- uniq-id []
  (swap! counter inc))

