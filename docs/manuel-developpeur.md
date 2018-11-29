# Manuel pour le développeur

## Cycle de développement

Lors du développement, les différentes étapes sont suivies:

- Assignation d'un ticket: le développeur récupère un ticket. Le ticket détaille les développements à effectuer, les resources à utiliser, les spécifications à suivre. Un ticket a un numéro qui nous permettra de suivre et de tagguer nos développements. Par exemple `APP-24`.
- Création d'une feature branch. Pour isoler les développements des différents développeurs sur un projet, les développeurs doivent créer des branches par ticket. Exemple:

```
# récupère les dernières modifications du projet
git checkout master && git pull
# Crée une nouvelle branche
git branch features/APP-24-add-user-login
# Se placer sur la nouvelle branche
git checkout features/APP-24-add-user-login
```

Le nommage de branche est important car il permet de se repérer dans les différents développements. Il peut contenir un préfix `features` ou `fix` pour qualifier la nature du travail. Il doit contenir le numéro du ticket lié (ici `APP-24`) et un court descriptif de la feature.

- Code. Ici le développeur crée la fonctionnalité. Ne pas oublier d'ajouter:
    - des tests unitaires pour les parties avec de la logique complexe et/ou difficile à tester à la main
    - des logs (en suivant les bonnes pratiques)
    - de la documentation si besoin (API, configuration...)
- Commiter les changements (un ou plusieurs commits)
- Tester l'application avant de créer une pull-request (tests manuels et/ou automatiques)
- Créer la Pull Request (attention à la branche de destination). Lors de la création, il est bon d'ajouter une description de la feature, les parties impactées, comment tester la fonctionnalité. Pour les parties front, une capture d'écran peut aussi être utile pour donner un aperçu rapide de ce qui a été fait.
- Correction des commentaires sur la Pull Request
- Merge de la Pull Request: une fois la PR validée par les membres de l'équipe, un membre de l'équipe (autre que l'auteur de la PR) va se charger de merger la PR. Le code ainsi développer va se retrouver sur la branche principale (`master`)

## Cycle du projet

Le projet est organisé en itérations successives qui ont soit une durée limitée (sprint), soit un scope de fonctionnalités limitées. Chaque projet a un rituel/une vie de projet différente. Ici on décrit grossièrement l'organisation d'un projet qui suit un mode agile.

- Pour chaque itération, définition du scope de fonctionnalités. Les tickets sont estimés et ajoutés en fonction de leur priorité au scope de l'itération.
- Pendant l'itération, chaque développeur répète le cycle de développement. "Il dépile des tickets"
- A la fin de l'itération, une release de l'application est effectuée.
- Cette release est déployée sur l'environnement de qualification/recette.
- Selon les retours, la release peut être corrigée grâce à des tickets de bug ajoutés à l'itération courante. Si la qualité est suffisante la release peut être déployée sur les environnement de pre-prod/production.
- Plannification d'une nouvelle itération.

## Bonnes pratiques du développeur

Pour garder un code clair, lisible et cohérent, des bonnes pratiques doivent être suivies par les développeurs. Ces pratiques sont détaillées dans plusieurs livres et articles notamment [Clean Code](https://www.amazon.com/Clean-Code-Handbook-Software-Craftsmanship/dp/0132350882)

- Single Responsability Principle: Chaque partie du code doit s'occuper d'une seule chose
- Duplication, Refactoring: Éviter la duplication du code, en profiter pour refactorer les parties communes
- Pure Functions: Organiser le code en utilisant des fonctions pures, sans effet de bord
- Éviter les commentaires dans le code
- Tester le code: utilisation de tests automatiques unitaires, d'intégration ...

La priorité du code doit rester la lisibilité et sa facilité de compréhension par les autres développeurs.
