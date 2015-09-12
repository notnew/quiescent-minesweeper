(defproject minesweeper "0.1.0-SNAPSHOT"

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.122"]
                 [quiescent "0.2.0-alpha1"]]

  :plugins [[lein-figwheel "0.4.0-SNAPSHOT"]]

  :clean-targets ^{:protect false} [:target-path "out" "resources/public/cljs"]

  :cljsbuild
    { :builds [{:id "dev"
                :source-paths ["src"]
                :figwheel true
                :compiler {:main "minesweeper.core"
                           :asset-path "cljs/out"
                           :output-to "resources/public/cljs/main.js"
                           :output-dir "resources/public/cljs/out"
                          }
               }]
    }

  :figwheel
  { :css-dirs ["resources/public/css"] }
)
