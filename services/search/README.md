# Search Service

`Search` service handles `search` functionalities. And it is used for `products` and `order` data. It is connected to `Elasticsearch` and it can be viewed at `Kibana` server. 

### Build and Run Search Service

Make sure `docker containers`, `config-server` and `discovery` services are running.

Build the `search` service. For that locate to `search` at terminal: `cd services/search`

```sh 
../../mvnw clean install
```

Run the service.

```sh 
../../mvnw spring-boot:run
```

### Search Service Endpoints

`BASE_URL` for `ProductSearchController` = `/api/v1/search/products`

| Method Type | Endpoint URL | Authorization | Function Name |
| :--- | :--- | :--- | :--- |
| POST | `Base URL` | `Public Endpoint` | `searchProducts` |
| GET  | `Base URL/autocomplete` | `Public Endpoint` | `autocomplete` |

### Search Examples

Check docs named `Search Examples` for use cases.

- [Search Functions](https://github.com/berkesayin/shopping-app-microservices/blob/master/docs/5.SEARCH_FUNCTIONS.md)