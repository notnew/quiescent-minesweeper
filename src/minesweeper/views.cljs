(ns minesweeper.views
    (:require [quiescent.core :refer-macros [defcomponent]]
              [quiescent.dom :as dom]
              [minesweeper.dispatch :as dispatch]))

(defcomponent Main-panel
  [state]
  (dom/div {}
     "Hello from minesweeper project"
     (dom/p)
     "Your state is: " (pr-str state)))
