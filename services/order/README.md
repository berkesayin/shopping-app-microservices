# Order Service

`Order` service handles customers operations. For `orders` data:
- `Primary database`: `PostgreSQL`
- `Secondary data store for search and analysis`: `Elasticsearch`

### Build and Run Order Service

Make sure `docker containers`, `config-server` and `discovery` service are running.

Build the `order` service. For that locate to `order` at terminal: `cd services/order`

```sh 
mvn clean install
```

Run the service.

```sh 
mvn spring-boot:run
```

### Order Service Endpoints

`BASE_URL` = `/api/v1/orders`

| Method Type | Endpoint URL | Authorization | Function Name |
| :--- | :--- | :--- | :--- |
| POST | `Base URL` | `hasRole('USER')` | `createOrder` |
| GET  | `Base URL` | `hasRole('BACKOFFICE')` | `getAllOrders` |
| GET | `Base URL/{orderId}` | `hasRole('BACKOFFICE')` | `getOrderById` |
