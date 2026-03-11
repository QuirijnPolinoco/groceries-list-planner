(ns groceries-list-planner.core
  (:require [clojure.string :as str]
            [groceries-list-planner.ingredients :as ingredients]
            [groceries-list-planner.recipe :as recipe]
            [groceries-list-planner.planning :as planning]))

(declare print-recipes printing-shopping-list)

;; Application entry point.
(defn -main [& _]
  (println "Type 'start' to begin planning your week")
  ;; Read one trimmed line from the user.
  (let [input (str/trim (read-line))]
    (when (= (str/lower-case input) "start")
      (print-recipes)
      (println)
      ;; Build a map from recipe names to ids for quick lookup.
      (let [name->id (recipe/recipe-name->id-map recipe/recipes)]
        ;; Local helpers for the main interaction loop.
        (letfn [(plan-loop []
                  ;; Build the week plan, show it, then ask for confirmation.
                  (let [week-plan (planning/collect-week-plan name->id)]
                    (println)
                    (planning/format-week-plan week-plan recipe/recipes)
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
; (defn run []
;  (-main))

;; Print the shopping list grouped by category.
(defn printing-shopping-list [shopping-list]
  ;; Loop over all categories.
  (doseq [cat (sort (keys shopping-list))]
    (println (str "== " (name cat) " =="))
    ;; Loop over all items in the current category.
    (doseq [i (get shopping-list cat)]
      (println (str "  " (ingredients/format-amount (:amount i)) " " (name (:unit i)) " " (:name i))))
    (println)))

;; Print all available recipes.
(defn print-recipes []
  (println "Recipes:")
  (doseq [[_ r] recipe/recipes]
    (println (str " - " (:name r)))))