# Flowchart Spring Boot Application
## Tech Stack
* Java: 17
* Spring Boot
* Spring Data JPA
* H2 Database
* Swagger: Springdoc OpenAPI for API documentation


### Structure
* Flowchart: A container for nodes and edges.
* Node: Represents individual entities within a flowchart.
* Edge: Represents connections between nodes.

### Database Tables:

* Flowchart: Stores flowchart details.
* Node: Stores node details (focused on name/value pairs).
* Edge: Stores edge details (focused on name/value pairs).

## Getting Started
#### Prerequisites
* Java 17 or higher installed on your machine.
#### Clone the Project
> git clone https://github.com/ROCK05/flowchart.git

* Navigate to the project directory:

> cd flowchart
* To run the project in your IDE (e.g., IntelliJ IDEA), simply open the project and run the FlowchartApplication class.
* The application should be running on port 8091 by default. You can access the API at:
http://localhost:8091
#### Swagger Documentation
* Once the application is running, you can access the Swagger UI documentation at:
http://localhost:8091/swagger-ui.html

#### H2 Database
* The project uses H2 Database for local development. You can access the H2 console at http://localhost:8091/h2-console.

* Credentials and URL used by the application:
````
JDBC URL: jdbc:h2:~/flowchart
Username: alpha
Password: alpha
````

### API Endpoints
1. Upsert Flowchart \
   Endpoint: PUT /upsert \
   Description: Creates or updates the complete flowchart.
2. Get Flowchart by ID \
   Endpoint: GET /flowchart/{id} \
   Description: Retrieves a flowchart by its ID.
3. Delete Flowchart (Soft Delete) \
   Endpoint: DELETE /flowchart/delete/{id} \
   Description: Soft deletes the flowchart by its ID.
4. Add Nodes \
   Endpoint: PUT /flowchart/{id}/nodes/add \
   Description: Adds nodes to a specific flowchart.
5. Delete Nodes and Connected Edges \
   Endpoint: DELETE /flowchart/{id}/nodes/delete \
   Description: Deletes nodes and all edges connected to them.
6. Add Edges \
   Endpoint: PUT /flowchart/{id}/edges/add \
   Description: Adds edges between nodes in the flowchart.
7. Delete Edges \
   Endpoint: DELETE /flowchart/{id}/edges/delete \
   Description: Deletes edges in the flowchart.
8. Get Target Nodes \
   Endpoint: PUT /flowchart/{id}/target-nodes \
   Description: Retrieves all outgoing nodes for a given node in the flowchart.
