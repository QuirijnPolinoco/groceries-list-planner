(ns groceries-list-planner.ingredients)

(defn scale-ingredients [recipe people]
  (let [ratio (/ people (:servings recipe))]
    (map (fn [ingredient]
           (update ingredient :amount * ratio))
         (:ingredients recipe))))

(defn week-plan-ingredients [recipes week-plan]
  (if (empty? week-plan)
    []
    (let [day (first week-plan)
          ings (scale-ingredients (get recipes (:recipe day)) (:people day))]
      (concat ings (week-plan-ingredients recipes (rest week-plan))))))

(defn aggregate-ingredients [ingredients]
  (let [key-fn (fn [{:keys [name unit category]}]
                 [name unit category])]
    (->> ingredients
         (group-by key-fn)
         (map (fn [[_ group]]
                (let [total (reduce + (map :amount group))]
                  (assoc (first group) :amount total)))))))