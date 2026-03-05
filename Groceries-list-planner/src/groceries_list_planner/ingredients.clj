(ns groceries-list-planner.ingredients)

;; Scale ingredients with the amount of people that will eat the recipe on that day
(defn scale-ingredients [recipe people]
  (let [ratio (/ people (:servings recipe))]
    (map (fn [ingredient]
           (update ingredient :amount * ratio))
         (:ingredients recipe))))

;; Puts all ingredients from each day into a single list for the week
(defn week-plan-ingredients [recipes week-plan]
  (if (empty? week-plan)
    []
    (let [day (first week-plan)
          ings (scale-ingredients (get recipes (:recipe day)) (:people day))]
      (concat ings (week-plan-ingredients recipes (rest week-plan))))))

;; Aggregates ingredients by name, unit and category
(defn aggregate-ingredients [ingredients]
  (let [key-fn (fn [{:keys [name unit category]}]
                 [name unit category])]
    (->> ingredients
         (group-by key-fn)
         (map (fn [[_ group]]
                (let [total (reduce + (map :amount group))]
                  (assoc (first group) :amount total)))))))

;; Groups ingredients by category
(defn group-by-category [ingredients]
  (group-by :category ingredients))

;; Creates a shopping list from the week plan and recipes
(defn shopping-list [recipes week-plan]
  (->> (week-plan-ingredients recipes week-plan)
       (aggregate-ingredients)
       (group-by-category)))