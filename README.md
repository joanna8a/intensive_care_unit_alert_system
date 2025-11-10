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
