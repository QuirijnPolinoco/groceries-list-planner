Verslag Paradigma

1. Inleiding
2. Onderzoek
3. Challenge
4. Implementatie
5. Reflectie
6. Conclusie
7. Bronnen

## 3. Challenge

Voor deze opdracht heb ik gekozen om een functionele boodschappenplanner te ontwikkelen in Clojure. Het doel van het programma is om automatisch een complete en gestructureerde boodschappenlijst te genereren op basis van een dataset met recepten en een weekplanning die de gebruiker via de terminal invoert. Per dag kan de gebruiker aangeven welk recept wordt bereid en voor hoeveel personen er gekookt wordt.

Op basis van deze planning selecteert het programma de benodigde recepten en verzamelt het alle bijbehorende ingrediënten. Wanneer een recept meerdere keren voorkomt of voor meer personen wordt gemaakt, worden de hoeveelheden automatisch opgeschaald. Ingrediënten die in meerdere recepten voorkomen, worden samengevoegd tot één totaalhoeveelheid. Het eindresultaat wordt vervolgens gegroepeerd per supermarkt-categorie, zodat de boodschappenlijst logisch is ingedeeld tijdens het winkelen.

## 4. Implementatie

De applicatie is opgedeeld in vier namespaces:

- [core](../Groceries-list-planner/src/groceries_list_planner/core.clj) (entree en gebruikersinteractie)
- [recipe](../Groceries-list-planner/src/groceries_list_planner/recipe.clj) (recepten en naam→id-mapping)
- [planning](../Groceries-list-planner/src/groceries_list_planner/planning.clj) (invoer en opbouw van de weekplanning)
- [ingredients](../Groceries-list-planner/src/groceries_list_planner/ingredients.clj) (schalen, aggregeren en opbouwen van de boodschappenlijst)

Flow: de gebruiker start in core, kiest per dag een recept en aantal personen (planning), bevestigt de planning, waarna de boodschappenlijst wordt berekend (ingredients) en geprint (core).

### Zuiverheid (pure functions)

- **Waar:** In [ingredients](../Groceries-list-planner/src/groceries_list_planner/ingredients.clj) (scale-ingredients, week-plan-ingredients, aggregate-ingredients, shopping-list, format-amount), [recipe](../Groceries-list-planner/src/groceries_list_planner/recipe.clj) (recipe-name->id-map) en [planning](../Groceries-list-planner/src/groceries_list_planner/planning.clj) (meal-input).
- **Waarom hier:** Deze functies doen alleen datatransformatie:zelfde invoer geeft altijd dezelfde uitvoer, geen neveneffecten.

Het grootste deel van de logica is puur: o.a. [scale-ingredients](../Groceries-list-planner/src/groceries_list_planner/ingredients.clj), [week-plan-ingredients](../Groceries-list-planner/src/groceries_list_planner/ingredients.clj), [aggregate-ingredients](../Groceries-list-planner/src/groceries_list_planner/ingredients.clj), [shopping-list](../Groceries-list-planner/src/groceries_list_planner/ingredients.clj), [format-amount](../Groceries-list-planner/src/groceries_list_planner/ingredients.clj), [recipe-name->id-map](../Groceries-list-planner/src/groceries_list_planner/recipe.clj) en [meal-input](../Groceries-list-planner/src/groceries_list_planner/planning.clj).

### Higher-order functions

- **Waar:** In [scale-ingredients](../Groceries-list-planner/src/groceries_list_planner/ingredients.clj) en [recipe-name->id-map](../Groceries-list-planner/src/groceries_list_planner/recipe.clj) (anonieme functie aan `map`), en in [aggregate-ingredients](../Groceries-list-planner/src/groceries_list_planner/ingredients.clj) (`group-by` met key-fn en `map` met een functie over groepen).
- **Waarom hier:** We willen over collecties transformeren zonder voor elke case een aparte loop te schrijven. Dit doen we door een functie door te geven aan `map`, `reduce` en `group-by`.

Functies worden als waarden gebruikt. In [scale-ingredients](../Groceries-list-planner/src/groceries_list_planner/ingredients.clj) en [recipe-name->id-map](../Groceries-list-planner/src/groceries_list_planner/recipe.clj) wordt een anonieme functie aan `map` gegeven; in [aggregate-ingredients](../Groceries-list-planner/src/groceries_list_planner/ingredients.clj) krijgt `group-by` een `key-fn` en wordt over de groepen gemapt met een functie die de hoeveelheden opsomt.

