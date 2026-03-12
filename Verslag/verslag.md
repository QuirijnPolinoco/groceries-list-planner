Verslag Paradigma

1. Inleiding
2. Onderzoek
3. Challenge
4. Implementatie
5. Reflectie
6. Conclusie
7. Bronnen

## 1. Inleiding

## 2. Onderzoek

Voor deze opdracht heb ik eerst onderzocht wat Clojure precies is en welke functionele ideeën centraal staan in de taal. Clojure is een modern Lisp‑dialect op de JVM, met de focus op immutable data en functies in plaats van objecten. In onder andere de officiële documentatie (`about/functional_programming`, `reference`, `reference/functions`) en de blogpost “Why Clojure, seriously why?” heb ik gelezen hoe Clojure zich positioneert als praktische, functionele taal voor “real‑world” applicaties.

In mijn onderzoek heb ik me vooral gericht op een paar concepten die belangrijk zijn voor het functionele paradigma én relevant zijn voor mijn boodschappenplanner: pure functies, recursie, higher‑order functies, immutability en lazy sequences. De Clojure‑guides over higher‑order functions en data structures lieten zien hoe je met functies als `map`, `filter` en `reduce` over collecties denkt zonder traditionele loops. De stukken over sequences en lazy evaluation maakten duidelijk dat veel Clojure‑functies lui werken: resultaten worden pas berekend als ze echt nodig zijn. De pagina’s over state en immutability benadrukten juist het idee dat je bestaande data niet aanpast, maar nieuwe versies opbouwt.

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

In deze opdracht heb ik bewust gekozen voor Clojure, omdat de syntax mij direct aansprak. De vele haakjes en de uniforme structuur van expressies zagen er op het eerste gezicht rustig en consistent uit, waardoor ik benieuwd werd hoe het zou zijn om er een volledige applicatie mee te bouwen. Nu ik daadwerkelijk met Clojure heb gewerkt, vind ik die syntax nog steeds prettig, omdat de code lineair en voorspelbaar oogt. Tegelijk merk ik dat het sluiten van alle haakjes soms wat onnatuurlijk voelt; vooral bij geneste expressies moet ik goed opletten waar een functie precies begint en eindigt.

Binnen deze paradigma-opdracht heb ik vooral geleerd om mijn probleem echt als een datastroom te zien: van recepten, naar weekplanning, naar ingrediënten, naar een uiteindelijke boodschappenlijst. In Clojure beschrijf ik die stappen met pure functies en onveranderlijke data, wat goed past bij het functionele denken dat in deze opdracht centraal stond. Ik vond het prettig om met pure functies te werken, omdat elke functie duidelijk een input en een verwachte output heeft. Daardoor kon ik relatief makkelijk achterhalen waar iets fout ging: als een resultaat niet klopte, wist ik dat het in één specifieke functie zat in plaats van in een wirwar van gedeelde state. Voor mij werkt dit goed om fouten op te sporen en te controleren of een functie echt doet wat hij hoort te doen, of toch net een ander resultaat oplevert dan ik verwachtte.

De functionele concepten die ik het fijnst vond om mee te werken, zijn recursie, higher‑order functies en zuivere functies. Recursie voelde in eerste instantie wat abstract, maar past eigenlijk heel logisch bij het stap voor stap verwerken van lijsten of dagen in een weekplanning. In plaats van handmatig een loop en tellervariabele te schrijven, denk ik nu eerder in termen van “basisgeval en daarna de rest”, wat goed aansluit bij hoe ik het probleem in mijn hoofd opdeel. Higher‑order functies vond ik prettig omdat ze veel standaardpatronen op collecties (zoals iets toepassen op elk element of groeperen) heel kort en expressief maken. Het idee dat functies zelf waarden zijn die je kunt doorgeven, maakt de code compacter en beter herbruikbaar. Zuivere functies vormen daarbij de basis: omdat ze geen verborgen neveneffecten hebben en alleen afhankelijk zijn van hun input, voelt het bouwen van ketens van zulke functies schoner en minder foutgevoelig dan de imperatieve aanpak die ik uit andere talen ken. Ook het feit dat er geen mutabele state is, maakt redeneren over de code makkelijker: fouten zijn vaker terug te leiden tot één functie die verkeerde output geeft, in plaats van een complex samenspel van veranderende variabelen.

Wat meer specifiek bij Clojure hoort, vond ik het ook interessant dat de taal sterk leunt op REPL‑gebaseerde ontwikkeling. Tegelijkertijd had ik daardoor ook praktische struggles: ik merkte dat het opzetten en goed werkend krijgen van terminal input voor de gebruiker lastiger was dan ik had verwacht, mede omdat ik zelf nog niet super ervaren ben met command line tools en de onderliggende tooling rond Clojure en WSL. Door met dit project bezig te zijn, heb ik gemerkt dat ik nog veel kan leren over de terminal en de tooling eromheen, en dat het me juist triggert om me daar in de toekomst verder in te verdiepen zodat ik dit soort functionele projecten nog soepeler kan ontwikkelen en testen.

## 6. Conclusie

## 7. Bronnen

Higginbotham, D. (z.d.-a). _Core functions in depth_. Brave Clojure, van https://www.braveclojure.com/core-functions-in-depth/

Higginbotham, D. (z.d.-b). _Do things_. Brave Clojure, van https://www.braveclojure.com/do-things/

Clojure. (z.d.-a). _Functional programming_. Clojure.org, van https://clojure.org/about/functional_programming

Clojure. (z.d.-b). _Reference_. Clojure.org, van https://clojure.org/reference

Clojure. (z.d.-c). _Functions_. Clojure.org, van https://clojure.org/reference/functions

Clojure. (z.d.-d). _Higher-order functions_. Clojure.org, van https://clojure.org/guides/higher_order_functions

Clojure. (z.d.-e). _Data structures_. Clojure.org, van https://clojure.org/reference/data_structures

Clojure. (z.d.-f). _Sequences_. Clojure.org, van https://clojure.org/reference/sequences

Clojure. (z.d.-g). _Lazy_. Clojure.org, van https://clojure.org/reference/lazy

Clojure. (z.d.-h). _State_. Clojure.org, van https://clojure.org/about/state

Clojure core team. (z.d.). _clojure/core.match_ [GitHub-repository]. GitHub, van https://github.com/clojure/core.match

Clojurepatterns.com. (z.d.). _Clojure patterns_, van https://clojurepatterns.com/

ertu.ctn. (z.d.). _Why Clojure, seriously, why?_ Medium, van https://medium.com/@ertu.ctn/why-clojure-seriously-why-9f5e6f24dc29

OpenAI. (2026, 12 maart). _ChatGPT-gesprek over Clojure namespace error_ [AI-chat], van https://chatgpt.com/share/69b344d0-41c0-8004-8552-3ba0edec1fd4

OpenAI. (2026, 12 maart). _ChatGPT-gesprek over Linux username validation_ [AI-chat], van https://chatgpt.com/share/69b344b6-0134-8004-83fb-5fb687cdcaec
