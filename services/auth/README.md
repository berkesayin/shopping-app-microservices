# Auth Service

`Auth` service handles `authantication` functionalities. 

### Build and Run Auth Service

Make sure `docker containers`, `config-server`, `discovery` and `customer` services are running.

Build the `auth` service. For that locate to `auth` at terminal: `cd services/auth`

```sh 
../../mvnw clean install
```

Run the service.

```sh 
../../mvnw spring-boot:run
```

### Auth Service Endpoints

| Method Type | Endpoint URL | Authorization | Function Name |
| :--- | :--- | :--- | :--- |
| POST | `Base URL/login` | `Public Endpoint` | `getToken` |
| POST  | `Base URL/register` | `Public Endpoint` | `createUser` |
| POST  | `Base URL/logout` | `hasRole(USER)`, `hasRole(BACKOFFICE)` | `invalidateToken` |

