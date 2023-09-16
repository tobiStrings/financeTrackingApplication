# Finance Tracking Application

## Overview

The Finance Tracking Application is a web-based tool that allows users to manage their finances, track income and expenses, set budgets, and gain insights into their financial activities. It is built using Java Spring Boot for the backend, PostgreSQL as the database, and Gradle for dependency management.

## Table of Contents

1. [Features](#features)
2. [Prerequisites](#prerequisites)
3. [Getting Started](#getting-started)
4. [Configuration](#configuration)
5. [Database Setup](#database-setup)
6. [Build and Run](#build-and-run)
7. [API Documentation](#api-documentation)
8. [Contributing](#contributing)
9. [License](#license)

## Features

- User registration and authentication.
- Transaction management with categories and tags.
- Budget creation and tracking.
- Income and expense tracking.
- Net worth calculation.
- Financial reporting and visualization.
- Goal setting and tracking.
- Account management with bank account integration.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java Development Kit (JDK) 11 or higher installed.
- Gradle build tool installed.
- PostgreSQL database server set up.
- IDE (e.g., IntelliJ IDEA, Eclipse) for development.
- Git for version control (optional but recommended).

## Getting Started

To get the Finance Tracking Application up and running on your local machine, follow these steps:

### Configuration

1. Clone the repository:

   ```shell
    git clone https://github.com/tobiStrings/financeTrackingApplication.git
    
2. Navigate into the project directory
   ```shell
   cd finance-tracking-app

3. Configure your database connection settings in
   ```shell
   src/main/resources/application.properties


### Database Setup

1. Create a PostgreSQL database for the application.
2. Update the database connection properties in 'application.properties'.
   
### Build and Run
1. Build the application
   ```shell
   ./gradlew build

2. Run the Application 
   ```shell
   ./gradlew bootRun
The finance application should be running on http://localhost:8080

### API Documentation
1. The API documentation for the Finance Tracking Application is available at [API Documentation]()

### Contributing
