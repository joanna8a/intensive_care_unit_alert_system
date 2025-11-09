# intensive_care_unit_alert_system
Intensive Care Unit Alert System

Medical Alert System - Real-Time Patient Monitoring
📋 Table of Contents
Overview

Features

Architecture

Tech Stack

Quick Start

API Documentation

Deployment

Contributing

🌟 Overview
A real-time medical alert system built with Spring Boot and React that monitors patient vital signs, detects critical conditions, and provides instant multi-language alerts to medical staff. The system demonstrates senior-level software engineering principles including SOLID, design patterns, and clean architecture.

✨ Features
🚨 Real-Time Monitoring
Continuous Vital Signs Tracking: Heart rate, oxygen saturation, blood pressure, temperature

Intelligent Alert System: Multi-level severity (Critical, Warning, Info)

Predictive Analytics: Early detection of deteriorating conditions

Multi-language Support: English and Spanish interfaces

🏗️ Architecture Excellence
SOLID Principles: Clean separation of concerns

Design Patterns: Strategy, Observer, Builder, Visitor, Factory

Microservices Ready: Modular and scalable design

Real-time Communication: WebSocket/RSocket integration

🎯 Medical Specific
Configurable Rules: Medical rule engine with priority-based evaluation

Smart Recommendations: Context-aware medical actions

Multi-channel Notifications: Dashboard, mobile, nurse call systems

Audit Trail: Complete alert history and acknowledgment tracking

🏗️ Architecture
System Architecture
text
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   React Frontend│◄──►│  Spring Boot API │◄──►│    MySQL DB     │
│                 │    │                  │    │                 │
│ • Real-time UI  │    │ • Alert Engine   │    │ • Patient Data  │
│ • Multi-language│    │ • Rule Engine    │    │ • Vital Signs   │
│ • WebSocket     │    │ • Notifications  │    │ • Alert History │
└─────────────────┘    └──────────────────┘    └─────────────────┘
         │                         │                      │
         │                         │                      │
         ▼                         ▼                      ▼
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│ Medical Devices │    │ External Systems │    │ Audit & Logging │
│ • IoT Sensors   │    │ • EHR Integration│    │ • Compliance    │
│ • Monitors      │    │ • Lab Systems    │    │ • Reporting     │
└─────────────────┘    └──────────────────┘    └─────────────────┘
🛠 Tech Stack
Backend
Java 17 - Core programming language

Spring Boot 3.x - Application framework

Spring Data JPA - Database access

MySQL 8.0 - Production database

Spring WebSocket - Real-time communication

Maven - Dependency management

JUnit 5 - Testing framework

Frontend
React 18 - UI framework

TypeScript - Type safety

WebSocket Client - Real-time updates

i18next - Internationalization

Chart.js - Data visualization

Material-UI - Component library

DevOps
Docker - Containerization

Docker Compose - Multi-container setup

MySQL - Database server

Prometheus - Monitoring

Grafana - Dashboards

🚀 Quick Start
Prerequisites
Java 17 or higher

Node.js 18 or higher

Docker and Docker Compose

SOLID Principles Analysis
✅ S - Single Responsibility Principle (SRP)
Well Applied:

Controllers: Handle HTTP requests only (AlertController, PatientController, etc.)

Services: Contain business logic (MedicalAlertService, PatientService, etc.)

Repositories: Handle data access only

Strategies: Each rule handles one specific medical condition

Example of Good SRP:

java
// Each service has one responsibility
MedicalAlertService - Manages alert logic
PatientService - Manages patient data
VitalSignsService - Handles vital signs processing
✅ O - Open/Closed Principle (OCP)
Excellent Implementation!

Strategy Pattern: New medical rules can be added without modifying existing code

Interface-based: MedicalRuleStrategy allows extension

java
// New rules can be added without changing MedicalAlertService
@Component
public class BloodPressureRule implements MedicalRuleStrategy {
    // Implements interface without modifying existing code
}
✅ L - Liskov Substitution Principle (LSP)
Well Applied:

All strategy implementations (HeartRateRule, OxygenSaturationRule) can substitute MedicalRuleStrategy

Repository interfaces follow JPA contract

Consistent method signatures across implementations

✅ I - Interface Segregation Principle (ISP)
Good Implementation:

MedicalRuleStrategy has focused, cohesive methods

Repository interfaces are specific to their entities

No "fat" interfaces forcing unnecessary implementations

✅ D - Dependency Inversion Principle (DIP)
Excellent Implementation!

Constructor injection with @RequiredArgsConstructor

Depend on abstractions: Services depend on interfaces, not concretions

Spring's DI container manages dependencies

java
// DIP well implemented
@Service
@RequiredArgsConstructor
public class MedicalAlertService {
    private final MedicalAlertRepository alertRepository; // Abstraction
    private final List<MedicalRuleStrategy> ruleStrategies; // Abstraction
}
Architecture Strengths
1. Clean Layered Architecture
text
Controllers → Services → Repositories → Database
2. Event-Driven Design
Kafka integration for real-time processing

Separation of concerns between data ingestion and alert processing

Scalable message-driven architecture

3. Strategy Pattern for Medical Rules
java
// Excellent use of strategy pattern
private final List<MedicalRuleStrategy> ruleStrategies;

public List<MedicalAlert> evaluateVitalSigns(...) {
    return ruleStrategies.stream()
        .map(strategy -> strategy.evaluate(...))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
}
4. Comprehensive Error Handling
Input validation in services

Kafka error handling with acknowledgments

Proper exception propagation
