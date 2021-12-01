(defn binom [n k]
  (if (or (< k 0) (> k n)) 0)
  (if (or (= k 0) (= k n)) 1)
  (/
   (apply * (range (- (inc n) k) (inc n)))
   (apply * (range 1 (inc k))))
  )

(defn bernoulli [n]
  (if (= n 0) 1
      (- 0
         (apply + (for [k (range n)]
                    (/ (* (binom n k) (bernoulli k))
                       (- n (- k 1))))))))

(def n (Integer/parseInt (read-line)))
(println (bernoulli n))
