(ns groceries-list-planner.recipe)

(def recipes
{ :meat-pasta {:id        :meat-pasta
 :name      "Meat pasta"
 :servings  2
 :ingredients [{:name "ricattoni pasta" :amount 250 :unit :g :category :grains}
               {:name "minced beef" :amount 300 :unit :g :category :meat}
               {:name "tomato paste" :amount 120 :unit :g :category :canned}
               {:name "tomato cubes" :amount 400 :unit :g :category :canned}
               {:name "onion" :amount 1 :unit :pcs :category :vegetable}
               {:name "garlic" :amount 2 :unit :pcs :category :vegetable}
               {:name "olive oil" :amount 5 :unit :ml :category :oil}
               {:name "salt" :amount 4 :unit :g :category :spice}
               {:name "pepper" :amount 2 :unit :g :category :spice}
               {:name "basil" :amount 15 :unit :g :category :spice}
               {:name "grated cheese" :amount 50 :unit :g :category :dairy}]}

 :green-curry {:id        :green-curry
 :name      "Green curry"
 :servings  2
 :ingredients [{:name "chicken" :amount 300 :unit :g :category :meat}
               {:name "green curry paste" :amount 120 :unit :g :category :canned}
               {:name "coconut milk" :amount 500 :unit :ml :category :canned}
               {:name "onion" :amount 1 :unit :pcs :category :vegetable}
               {:name "garlic" :amount 2 :unit :pcs :category :vegetable}
               {:name "olive oil" :amount 5 :unit :ml :category :oil}
               {:name "salt" :amount 4 :unit :g :category :spice}
               {:name "coriander" :amount 5 :unit :g :category :spice}
               {:name "green beans" :amount 400 :unit :g :category :vegetable}
               {:name "rice" :amount 200 :unit :g :category :grains}]}

 :tacos {:id        :tacos
 :name      "tacos"
 :servings  2
 :ingredients [{:name "tortillas" :amount 6 :unit :pcs :category :grains}
               {:name "minced beef" :amount 300 :unit :g :category :meat}
               {:name "tomato cubes" :amount 200 :unit :g :category :canned}
               {:name "onion" :amount 1 :unit :pcs :category :vegetable}
               {:name "garlic" :amount 2 :unit :pcs :category :vegetable}
               {:name "olive oil" :amount 5 :unit :ml :category :oil}
               {:name "salt" :amount 4 :unit :g :category :spice}
               {:name "pepper" :amount 2 :unit :g :category :spice}
               {:name "coriander" :amount 5 :unit :g :category :spice}
               {:name "grated cheese" :amount 100 :unit :g :category :dairy}]}

 :chicken-orzo {:id        :chicken-orzo
 :name      "Chicken orzo"
 :servings  2
 :ingredients [{:name "chicken breasts" :amount 300 :unit :g :category :meat}
               {:name "orzo pasta" :amount 200 :unit :g :category :grains}
               {:name "lemon" :amount 1 :unit :pcs :category :vegetable}
               {:name "dijon mustard" :amount 30 :unit :g :category :canned}
               {:name "garlic" :amount 3 :unit :pcs :category :vegetable}
               {:name "olive oil" :amount 10 :unit :ml :category :oil}
               {:name "chicken broth" :amount 500 :unit :ml :category :canned}
               {:name "salt" :amount 4 :unit :g :category :spice}
               {:name "pepper" :amount 2 :unit :g :category :spice}
               {:name "parmesan cheese" :amount 40 :unit :g :category :dairy}]}

 :chicken-gnocchi {:id        :chicken-gnocchi
 :name      "Chicken gnocchi"
 :servings  2
 :ingredients [{:name "chicken thighs" :amount 300 :unit :g :category :meat}
               {:name "potato gnocchi" :amount 250 :unit :g :category :grains}
               {:name "sun-dried tomatoes" :amount 60 :unit :g :category :canned}
               {:name "spinach" :amount 100 :unit :g :category :vegetable}
               {:name "garlic" :amount 3 :unit :pcs :category :vegetable}
               {:name "olive oil" :amount 10 :unit :ml :category :oil}
               {:name "chicken broth" :amount 600 :unit :ml :category :canned}
               {:name "salt" :amount 4 :unit :g :category :spice}
               {:name "pepper" :amount 2 :unit :g :category :spice}
               {:name "cream" :amount 80 :unit :ml :category :dairy}]}

 :honey-chicken {:id        :honey-chicken
 :name      "Honey garlic chicken"
 :servings  2
 :ingredients [{:name "chicken thighs" :amount 300 :unit :g :category :meat}
               {:name "honey" :amount 50 :unit :g :category :canned}
               {:name "soy sauce" :amount 40 :unit :ml :category :canned}
               {:name "garlic" :amount 3 :unit :pcs :category :vegetable}
               {:name "olive oil" :amount 10 :unit :ml :category :oil}
               {:name "rice" :amount 200 :unit :g :category :grains}
               {:name "salt" :amount 3 :unit :g :category :spice}
               {:name "pepper" :amount 2 :unit :g :category :spice}]}

 :chicken-pitas {:id        :chicken-pitas
 :name      "Chicken pitas"
 :servings  2
 :ingredients [{:name "chicken" :amount 350 :unit :g :category :meat}
               {:name "pita bread" :amount 4 :unit :pcs :category :grains}
               {:name "Greek yogurt" :amount 80 :unit :g :category :dairy}
               {:name "cucumber" :amount 1 :unit :pcs :category :vegetable}
               {:name "tomato" :amount 1 :unit :pcs :category :vegetable}
               {:name "garlic" :amount 3 :unit :pcs :category :vegetable}
               {:name "olive oil" :amount 15 :unit :ml :category :oil}
               {:name "oregano" :amount 2 :unit :g :category :spice}
               {:name "salt" :amount 4 :unit :g :category :spice}
               {:name "pepper" :amount 2 :unit :g :category :spice}]}})