### Immutability

- **Waar:** In [collect-week-plan](../Groceries-list-planner/src/groceries_list_planner/planning.clj) (nieuw plan met `conj` en `assoc` bij elke stap) en in [week-plan-ingredients](../Groceries-list-planner/src/groceries_list_planner/ingredients.clj) (`concat` in plaats van een lijst aan te passen). Ook in [scale-ingredients](../Groceries-list-planner/src/groceries_list_planner/ingredients.clj) (`update` maakt een nieuw ingrediëntenmapje).
- **Waarom hier:** Er wordt nergens bestaande data gewijzigd; elke stap produceert een nieuwe waarde.

Er is geen mutabele state. Nieuwe waarden ontstaan via `conj`, `assoc` en `update`. Het weekplan wordt in [collect-week-plan](../Groceries-list-planner/src/groceries_list_planner/planning.clj) opgebouwd door bij elke stap een nieuw plan aan `recur` door te geven; in [week-plan-ingredients](../Groceries-list-planner/src/groceries_list_planner/ingredients.clj) worden lijsten geconcateneerd zonder bestaande data aan te passen.

### Recursie

- **Waar:** In [collect-week-plan](../Groceries-list-planner/src/groceries_list_planner/planning.clj) (tail-recursie met `recur` over de dagen) en in [week-plan-ingredients](../Groceries-list-planner/src/groceries_list_planner/ingredients.clj) (recursie over het weekplan: base case lege planning, anders eerste dag + recursie op de rest).
- **Waarom hier:** We moeten over een lijst (dagen of weekplan) lopen en iets accumuleren; in een functionele stijl doen we dat zonder loop-variabelen door bij elke stap op de rest te recurseren en het resultaat op te bouwen.

In [collect-week-plan](../Groceries-list-planner/src/groceries_list_planner/planning.clj) wordt met `recur` tail-recursief over de resterende dagen gelopen. In [week-plan-ingredients](../Groceries-list-planner/src/groceries_list_planner/ingredients.clj) is de recursie expliciet: base case als het weekplan leeg is, anders de ingrediënten van de eerste dag geconcateneerd met het resultaat van een aanroep op de rest van het weekplan.

### Lazy evaluation

- **Waar:** In alle gebruik van `map` in [ingredients](../Groceries-list-planner/src/groceries_list_planner/ingredients.clj) (o.a. [scale-ingredients](../Groceries-list-planner/src/groceries_list_planner/ingredients.clj), [aggregate-ingredients](../Groceries-list-planner/src/groceries_list_planner/ingredients.clj)) en in [recipe-name->id-map](../Groceries-list-planner/src/groceries_list_planner/recipe.clj); de `->>`-pipeline in [shopping-list](../Groceries-list-planner/src/groceries_list_planner/ingredients.clj) verbindt lazy stappen.
- **Waarom hier:** Clojure’s `map` en veel seq-functies retourneren lazy sequences: elementen worden pas berekend als ze worden opgevraagd.

`map` (en de pipelines in [ingredients](../Groceries-list-planner/src/groceries_list_planner/ingredients.clj)) werken op lazy sequences: elementen worden pas berekend wanneer ze nodig zijn.

### Threading (->>)

- **Waar:** In [aggregate-ingredients](../Groceries-list-planner/src/groceries_list_planner/ingredients.clj) (ingredients → group-by → map) en in [shopping-list](../Groceries-list-planner/src/groceries_list_planner/ingredients.clj) (week-plan-ingredients → aggregate-ingredients → group-by-category).
- **Waarom hier:** De datastroom is een opeenvolging van stappen; `->>` voegt elke uitkomst als laatste argument in de volgende vorm in. Daardoor leest de code van boven naar beneden in de volgorde van de transformaties, zonder diepe geneste aanroepen.

In [aggregate-ingredients](../Groceries-list-planner/src/groceries_list_planner/ingredients.clj) en [shopping-list](../Groceries-list-planner/src/groceries_list_planner/ingredients.clj) wordt `->>` gebruikt: het resultaat van elke stap gaat als laatste argument in de volgende.

## 5. Reflectie

## 6. Conclusie

## 7. Bronnen
