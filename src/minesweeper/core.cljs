(ns minesweeper.core
  (:require [quiescent.core :as quiescent]
            [minesweeper.state :as state]
            [minesweeper.views :as views]
            ))

(def app-mount-point (.getElementById js/document "app"))

(defn render! []
  (quiescent/render (views/Main-panel @state/state) app-mount-point))

(render!)

(add-watch state/state :render-on-change (fn [_ _ _ _] (render!)))
