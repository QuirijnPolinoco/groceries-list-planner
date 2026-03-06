(ns groceries-list-planner.core
  (:require [clojure.string :as str]
            [groceries-list-planner.ingredients :as ingredients]
            [groceries-list-planner.recipe :as recipe]
            [groceries-list-planner.planning :as planning]))

(declare print-recipes recipe-name->id-map collect-week-plan format-week-plan printing-shopping-list)

(defn -main [& _]
  (println "Type 'start' to begin planning your week")
  (let [input (str/trim (read-line))]
    (when (= (str/lower-case input) "start")
      (print-recipes)
      (println)
      (let [name->id (recipe-name->id-map recipe/recipes)]
        (letfn [(plan-loop []
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

(defn run []
  (-main))

(defn printing-shopping-list [shopping-list]
  (doseq [cat (sort (keys shopping-list))]
    (println (str "== " (name cat) " =="))
    (doseq [i (get shopping-list cat)]
      (println (str "  " (:amount i) " " (name (:unit i)) " " (:name i))))
    (println)))

(defn recipe-name->id-map [recipes]
  (into {}
        (map (fn [[id recipe]]
               [(.toLowerCase ^String (:name recipe)) id])
             recipes)))

(defn print-recipes []
  (println "Recipes:")
  (doseq [[_ r] recipe/recipes]
    (println (str " - " (:name r)))))

(defn meal-input [input name->id]
  (when-let [parts (and input (seq (str/split (str/trim input) #",")))]
    (let [name-part (str/trim (first parts))
          people-part (str/trim (second parts))
          recipe-id (get name->id (str/lower-case name-part))
          people (when people-part (parse-long people-part))]
      (when (and recipe-id people (pos? people))
        {:recipe recipe-id :people people}))))

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
         (recur name->id (rest days) (conj plan (assoc meal :day day)))
         (do (println "Invalid input. Use format: Recipe name, number")
             (recur name->id days plan)))))))

(defn format-week-plan [week-plan recipes]
  (doseq [meal week-plan]
    (let [day-name (str/capitalize (name (:day meal)))
          recipe-name (get-in recipes [(:recipe meal) :name])]
      (println (str day-name ": " recipe-name " (" (:people meal) " people)")))))