(ns minesweeper.state)

(defonce state (atom {:mode :start
                      :bomb-count 10
                      :board {:width 9 :height 9}}))


