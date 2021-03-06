(ns minesweeper.core
  (:require [quiescent.core :as quiescent]
            [minesweeper.state :as state]
            [minesweeper.views :as views]
            [minesweeper.util :refer [requestAnimationFrame]]
            ))

(def app-mount-point (.getElementById js/document "app"))

(defonce dirty? (atom false))

(defn render! []
  (reset! dirty? false)
  (quiescent/render (views/Main-panel @state/state) app-mount-point))

(add-watch state/state :set-dirty
           (fn [_ _ old new]
             (when (and (not @dirty?)
                        (not= old new))
               (reset! dirty? true))))

(add-watch dirty? :render-when-dirty
           (fn [_ _ old new]
             (when (and new (not old))  ; render if dirty? was just set
               (requestAnimationFrame render!))))

(render!)

