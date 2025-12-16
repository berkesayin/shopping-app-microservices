# Discovery Service

`Discovery` service enables service discovery for other services. 

### Build and Run Discovery Service

Make sure `docker containers` and `config-server` are running. `Discovery` service is the service that is supposed to be run firstly after `config-server`.

Build the `discovery` service. For that locate to `discovery` at terminal: `cd services/discovery`

```sh 
./mvnw clean install
```

Run the service.

```sh 
./mvnw spring-boot:run
```
