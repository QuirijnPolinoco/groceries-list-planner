;; Factorial
(defn fact [n]
  (loop [acc 1
         k n]
    (if (zero? k)
      acc
      (recur (* acc k) (dec k)))))

;; Fibonacci sequence
(defn fib [n] 
  (loop [i 0 
         a 0
         b 1]
    (if (= i n)
      a
      (recur (inc i) b (+ a b)))))


;; Greatest common divisor
(defn gcd [a b] 
  (loop [a a
         b b]
    (if (zero? b)
      a
      (recur b (mod a b)))))


;; Reverse array in place
(defn reverse-array [arr start end]
  (when (< start end)
    (let [temp (aget arr start)]
      (aset arr start (aget arr end))
      (aset arr end temp)
      (recur arr (inc start) (dec end)))))



;; Print from numeric input to binary output
(defn to-binary [n]
    (str (when (pos? (quot n 2))
           (to-binary (quot n 2)))
         (mod n 2)))

(assert (= (fact 0) 1))
(assert (= (fact 5) 120))
(assert (= (fib 0) 0))
(assert (= (fib 1) 1))
(assert (= (fib 10) 55))
(assert (= (gcd 54 24) 6))
(assert (= (gcd 1071 462) 21))
(let [a (int-array [1 2 3 4])]
  (reverse-array a 0 (dec (alength a)))
  (assert (= (vec a) [4 3 2 1])))
(assert (= (to-binary 13) "1101"))