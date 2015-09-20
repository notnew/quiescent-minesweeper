(ns minesweeper.util)

(defonce counter (atom 0))

(defn- uniq-id []
  (swap! counter inc))

(def requestAnimationFrame
  (or (.. js/window -requestAnimationFrame)
      (.. js/window -webkitRequestAnimationFrame)
      (.. js/window -mozRequestAnimationFrame)
      (.. js/window -msRequestAnimationFrame)
      (fn [raf-callback]
        (js/setTimeout raf-callback 16))))

