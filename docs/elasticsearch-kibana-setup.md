## Elasticsearch and Kibana Initial Setup

There are couple of ways to get `Elasticsearch` and `Kibana` instances. This guide explains to download with version `8.17.4` and setup on `MacOS` system. Use other options if you prefer to.

#### Download and Run Elasticsearch, Kibana

- [Download Elasticsearch](https://www.elastic.co/downloads/elasticsearch)
- [Download Kibana](https://www.elastic.co/downloads/kibana)

#### Run Elasticsearch 

At terminal, locate `/path/to/your/elasticsearch-8.17.4` and run

```sh
bin/elasticsearch
```

- Elasticsearch automatically generates certificates and keys in the following directory: `config/certs` 

- And it updates `elasticsearch.yml` for `xpack.security.enabled: true` settings and configures security options.

- It also provides `enrollment token` to be used at kibana server.

#### Run Kibana 

At terminal, locate `/path/to/your/kibana-8.17.4` and run

```sh
bin/kibana
```

Kibana has not been configured yet. Go to [kibana server](http://localhost:5601) and paste `enrollment token` there. 

#### Reset Elasticsearch Password 

Use this command to reset your Elasticsearch password. 

```sh
bin/elasticsearch-reset-password -u elastic
```