# intensive_care_unit_alert_system

# ICU Alert System - Complete Medical Monitoring Platform

A comprehensive real-time patient monitoring and medical alert system with full-stack Docker deployment. This system continuously evaluates patient vital signs and generates critical alerts for healthcare professionals with a modern web interface.

## 🏥 System Overview

The ICU Alert System is a complete medical monitoring platform designed to monitor patient vital signs in real-time, evaluate medical conditions using configurable rules, and generate immediate alerts for critical situations. The system features a Spring Boot backend with React frontend and full Docker containerization.

## 🚀 Features

### Backend Services
- **Real-time Vital Signs Monitoring**: Continuous monitoring of heart rate, oxygen saturation, blood pressure, temperature, and respiratory rate
- **Intelligent Alert System**: Configurable medical rules for detecting critical conditions
- **Multi-source Data Integration**: Support for manual entry, medical monitors, and IoT devices
- **Event-Driven Architecture**: Kafka integration for scalable real-time processing
- **RESTful APIs**: Comprehensive API documentation with Swagger/OpenAPI
- **Patient Management**: Complete patient record management system
- **IoT Data Simulation**: Built-in simulator for testing and development

### Frontend Interface
- **Real-time Dashboard**: Live monitoring of patient vital signs and alerts
- **Multi-language Support**: Internationalization with locale files
- **Responsive Design**: Works on desktop and mobile devices
- **Alert Management**: Acknowledge and manage medical alerts
- **Patient Overview**: Comprehensive patient information display

### DevOps & Infrastructure
- **Docker Containerization**: Complete containerized deployment
- **Multi-environment Support**: Development, production, and override configurations
- **Database Management**: MySQL with sample data and stored procedures
- **Automated Scripts**: Start, stop, and backup operations

## 🛠 Tech Stack

### Backend
- **Java 17** with **Spring Boot 3.x**
- **Spring Data JPA** with **Hibernate**
- **MySQL Database**
- **Apache Kafka** for event streaming
- **Lombok** for boilerplate reduction
- **Swagger/OpenAPI 3** for API documentation
- **Maven** for dependency management

### Frontend
- **React** with modern hooks
- **TypeScript** for type safety
- **Nginx** for serving static files
- **Internationalization** with locale support

### Infrastructure
- **Docker & Docker Compose**
- **MySQL 8.0**
- **Apache Kafka**
- **Custom scripts** for deployment and maintenance

## 📋 Quick Start

### Prerequisites
- Docker and Docker Compose
- Git

### 1. Clone and Setup
```bash
git clone https://github.com/joanna8a/intensive_care_unit_alert_system.git
cd medical-alert-system
cp .env.example .env
# Edit .env with your configuration
```
### 2. Start Development Environment
```bash
./scripts/start.sh
```
Or manually:
```bash
docker-compose up -d
```
### 3. Access the Application
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080
- **API Documentation**: http://localhost:8080/swagger-ui.html
- **MySQL Database**: localhost:3306

### 4. Stop the Application
```bash
./scripts/stop.sh
```
## 🏗 Project Structure

```
medical-alert-system/
├── docker-compose.yml              # Main Docker composition
├── docker-compose.prod.yml         # Production overrides
├── docker-compose.override.yml     # Development overrides
├── .env.example                    # Environment template
├── README.md                       # This file
├── backend/                        # Spring Boot application
│   ├── Dockerfile                  # Backend container definition
│   ├── pom.xml                     # Maven dependencies
│   ├── src/main/java/com/medical/alerts/
│   │   ├── MedicalAlertApplication.java  # Main application class
│   │   ├── config/                 # Spring configuration
│   │   ├── controller/             # REST API controllers
│   │   ├── model/                  # JPA entities and DTOs
│   │   ├── repository/             # Spring Data repositories
│   │   ├── service/                # Business logic services
│   │   └── strategy/               # Medical rule strategies
│   ├── src/main/resources/
│   │   ├── application.yml         # Main configuration
│   │   ├── application-docker.yml  # Docker-specific config
│   │   └── application-prod.yml    # Production configuration
│   └── mvnw                        # Maven wrapper
├── frontend/                       # React application
│   ├── Dockerfile                  # Frontend container definition
│   ├── nginx.conf                  # Nginx configuration
│   ├── package.json                # Node.js dependencies
│   ├── public/index.html           # HTML template
│   └── src/
│       ├── components/             # React components
│       ├── hooks/                  # Custom React hooks
│       ├── services/               # API service layer
│       ├── locales/                # Internationalization files
│       └── types/                  # TypeScript type definitions
├── docker/                         # Database configuration
│   └── mysql/
│       ├── init.sql                # Database schema initialization
│       ├── sample-data.sql         # Sample patient and medical data
│       └── stored-procedures.sql   # Database stored procedures
└── scripts/                        # Deployment scripts
    ├── start.sh                    # Start all services
    ├── stop.sh                     # Stop all services
    └── backup.sh                   # Database backup script
```
