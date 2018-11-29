# Metrics

L'applications utilise [Kamon](http://kamon.io) pour collecter et reporter les métriques. L'application est configurée pour envoyer les métriques à un serveur [InfluxDB](https://docs.influxdata.com/influxdb) server.

### Setup

Installer une instance de InfluxDB sur votre machine, soit manuellement, avec `brew` ou avec `docker`.


Ensuite, il faut créer une database sur InfluxDB pour recevoir nos métriques:

```sh
curl -i -XPOST http://localhost:8086/query --data-urlencode "q=CREATE DATABASE saretec"
```

(Remplacer localhost et le port les informations de votre instance d'InfluxDB)

### Visualisation

Vous pouvez visualiser les métriques en utilisant [Grafana](http://grafana.org) ou [Cronograf](https://docs.influxdata.com/chronograf). Ici nous allons utiliser Grafana. Des dashboards pré-configurés sont disponibles dans le dossier `grafana/`.

Dans Grafana, vous aurez tout d'abord besoin de configurer InfluxDB en tant que datasource. Cliquer sur le bouton "Data Sources", "Add data source". Selectionner `Type = "InfluxDB"`, configurer les options http et sélectionner la base de données que vous venez de créer, `saretec` ici.

Pour importer un dashboard, sélectionner le bouton "Import", "Upload .json File". Ensuite entrez un nom pour votre dashboard, sélectionnez la datasource InfluxDB que vous venez de configurer. Pour le champ `application name`, entrer la valeur qui est présente dans `conf/application.conf`, pour la clé `kamon.influxdb.application-name`.
