(def xs [3 12 55 60 61 70 2 9 50 51 100])

(defn square-of-events [xs]
  (map (fn [x] (* x x)) xs))

(defn sum-gt-50 [xs]
  (reduce + (filter (fn [x] (> x 50)) xs)))

(defn freqs [xs]
  (reduce (fn [acc x]
            (assoc acc x (inc (get acc x 0))))
          {}
          xs))

(defn my-map [f xs] 
  (reduce (fn [acc x]
            (conj acc (f x)))
          []
          xs))

(assert (= (square-of-events xs)
           [9 144 3025 3600 3721 4900 4 81 2500 2601 10000]))
(assert (= (sum-gt-50 xs) 397))
(assert (= (freqs [1 1 2 3 3 3]) {1 2, 2 1, 3 3}))
(assert (= (my-map inc [1 2 3]) [2 3 4]))
