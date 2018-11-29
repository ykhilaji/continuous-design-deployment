# SBT

[SBT](http://www.scala-sbt.org) est un outil de build dans l'eco-système scala. C'est une version plus évolouée de Maven pour le Java.
On peut également le comparer à Gradle toujours en java.

## build.sbt

Le fichier `build.sbt` décrit un programme. On retrouve plusieurs informations importantes dans ce fichier:

- informations sur le projet: nom, organisation, version
- les dépendances sur d'autres librairies
- les options de compilation
- les options de distribution
- la définition des différents modules du projet
- ...

Le fichier sbt est interprété comme un fichier scala. On peut ainsi y définir des variables et des fonctions.

## Plugins

Le comportement de sbt peut facilement être étendu pour rajouter de nouvelles fonctionnalités:

- génération de docker files
- récupération des informations de build pour mettre à disposition du code
- automatisation du process de release
- inspection de l'arbre de dépendance
- formattage automatique du code
- ...

Les plugins sont définis principalement dans le fichier `project/plugins.sbt` et ces plugins sont chargés avant le fichier `build.sbt`

## Commandes utiles

Pour la suite des commandes, nous ometterons le préfixe `sbt`. Par exemple, la commande `run` s'utilise depuis une ligne de commande bash avec `sbt run`

- `~<commande>`: lance la commande `<commande>` dès qu'un fichier est modifié. ex: `~run` ou `~test`
- `run`: lance la classe main du programme
- `compile`: compile les fichiers scala/java
- `test`: lance les tests
- `reload`: recharge la configuration de sbt. Utile après un changement dans `build.sbt` ou sur un plugin
- `console`: démarre une instance de la REPL avec les dépendances et les classes du programme. Utile pour tester rapidement un bout de code
- `consoleQuick`: démarre la REPL avec seulement les dépendances du projet
- `test:compile`: compile les tests

## Exemple: ajouter une dépendance

1. Récupérer la version de la dépendance: ex `"com.typesafe.play" %% "anorm" % "2.6.0"`
2. Modifier le fichier `build.sbt`: ajouter dans la clé `libraryDependencies` la nouvelle dépendance: `libraryDependencies += "com.typesafe.play" %% "anorm" % "2.6.0"`
3. Recharger sbt: `reload`
