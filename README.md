# Prérequis

## Compte Google Cloud

Pour réaliser ce tutoriel, nous utiliserons Cloud Shell Editor, qui est un service de Google Cloud.
Si vous n'avez pas de compte Google Cloud, il sera nécessaire d'en créer un. Vous pouvez le faire sur la page suivante: [https://console.cloud.google.com/freetrial](https://console.cloud.google.com/freetrial)

> Lors de la création du compte, vous devrez fournir un numéro de carte de crédit,
> mais le tutoriel ne dépassera pas l'offre gratuite.
> Il est aussi possible d'utiliser son environnement de développement local.
> Toutefois, les énoncés et corrigés n'ont été testés que sur Cloud Shell Editor.
> Si vous décidez d'utiliser votre environnement local, il faudra adapter le contenu des exercices en autonomie.

## Cloud Shell Editor

Cloud Shell Editor est un VSCode accessible en ligne. IL va nous permettre d'éditer du code et d'éxécuter des commandes pour piloter DVC sans avoir besoin d'installer quoique ce soit depuis votre poste.

Pour l'ouvrir, il suffit de se rendre sur la page suivante: [https://ssh.cloud.google.com/cloudshell/editor](https://ssh.cloud.google.com/cloudshell/editor)

🚨 Il faudra aussi veiller à ce que le mode éphémère soit désactivé.
Lorsque le mode éphémère est activé, la mention "Éphémère" apparaît en haut à gauche de l'écran.

![screenshot_ephemeral](./docs/assets/00.ephemeral.png)

Toutes les commandes seront exécutées depuis le Cloud Shell Editor.

## Exécution du script d'initialisation

Dans un terminal, exécuter les commandes suivantes pour initialiser l'environnement de travail:

```bash
git clone https://github.com/bnau/dvc-project
cd dvc-project

export PATH=$PATH:~/.local/bin
pip install dvc dvc[gs] ipykernel matplotlib fastapi uvicorn python-multipart
```

> On peut accéder à un terminal en cliquant sur le bouton dédié en haut à droite de l'écran.
![screenshot_terminal](./docs/assets/00.terminal.png)

## Ouverture du projet dans Cloud Shell Editor

Il vous faudra enfin ouvrir le projet dans Cloud Shell Editor.
![screenshot_open](./docs/assets/00.open.png)

/!\ **Important** /!\

> Vous pouvez maintenant suivre le hands-on.
> Chaque étape commencera par des instructions pour initialiser son environnement de travail.
> Ces instructions sont facultatives (sauf pour la première partie).
> Elles permettent de reprendre à partir du corrigé de l'étape précédente,
> mais il est préférable de réaliser l'intégralité des exercices.

## Lançons nous dans l'aventure

Un collègue a écrit un petit script pour générer un réseau de neurones qui classifie des images de chats ou de chiens.
Vous venez d'intégrer son équipe et vous devez maintenant monter votre environnement de développement.

Votre collègue a mis à disposition un repository Git avec le code source du projet:

[https://github.com/bnau/dvc-project](https://github.com/bnau/dvc-project)

Comme tout réseau de neurones a besoin de données pour s'entraîner,
et qu'on ne va pas mettre de fichiers volumineux dans Git,
il a également fourni une url permettant de télécharger respectivement les données d'entraînement:

[https://storage.googleapis.com/dvc-input-dependencies-hands-on/data.zip](https://storage.googleapis.com/dvc-input-dependencies-hands-on/data.zip)

Le réseau de neurones n'a pas été entraîné from scratch.
Il ajoute quelques couches de neurones à un réseau pré-entraîné.
Votre collègue a également mis à disposition le modèle pré-entraîné:

[https://storage.googleapis.com/dvc-input-dependencies-hands-on/base.h5](https://storage.googleapis.com/dvc-input-dependencies-hands-on/base.h5)

> Pas besoin de cloner le repo Git ni de télécharger quoique ce soit en local.
> On va le faire dans l'étape suivante dans Cloud Shell Editor.

Ce hands-on aura pour but d'améliorer un projet existant pour lui permettre d'être pilotable via Git.

Les étapes sont les suivantes :

1. [Dans la peau d'un projet Grosses Données](docs/01.first_step.md)
2. [Surveillons les fichers](docs/02.track_files.md)
3. [Stoackage distant de l'état](docs/03.remote-storage.md)
4. [Séparons les fichers](docs/04.split-file.md)
5. [Tuyauterie](docs/05.pipeline.md)
6. [Récupérons des modifications distantes](docs/06.pull-experiment.md)
7. [Sauvegarde et partage de model](docs/07.model-registry.md)
