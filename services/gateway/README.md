# Gateway Service

### Build and Run Gateway Service

Make sure `docker containers`, `config-server` and `discovery` services are running.  

Build the `gateway` service. For that locate to `gateway` at terminal: `cd services/gateway`

```sh 
../../mvnw clean install
```

Run the service.

```sh 
../../mvnw spring-boot:run
```
