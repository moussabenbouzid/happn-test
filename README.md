*****Application Poi-project*****

Présentation de l'application:
- Poi-project est une application permettant de calculer les points d'intérêt (POI) d'une zone et de trouver les
zones les plus denses sur un plan à deux dimensions, latitude et longitude.
- Le plan est une grille sur laquelle on se déplace par incréments de 0,5 en latitude et en longitude
- Un POI est caractérisé par une latitude et une longitude
- Plus une zone contient de POI et plus elle est dense


Pré-requis:
- Serveur Web
- Client Web Service

Installation:
- Récupérer le fichier poi-project-0.0.1-RELEASE.war et le déposer sur le serveur
- Démarrer le serveur

Exemple:
- Télécharger et installer le serveur web tomcat https://tomcat.apache.org/download-80.cgi
- Déposer le .war dans le dossier webapps sous le dossier d'installation du serveur tomcat
- Démarrer l'application Monitor Tomcat
- Démarrer le serveur tomcat
- Installer le client Postman https://www.getpostman.com/downloads/
- Démarrer l'application Postman

Services disponibles :
1) {endPoint}/poi-project-0.0.1-RELEASE/parse  -- (POST)
1) {endPoint}/poi-project-0.0.1-RELEASE/poisbyzone/{latMin}/{latMax} -- (GET)
3) {endPoint}/poi-project-0.0.1-RELEASE/mostfilledareas/{nbZones} -- (GET)


1er service :
- Parse un fichier d'entrée .TSV contenant des POI
(Exemple dans le fichier poi.tsv disponible dans le .war sous WEB-INF\classes\static)
- Retourne la liste de POI chargés en session

2ème service:
- Prend en entrée deux décimaux, représentant une latitude minimale et une longitude minimale.
- Retourne tous les POIs de la zone demandée

3ème service:
- Calcul des n zones les plus denses, avec n comme point d'entrée du service.

Raisonnement pour le calcul des zones les plus denses:
un POI est un point du plan, caractérisé par une latitude et une longitude.

Soit le POI de latitude -4,2 et de longitude 3,6.
Soit un deuxième POI de latitude -4,4 et de longitude 3,7

Ces deux points sont dans la zone {"minLat":-4,5, "maxLat":-4, "minLon":3,5, "maxLon":4}

Pour déterminer la zone dans laquelle ces POI se situent, on simplifie le problème en réévaluant leurs coordonnées au demi entier inférieur le plus proche.

Les deux POI sont donc réévalués aux positions suivantes: latitude de -4.5, et longitude 3,5

Pour connaitre le nombre de POI présents dans une zone, il suffit de compter le nombre de POI ayant les mêmes positions réévaluées.
Ces derniers seront dans la zone entre la position réévaluée et la position incrémentée de 0,5.

On obtient ensuite les N zones les plus denses en triant de façon descendante la liste des POI ayant les mêmes coordonnées réévaluées 

Cas des POI ayant leurs latitude et/ou longitude non réévaluées:

Cela signifie qu'ils appartiennent à deux zones en latitude et/ou deux zones en longitude.

Cas du POI de latitude -4,5 et de longitude 3,6

Latitude réévaluée : -4,5     Longitude réévaluée : 3.5

La latitude de ce POI n'a pas été réévaluée.

Cette particularité implique que ce POI appartient à deux zones :

{"minLat":-4,5, "maxLat":-4, "minLon":3,5, "maxLon":4}

{"minLat":-5, "maxLat":-4,5, "minLon":3,5, "maxLon":4}

Cas du POI de latitude -4,5 et de longitude 3,5

Latitude réévaluée : -4,5     Longitude réévaluée : 3.5

Ce POI appartient à quatre zones:

{"minLat":-4,5, "maxLat":-4, "minLon":3,5, "maxLon":4}

{"minLat":-5, "maxLat":-4,5, "minLon":3,5, "maxLon":4}

{"minLat":-4,5, "maxLat":-4, "minLon":3, "maxLon":3,5}

{"minLat":-5, "maxLat":-4,5, "minLon":3, "maxLon":3,5}

Cette particularité est prise en compte par l'application

GL & HF !
