(ns minesweeper.state)

(defonce state (atom {:mine-count 10
                      :width 9
                      :height 9
                      }))


