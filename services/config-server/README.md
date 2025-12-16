# Config Server

`config-server` provides centralized management for the configuration of all services. 

Configurations for `config-server` itself:
- `/config-server/src/main/resources/application.yml`
- `/config-server/src/main/resources/configurations/application.yml`

Configurations for all services:
- `/config-server/src/main/resources/configurations/`
  - `auth-service.yml`
  - `basket-service.yml`
  - `customer-service.yml`
  - `discovery-service.yml`
  - `gateway-service.yml`
  - `notification-service.yml`
  - `order-service.yml`
  - `payment-service.yml`
  - `product-service.yml`
  - `search-service.yml`

### Build and Run Config Server

Make sure `docker containers` are running. `config-server` is the first service that is supposed to be started. 

Build the `config-server`. For that locate to `config-server` at terminal: `cd services/config-server`

```sh 
./mvnw clean install
```

Run the service.

```sh 
./mvnw spring-boot:run
```
