(ns groceries-list-planner.ingredients)

;; Scale recipe ingredients by number of people.
(defn scale-ingredients [recipe people]
  (let [ratio (/ people (:servings recipe))]
    ;; Map over each ingredient and scale :amount by ratio.
    (map (fn [ingredient]
           (update ingredient :amount * ratio))
         (:ingredients recipe))))

;; Collect all scaled ingredients from the week plan into one list.
(defn week-plan-ingredients [recipes week-plan]
  (if (empty? week-plan)
    ;; Base case: no days left.
    []
    (let [day (first week-plan)
          ings (scale-ingredients (get recipes (:recipe day)) (:people day))]
      ;; Concat first day's ingredients with the rest (recursive).
      (concat ings (week-plan-ingredients recipes (rest week-plan))))))

;; Aggregate ingredients by name, unit and category; sum amounts for duplicates.
(defn aggregate-ingredients [ingredients]
  ;; key-fn destructures :name, :unit, :category from each ingredient.
  (let [key-fn (fn [{:keys [name unit category]}]
                 [name unit category])]
    ;; ->> threads value through each form as last argument.
    (->> ingredients
         (group-by key-fn)
         (map (fn [[_ group]]
                ;; Sum :amount for this group, then assoc back into first item.
                (let [total (reduce + (map :amount group))]
                  (assoc (first group) :amount total)))))))

;; Group ingredients by :category.
(defn group-by-category [ingredients]
  (group-by :category ingredients))

;; Build shopping list from week plan and recipes.
(defn shopping-list [recipes week-plan]
  ;; ->> pipeline: all ingredients → aggregate → group by category.
  (->> (week-plan-ingredients recipes week-plan)
       (aggregate-ingredients)
       (group-by-category)))