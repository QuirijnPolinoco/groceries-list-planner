(ns groceries-list-planner.planning
    (:require [clojure.string :as str]))

;; Vector of keywords representing the days of the week.
;; Used by collect-week-plan to know for which days it should ask the user for a meal.
(def week-days
  [:monday :tuesday :wednesday :thursday :friday :saturday :sunday])

;; Parse a raw input line into a meal map.
(defn meal-input [input name->id]
  ;; when-let first binds `parts`; the body is only executed when `parts` is not nil/false.
  ;; support for the user forgetting the ", number" part.
  (when-let [parts (and input
                        (seq (str/split (str/trim input) #",")))]
    (when (>= (count parts) 2)
      (let [name-part (str/trim (first parts))
            people-part (some-> (second parts) str/trim)
            recipe-id (get name->id (str/lower-case name-part))
            people (when people-part (parse-long people-part))]
        (when (and recipe-id people (pos? people))
          {:recipe recipe-id :people people})))))

;; Ask the user for meals for each day and return a vector of meal maps.
(defn collect-week-plan
  ([name->id] (collect-week-plan name->id week-days []))
  ([name->id days plan]
   (if (empty? days)
     plan
     (let [day (first days)
           day-name (str/capitalize (name day))
           _ (println (str "What would you like to eat on " day-name " and with how many?"))
           input (str/trim (read-line))
           meal (meal-input input name->id)]
       (if meal
         ;; Tail-recursive: recur continues with remaining days and updated plan.
         (recur name->id (rest days) (conj plan (assoc meal :day day)))
         (do (println "Invalid input. Use format: Recipe name, number")
             ;; Tail resursive if invalid
             (recur name->id days plan)))))))

;; Print a human‑readable overview of the week plan.
(defn format-week-plan [week-plan recipes]
  ;; Loop over all meals in the plan.
  (doseq [meal week-plan]
    (let [day-name (str/capitalize (name (:day meal)))
          ;; Look up the recipe name by id inside recipes.
          recipe-name (get-in recipes [(:recipe meal) :name])]
      (println (str day-name ": " recipe-name " (" (:people meal) " people)")))))