(defproject minesweeper "0.1.0-SNAPSHOT"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.521"]
                 [quiescent "0.3.2"]]

  :plugins [[lein-cljsbuild "1.1.6"]
            [lein-figwheel "0.5.10"]]

  :clean-targets ^{:protect false} [:target-path "out" "resources/public/cljs"]

  :cljsbuild
  { :builds [{:id "dev"
              :source-paths ["src"]
              :figwheel true
              :compiler {:main "minesweeper.core"
                         :asset-path "cljs/out"
                         :output-to "resources/public/cljs/main.js"
                         :output-dir "resources/public/cljs/out"}}
             {:id "release"
              :source-paths ["src"]
              :compiler {:main "minesweeper.core"
                         :output-to "docs/cljs/main.js"
                         :optimizations :whitespace
                         :pretty-print true}}]}

  :figwheel
  { :css-dirs ["resources/public/css"] }
)
