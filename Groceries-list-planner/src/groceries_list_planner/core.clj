;; This namespace contains the main entry point and user interaction logic.
;; It orchestrates the planning flow but delegates domain logic to other namespaces.
(ns groceries-list-planner.core
  (:require [clojure.string :as str]
            [groceries-list-planner.ingredients :as ingredients]
            [groceries-list-planner.recipe :as recipe]
            [groceries-list-planner.planning :as planning]))

(declare print-recipes recipe-name->id-map collect-week-plan format-week-plan printing-shopping-list)

;; -main is the entry point used by Clojure when running this application.
(defn -main [& _]
  (println "Type 'start' to begin planning your week")
  ;; This let reads one line from the user and stores the trimmed value in `input`,
  ;; so we do not have to call `read-line` multiple times.
  (let [input (str/trim (read-line))]
    (when (= (str/lower-case input) "start")
      (print-recipes)
      (println)
      ;; This let builds a map from recipe names to ids once, and passes it
      ;; to the rest of the flow so we can look up recipes quickly by name.
      (let [name->id (recipe-name->id-map recipe/recipes)]
        ;; letfn defines two local helper functions that can call each other:
        ;; - plan-loop: asks for the meals for all days and then shows the plan
        ;; - confirm-loop: asks the user to confirm or redo the plan
        (letfn [(plan-loop []
                  ;; Here we call collect-week-plan to build the plan,
                  ;; then print it and hand it over to confirm-loop.
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

;; Convenience function so you can start the app from a REPL by calling (run).
(defn run []
  (-main))

;; Converts a numeric amount into a string:
;; whole numbers like 2.0 become "2", others get one decimal and a comma as separator.
(defn format-amount [amount]
  (let [n (double amount)]
    (if (== n (long n))
      (str (long n))
      (str/replace (format "%.1f" n) "." ","))))

;; Prints the shopping list grouped by category.
;; Within each category it prints the amount, unit and name for every item.
(defn printing-shopping-list [shopping-list]
  (doseq [cat (sort (keys shopping-list))]
    (println (str "== " (name cat) " =="))
    (doseq [i (get shopping-list cat)]
      (println (str "  " (format-amount (:amount i)) " " (name (:unit i)) " " (:name i))))
    (println)))

;; Builds a map from lower‑cased recipe names to recipe ids,
;; so the user can type a name instead of an id.
(defn recipe-name->id-map [recipes]
  (into {}
        (map (fn [[id recipe]]
               [(.toLowerCase ^String (:name recipe)) id])
             recipes)))

;; Prints all available recipes so the user knows what they can choose from.
(defn print-recipes []
  (println "Recipes:")
  (doseq [[_ r] recipe/recipes]
    (println (str " - " (:name r)))))

;; Tries to turn a raw input line into a meal map.
;; Expects input in the form "Recipe name, number" and uses name->id to look up the recipe.
;; Returns nil if the input does not match this format.
(defn meal-input [input name->id]
  (when-let [parts (and input (seq (str/split (str/trim input) #",")))]
    (let [name-part (str/trim (first parts))
          people-part (str/trim (second parts))
          recipe-id (get name->id (str/lower-case name-part))
          people (when people-part (parse-long people-part))]
      (when (and recipe-id people (pos? people))
        {:recipe recipe-id :people people}))))

;; Asks the user what they want to eat on each day and with how many people.
;; Returns a vector of meal maps that include the day, recipe id and number of people.
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
         ;; When the input is valid, we add the meal (with the day added)
         ;; to the plan and continue with the remaining days.
         (recur name->id (rest days) (conj plan (assoc meal :day day)))
         (do (println "Invalid input. Use format: Recipe name, number")
             (recur name->id days plan)))))))

;; Prints a human‑readable overview of the week plan:
;; one line per meal, with the day, recipe name and number of people.
(defn format-week-plan [week-plan recipes]
  (doseq [meal week-plan]
    (let [day-name (str/capitalize (name (:day meal)))
          recipe-name (get-in recipes [(:recipe meal) :name])]
      (println (str day-name ": " recipe-name " (" (:people meal) " people)")))))