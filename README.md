
# Courier Tracking System

  

A Spring Boot application for tracking courier geolocation events and applying geospatial business rules.

[Web version](https://mirgos.onrender.com/swagger-ui/index.html) (Might take a few minutes to deploy)
[Github Repo](https://github.com/ozanokur/mirgos)



## Running the Application
    
### Build and Run
RUN.bat file can be used to build and deploy locally. It will compile the project and deploy it with the settings given in the .env file.
The application will start on  `http://localhost:8080`

Additionally docker can be used to deploy locally.
```bash
docker build -t courier-tracking . 
docker run -p 8080:8080 --env-file .env courier-tracking
```

### API Endpoints
API documentation can be found on swagger. ([web endpoint](https://mirgos.onrender.com/swagger-ui/index.html), [local endpoint](http://localhost:8080/swagger-ui/index.html))

## Features

### Location Ingestion

- REST endpoint: `POST /api/courier-locations`

- Accepts `CourierLocation` stream over API. 


### Store Proximity Detection

- Store locations are entered through API and stored in PostgreSQL with PostGIS geographies to allow geospatial indexing.

- Detects when a courier enters within a configurable distance (default: 100m) of a store

- Cooldown period: 1 minute (configurable)

- Logs and stores entrance events via Observer pattern

- Entrance event query endpoint: 'GET /api/couriers/{id}/entrances'

-  **Assumption**: In a real project this ingestion would be event based, but here API calls are used for simplicity.

  

### Distance Tracking

- Incrementally tracks total distance traveled per courier

- Uses Haversine formula for accurate geospatial distance calculation. Can be switched to PostGIS distance strategy through DB calls.

- Distance calculation is done after the courier location ingestion, not at the time of the API call to increase performance.

- REST endpoint: `GET /api/couriers/{id}/total-distance`

- Returns total distance in meters

- Distance tracking is asynchronously processed through an event in order to increase performance of location ingestions.

-  **Assumption**: Events arrive in order per courier. In a production environment this may not be guaranteed, so additional steps might be needed to ensure consistency.

-  **Assumption**: Events arrive with valid couriers. In a production environment this may not be guaranteed, nonexistent couriers may be selected. Severals options can be considered here based on performance issues and the nature of the data, such as validations, foreign keys, and eventual consistency decisions.

  

## Architecture
  
A simple workflow graph for the required logic can be found on `workflow.png`

### Architecture Layers

  

-  **Controller** (`controller`): REST endpoints for location ingestion and distance queries

-  **Service** (`service`): Business logic for tracking, proximity detection, and distance calculation

-  **Domain** (`domain`): Core domain models 

-  **Event** (`event`): Event based processes (distance calculation, entrance detection) 

-  **Repository** (`repository`): External PostgreSQL database with PostGIS integration for geolocational indexing.

-  **Util** (`util`): Utility classes including distance calculation strategies

-  **Config** (`config`): Spring configuration for store loading and bean setup

  

### Design Patterns

  

#### 1. Strategy Pattern - Distance Calculation

-  **Interface**: `DistanceCalculationStrategy`

-  **Implementations**: `HaversineDistanceStrategy`, `PostgisDistanceStrategy` 

-  **Purpose**: Allows swapping distance calculation algorithms without changing business logic. Decided through `distance_strategy` parameter, requires cache refresh.

  

#### 2. Observer Pattern - Store Entrance Events

-  **Observers**: `DistanceCalculationEventListener`, `EntranceCalculationEventListener`

-  **Purpose**: Decouples distance calculation and entrance detection from location ingestion logic.

-  **Assumption**: In a real project these events would use messaging brokers such as Kafka or RabbitMQ to reduce the load on the server and increase scalability, but here Spring Boot events are used for simplicity.

#### 3. Built-in Patterns

-  Several built-in design patterns are used in the project such as Singletons and Factories.




## Testing

  
## Testing Strategy

Basic unit tests exist for entrance, distance calculation and location ingestion because these are some of the most important core aspects of the project.

 In a real production environment, this project would include:
- Extensive unit tests with conditional and edge-case coverage
- Integration tests backed by a real PostgreSQL + PostGIS instance since a large chunk of the processes rely on external integrations

## Running Tests
Tests can be run with the following command:
```bash
mvnw test
```
  

## Assumptions

 
1.  **Event Ordering**: Events arrive in chronological order per courier. This is documented in the codebase and critical for accurate distance tracking.

2.  **In-Memory Events**: Events are maintained in-memory. Application restart loses all events. Too many events may cause memory issues. Suitable for demonstration/prototype.

3.  **Single Instance**: Designed for single-instance deployment. No distributed state synchronization.

4.  **No Security Measures**: API calls are not secured. A gateway application is assumed to exist.


## Future Enhancements

1. Redis instead of in-memory caching

2. Kafka integration for high-throughput ingestion

3. Metrics and monitoring (Prometheus, Grafana)

4. Rate limiting and validation

5. Proper Testing