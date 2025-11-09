package com.medical.alerts.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name:Medical Alert System}")
    private String applicationName;

    @Bean
    public OpenAPI medicalAlertOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Medical Alert System API")
                        .description("""
                            ## Real-time Patient Monitoring and Alert System
                            
                            This API provides endpoints for:
                            - Patient management and monitoring
                            - Real-time vital signs processing
                            - Medical alert generation and management
                            - IoT device data integration
                            
                            ### Key Features:
                            - **Real-time Monitoring**: Continuous patient vital signs tracking
                            - **Smart Alerts**: Automated medical rule evaluation
                            - **Multi-level Severity**: CRITICAL, WARNING, INFO alerts
                            - **Kafka Integration**: IoT data stream processing
                            - **WebSocket Support**: Real-time dashboard updates
                            
                            ### Medical Rules:
                            - Oxygen Saturation: Critical < 92%, Warning < 94%
                            - Heart Rate: Critical < 50 or > 130 bpm
                            - Blood Pressure: Systolic Critical < 90 or > 180 mmHg
                            - Temperature: Critical < 35°C or > 39.5°C
                            """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Medical Alert Team")
                                .email("support@medicalalerts.com")
                                .url("https://medicalalerts.com"))
                        .license(new License()
                                .name("Medical Use License")
                                .url("https://medicalalerts.com/license")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server"),
                        new Server()
                                .url("http://localhost:8080/api")
                                .description("Docker Local Server"),
                        new Server()
                                .url("https://api.medicalalerts.com")
                                .description("Production Server")
                ))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}