# Git

Git est un outil pour le versionnement du code

## Tutorial

[https://try.github.io]

## Commandes de base

- `git status`: état du répertoire actuel
- `git checkout <ref>`: se placer sur la `<ref>`
- `git checkout -b <branchname>`: crée la branche `<branchname>` et se déplace dessus
- `git pull`: mettre à jour sa version locale du répertoire
    - `git pull --rebase`: mise à jour sans commit de merge en cas de différence
- `git add <filename>`: ajoute un ou des fichiers à l'index local
- `git commit -m "Commit message"`: crée un commit avec les fichiers indexés
- `git push`: envoie les changements locaux vers le serveur distant
- `git log`: montre l'historique
