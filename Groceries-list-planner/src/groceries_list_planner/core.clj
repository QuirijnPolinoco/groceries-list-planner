;; This namespace contains the main entry point and user interaction logic.
;; It orchestrates the planning flow but delegates domain logic to other namespaces.
(ns groceries-list-planner.core
  (:require [clojure.string :as str]
            [groceries-list-planner.ingredients :as ingredients]
            [groceries-list-planner.recipe :as recipe]
            [groceries-list-planner.planning :as planning]))

;; Forward declarations so these functions can be used before they are defined.
(declare print-recipes recipe-name->id-map collect-week-plan format-week-plan printing-shopping-list)

;; Application entry point.
(defn -main [& _]
  (println "Type 'start' to begin planning your week")
  ;; Read one trimmed line from the user.
  (let [input (str/trim (read-line))]
    (when (= (str/lower-case input) "start")
      (print-recipes)
      (println)
      ;; Build a map from recipe names to ids for quick lookup.
      (let [name->id (recipe-name->id-map recipe/recipes)]
        ;; Local helpers for the main interaction loop.
        (letfn [(plan-loop []
                  ;; Build the week plan, show it, then ask for confirmation.
                  (let [week-plan (collect-week-plan name->id)]
                    (println)
                    (format-week-plan week-plan recipe/recipes)
                    (confirm-loop week-plan)))
                (confirm-loop [week-plan]
                  (println "Is this correct? (yes/no)")
                  (let [answer (str/trim (str/lower-case (read-line)))]
                    (cond
                      (= answer "yes")
                      (let [list (ingredients/shopping-list recipe/recipes week-plan)]
                        (println)
                        (printing-shopping-list list))
                      (= answer "no")
                      (do (println "Let's try again.")
                          (plan-loop))
                      :else
                      (do (println "Please type 'yes' or 'no'.")
                          (confirm-loop week-plan)))))]
          (plan-loop))))))

;; Convenience function to start the app from the REPL.
(defn run []
  (-main))

;; Convert a numeric amount into a display string.
(defn format-amount [amount]
  (let [n (double amount)]
    (if (== n (long n))
      (str (long n))
      (str/replace (format "%.1f" n) "." ","))))

;; Print the shopping list grouped by category.
(defn printing-shopping-list [shopping-list]
  ;; Loop over all categories.
  (doseq [cat (sort (keys shopping-list))]
    (println (str "== " (name cat) " =="))
    ;; Loop over all items in the current category.
    (doseq [i (get shopping-list cat)]
      (println (str "  " (format-amount (:amount i)) " " (name (:unit i)) " " (:name i))))
    (println)))

;; Build a map from lower‑cased recipe names to recipe ids.
(defn recipe-name->id-map [recipes]
  (into {}
        (map (fn [[id recipe]]
               [(.toLowerCase ^String (:name recipe)) id])
             recipes)))

;; Print all available recipes.
(defn print-recipes []
  (println "Recipes:")
  (doseq [[_ r] recipe/recipes]
    (println (str " - " (:name r)))))

;; Parse a raw input line into a meal map.
(defn meal-input [input name->id]
  ;; when-let first binds `parts`; the body is only executed when `parts` is not nil/false.
  (when-let [parts (and input (seq (str/split (str/trim input) #",")))]
    (let [name-part (str/trim (first parts))
          people-part (str/trim (second parts))
          recipe-id (get name->id (str/lower-case name-part))
          people (when people-part (parse-long people-part))]
      (when (and recipe-id people (pos? people))
        {:recipe recipe-id :people people}))))

;; Ask the user for meals for each day and return a vector of meal maps.
(defn collect-week-plan
  ([name->id] (collect-week-plan name->id planning/week-days []))
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