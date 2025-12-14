# eCommerce Application With Elastic Data And Iyzico Payment

The `shopping app` is designed using `Microservices` architecture and `API Gateway` pattern where each service is responsible for a specific business function. 

`iyipay-java` API client developed by `iyzico` is integrated and used for the project at the `payment` service.

`Reference`: https://github.com/iyzico/iyzipay-java

![img](./docs/img/39.iyzico-sandbox.png)

`Architectural Diagram`

![img](./docs/img/40.architecture.png)

`Elasticsearch` &rarr; `product` index as example

![img](./docs/img/41.kibana-product-index.png)

### Technologies  

Software architectures and principles:
- `Microservices`
- `API gateway`
- `Distributed systems`
- `Domain driven design`

Technologies:
- `Java 21`
- `Spring Boot v.3.3.1`
- `Spring Security`
- `Maven`
- `GitHub Actions`
- `Elasticsearch`
- `Kibana`
- `Elastic Agent` and `Fleet Server`
- `Elastic APM` and `OpenTelemetry`
- `Docker Compose`
- `PostgreSQL`
- `MongoDB`
- `Redis`
- `Kafka`

### Documentation

Refer to the `docs` section for `setup` and `development` of the project. It includes these parts:

- [Backend Services]()
  - [auth](https://github.com/berkesayin/shopping-app-microservices/tree/master/services/auth)
  - [basket](https://github.com/berkesayin/shopping-app-microservices/tree/master/services/basket)
  - [config-server](https://github.com/berkesayin/shopping-app-microservices/tree/master/services/config-server)
  - [customer](https://github.com/berkesayin/shopping-app-microservices/tree/master/services/customer)
  - [discovery](https://github.com/berkesayin/shopping-app-microservices/tree/master/services/discovery)
  - [gateway](https://github.com/berkesayin/shopping-app-microservices/tree/master/services/gateway)
  - [notification](https://github.com/berkesayin/shopping-app-microservices/tree/master/services/notification)
  - [order](https://github.com/berkesayin/shopping-app-microservices/tree/master/services/order)
  - [payment](https://github.com/berkesayin/shopping-app-microservices/tree/master/services/payment)
  - [product](https://github.com/berkesayin/shopping-app-microservices/tree/master/services/product)
  - [search](https://github.com/berkesayin/shopping-app-microservices/tree/master/services/search)

[Getting API Key for Iyzipay Java Client]()

