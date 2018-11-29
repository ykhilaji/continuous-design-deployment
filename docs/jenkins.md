# Jenkins

## Introduction

Jenkins est un outil pour toutes les tâches d'automatisation. Très souple et configurable, il permet de créer des flux complexes de tâches.
Il fonctionne sur le mode master/workers:

  - le master contient une liste des "jobs". Un job est une configuration et correspond souvent à une suite de commandes à exécuter.
  - les workers vont recevoir des ordres du master et vont exécuter les tâches. Les workers sont souvent des serveurs distincts du master.

## Connection

Pour accéder aux fonctionnalités, il faut disposer d'un compte sur le serveur Jenkins auquel on veut se connecter.
Ici nous partirons du postulat que le serveur jenkins est `https://jenkins.znx.fr`

## Les jobs sur Jenkins

Un job contient:

- la configuration du job
- l'historique des builds

Des exemples de jobs peuvent être:

- Validation de PR

Un job jenkins est configuré pour surveiller un server git (github, bitbucket). A chaque nouvelle PR ou changement dans une PR, Jenkins va déclencher un build. Si le build se passe correctement la PR est validée sinon Jenkins remonte une erreur. Cela permet d'avoir une validation automatique des PR créées par les développeurs.

- Déploiement continu

Un job est configuré pour scruter une branche particulière d'un répertoire git (`demo`, `prod` ou `master`). A chaque nouveau commit sur cette branche, un build est déclenché. Ce build va récupérér les sources du projet, les compiler et crééer un exécutable. Ensuite il suffit de déployer cet exécutable sur l'environnement souhaité. Cette étape de déploiement peut être également facilitée par des solutions de type PAAS.
