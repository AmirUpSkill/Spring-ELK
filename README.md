
# ✨ Spring Boot ELK Monitoring Demo (elk project) ✨

Welcome to the `elk` project by Amir Abdallah! This project serves as a practical demonstration of integrating a **Spring Boot** application with the powerful **ELK Stack (Elasticsearch, Logstash, Kibana)** for robust, centralized logging and enhanced **observability**.

Whether you're learning about microservice monitoring, exploring the ELK stack, or looking for a template for your own Spring Boot logging setup, this project provides a clear, working example.

## 🎯 Project Goal

The primary goal is to showcase a complete, end-to-end flow for:

1.  **Generating** logs within a typical Spring Boot web application.
2.  **Shipping** these logs efficiently using Logback and the Logstash Logback Encoder.
3.  **Ingesting and Processing** logs using Logstash.
4.  **Storing and Indexing** logs in Elasticsearch.
5.  **Visualizing and Exploring** logs using Kibana.

This setup is fundamental for monitoring applications, troubleshooting issues quickly, and gaining insights into application behavior in real-time.

## 🚀 Key Features

*   **🐳 Dockerized ELK Stack:** Elasticsearch, Logstash, and Kibana (v7.17.3) configured and ready to run with a single `docker-compose up` command.
*   **☕ Spring Boot Application:** A simple RESTful application (running on port `8082`) built with Spring Boot 3.4.5 and Java 21.
*   **🪵 Logback Integration:** Configured via `logback-spring.xml` to send logs directly to Logstash.
*   **📄 JSON Log Formatting:** Uses `logstash-logback-encoder` to structure logs as JSON for easy parsing by Logstash and indexing in Elasticsearch.
*   **⚙️ Configurable Log Generation:** Includes REST endpoints (`/api/logs/info`, `/api/logs/warn`, `/api/logs/error`) to easily generate logs of different levels for testing.
*   **🔍 Centralized Log Viewing:** Demonstrates how to view and search application logs centrally in Kibana.
*   **🗓️ Daily Indexing:** Logstash configured to create daily indices in Elasticsearch (`spring-boot-logs-YYYY.MM.dd`) for better data management.

## 🏗️ Architecture Overview

The data flows through the system as follows:

1.  **Spring Boot App (Port 8082):** Generates log events.
    *   Uses **Logback** + **LogstashEncoder**.
    *   Sends JSON logs via **TCP** to `localhost:5044`.
2.  **Logstash (Docker Container):** Listens on port `5044`.
    *   Receives logs via **TCP Input** (decodes JSON).
    *   (Optional filtering stage - currently minimal).
    *   Sends logs via **HTTP** to `http://elasticsearch:9200` (**Output Plugin**).
    *   Also prints logs to console (**stdout Output** for debugging).
3.  **Elasticsearch (Docker Container):** Listens on port `9200`.
    *   Receives logs from Logstash.
    *   **Indexes** logs into daily `spring-boot-logs-*` indices.
    *   Provides search capabilities.
4.  **Kibana (Docker Container):** Listens on port `5601`.
    *   Connects to Elasticsearch (`http://elasticsearch:9200`).
    *   Provides a **Web UI** for users to explore, visualize, and create dashboards from the log data.


## 🛠️ Technology Stack

*   **Backend:** Java 21, Spring Boot 3.4.5, Spring Web
*   **Build:** Maven
*   **Logging:** SLF4J, Logback, Logstash Logback Encoder (7.0.1)
*   **Monitoring Stack (ELK):**
    *   Elasticsearch 7.17.3
    *   Logstash 7.17.3
    *   Kibana 7.17.3
*   **Containerization:** Docker, Docker Compose

## 📁 Project Structure

```
.
├── logstash/
│   └── logstash.conf       # Logstash pipeline configuration
├── src/
│   ├── main/
│   │   ├── java/com/amir/elk/
│   │   │   ├── ElkApplication.java # Spring Boot main class
│   │   │   └── controller/
│   │   │       └── LogGeneratorController.java # REST controller for log generation
│   │   └── resources/
│   │       ├── application.properties # Spring Boot app config (port, logging file)
│   │       └── logback-spring.xml     # Logback config for Logstash appender
│   └── test/                  # Unit/Integration tests (if any)
├── .gitignore
├── docker-compose.yml        # Defines the ELK stack services
├── pom.xml                   # Maven project configuration
├── README.md                 # This file!
└── mvnw*                     # Maven wrapper files
```

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

*   **Java:** Version 21 or compatible
*   **Maven:** For building and running the Spring Boot application
*   **Docker:** To run the ELK stack containers
*   **Docker Compose:** To easily manage the multi-container ELK setup
*   **Git:** To clone the repository
*   **curl** (or a similar tool like Postman/Insomnia): To easily hit the application endpoints.

