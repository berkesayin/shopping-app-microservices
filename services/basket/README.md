# Basket Service

`Basket` service handles user shopping baskets and its operations. It uses `Redis` for `basket` data.

### Build and Run Basket Service

Make sure `docker containers`, `config-server` and `discovery` service are running.

Build the `basket` service. For that locate to `basket` at terminal: `cd services/basket`

```sh 
mvn clean install
```

Run the service.

```sh 
mvn spring-boot:run
```

### Basket Service Endpoints

`BASE_URL` = `/api/v1/baskets`

| Method Type | Endpoint URL | Authorization | Function Name |
| :--- | :--- | :--- | :--- |
| GET | `Base URL/me` | `hasRole('USER')` | `getBasket` |
| POST  | `Base URL/items` | `hasRole('USER')` | `addItemToBasket` |
| GET | `Base URL/total-price` | `hasRole('USER')` | `calculateTotalBasketPrice` |

