# Product Service

`Product` service handles products and categories operations. For `products` data:
- `Primary database`: `PostgreSQL`
- `Secondary data store for search`: `Elasticsearch`

### Build and Run Product Service

Make sure `docker containers`, `config-server` and `discovery` services are running.

Build the `product` service. For that locate to `product` at terminal: `cd services/product`

```sh 
../../mvnw clean install
```

Run the service.

```sh 
../../mvnw spring-boot:run
```

### Product Service Endpoints

`BASE_URL` = `/api/v1/products`

| Method Type | Endpoint URL | Authorization | Function Name |
| :--- | :--- | :--- | :--- |
| GET | `Base URL` | `Public Endpoint` | `getAllProducts` |
| GET  | `Base URL/{productId}` | `Public Endpoint` | `getProductById` |
| POST | `Base URL` | `hasRole('BACKOFFICE')` | `createProduct` |
| PATCH | `Base URL/{productId}/status` | `hasRole('BACKOFFICE')` | `setProductStatus` |
| GET | `Base URL/{productId}/category-id` | `hasRole('BACKOFFICE')` | `getCategoryIdOfProduct` |


