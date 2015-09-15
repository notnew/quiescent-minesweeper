(ns minesweeper.random)

(defn floor [n]
  (.floor js/Math n))

(defn random []
  (.random js/Math))

(defn random-int
  "choose a random int between min (inclusive) and max (exclusive)"
  ([max] (random-int 0 max))
  ([min max]
     {:pre [(<= min max)]}
     (let [range (- max min)]
       (-> (random)
           (* range)
           floor
           (+ min)))))

(declare add-choice)

(defn random-combination
  "pick a random combination of k elements from n total elements
   `n' and `k' are ints
   returns a vector of ints representing the elements chosen
   each of the n!/(k!(n-k)!) possible combinations should have an equal chance
   of being returned
  "
  [n k]
  (let [choices (for [i (range k)]
                  (random-int (- n i)))]
    (reduce add-choice [] choices)))

(defn- add-choice
  "add the `new-choice'th unchosen int to `choices'
   `choices' is a sorted vector, inserts new-choice into correct spot in `choices'
      (add-choice [0 1 2 3 4 5 10] 2)  == [0 1 2 3 4 5 8 10]
      (add-choice [4 5] 0)  == [0 4 5]
      (add-choice [0 1 2 4] 0)  == [0 1 2 3 4]
      (add-choice [0 3] 1)  == [0 2 3]
      (add-choice [0 3] 4)  == [0 3 6]
  "
  [choices new-choice]
  (loop [acc []
         new new-choice
         choices choices]
    (cond
     (not (seq choices))  (conj acc new)

     (< new (first choices))
     (into (conj acc new) choices)

     :else (recur (conj acc (first choices))
                  (+ new 1)
                  (rest choices)))))