## 🚀 Getting Started

Follow these steps to get the entire system running:

1.  **Clone the Repository:**
    ```bash
    git clone <your-repository-url> # Replace with your repo URL
    cd elk # Navigate into the project directory
    ```

2.  **Start the ELK Stack:**
    Open a terminal in the project root directory (where `docker-compose.yml` is located) and run:
    ```bash
    docker-compose up -d
    ```
    *   This command downloads the necessary Docker images (if not already present) and starts Elasticsearch, Logstash, and Kibana containers in detached mode.
    *   Wait a minute or two for the services to fully initialize. Elasticsearch and Kibana can take some time on the first run.
    *   You can check the status of the containers using `docker ps`. You should see `elasticsearch`, `logstash`, and `kibana` running.

3.  **Run the Spring Boot Application:**
    Open another terminal, also in the project root directory, and run the application using the Maven wrapper:
    ```bash
    ./mvnw spring-boot:run
    ```
    *   The application will start, and you should see logs indicating it's running on port `8082`. You might also see initial connection attempts or logs being sent to Logstash in the console output if Logback initializes early.

4.  **Verify Services:**
    *   Spring Boot App: Should be accessible (e.g., try `curl http://localhost:8082/api/logs/info`)
    *   Elasticsearch: Should respond at `http://localhost:9200`
    *   Kibana: Should load the UI at `http://localhost:5601`

## ▶️ Usage: Generating and Viewing Logs

Now that everything is running, let's generate some logs and view them in Kibana:

1.  **Generate Logs:**
    Use `curl` (or your browser/API tool) to hit the endpoints of the Spring Boot application:
    ```bash
    # Generate an INFO log
    curl http://localhost:8082/api/logs/info

    # Generate a WARN log
    curl http://localhost:8082/api/logs/warn

    # Generate an ERROR log (with a simulated stack trace)
    curl http://localhost:8082/api/logs/error
    ```
    Each request will trigger the corresponding `LOGGER.<level>(...)` call in `LogGeneratorController`, sending a log event to Logstash.

2.  **Access Kibana:**
    Open your web browser and navigate to `http://localhost:5601`.

3.  **Configure Kibana Index Pattern (One-time setup):**
    *   Kibana needs to know which Elasticsearch indices to search. You'll need to create an "index pattern".
    *   Click the menu icon (☰) -> **Stack Management** -> **Kibana** -> **Index Patterns**.
    *   Click **Create index pattern**.
    *   In the "Index pattern name" field, enter `spring-boot-logs-*`. Kibana should indicate that this pattern matches one or more indices.
    *   Click **Next step**.
    *   For the "Time field", select `@timestamp` from the dropdown list. This tells Kibana which field contains the event time.
    *   Click **Create index pattern**.

4.  **Explore Logs in Kibana:**
    *   Now, click the menu icon (☰) -> **Analytics** -> **Discover**.
    *   You should see the log events generated by the Spring Boot application!
    *   You can:
        *   See logs appear in near real-time as you hit the endpoints.
        *   Use the search bar to filter logs (e.g., search for `level:ERROR` or `message:"Generating WARN"`).
        *   Expand log entries to see the full JSON structure sent from the application.
        *   Adjust the time range selector in the top right.

## ⚙️ Configuration Highlights

*   **Spring Boot Port:** `server.port=8082` in `application.properties`.
*   **Logstash Destination:** `<destination>localhost:5044</destination>` in `logback-spring.xml`. This points to the port exposed by the Logstash Docker container on your host machine.
*   **Logstash Input:** `port => 5044` in `logstash/logstash.conf`.
*   **Elasticsearch Host (for Logstash):** `hosts => ["http://elasticsearch:9200"]` in `logstash/logstash.conf`. Note: `elasticsearch` is the service name defined in `docker-compose.yml`, reachable within the Docker network.
*   **Elasticsearch Index Pattern:** `index => "spring-boot-logs-%{+YYYY.MM.dd}"` in `logstash/logstash.conf`.
*   **Kibana Elasticsearch URL:** `ELASTICSEARCH_URL: http://elasticsearch:9200` in `docker-compose.yml`.

## 🛑 Stopping the System

1.  Stop the Spring Boot application (Ctrl+C in its terminal).
2.  Stop and remove the ELK stack containers:
    ```bash
    docker-compose down
    ```
    *   Using `docker-compose down` cleans up the containers and the network created by `docker-compose up`. Add `-v` if you also want to remove the volumes (Elasticsearch data will be lost).

## 📄 License

*(Assuming MIT License - Amir, please update if you choose a different one or add a LICENSE file)*

This project is licensed under the MIT License. See the LICENSE file for details.

## 🧑‍💻 Author

*   **Amir Abdallah**

---
