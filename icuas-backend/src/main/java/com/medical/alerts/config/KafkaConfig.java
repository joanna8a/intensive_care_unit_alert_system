package com.medical.alerts.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    
    @Bean
    public NewTopic vitalSignsTopic() {
        return TopicBuilder.name("vital-signs-topic")
            .partitions(3)
            .replicas(1)
            .build();
    }
    
    @Bean
    public NewTopic medicalAlertsTopic() {
        return TopicBuilder.name("medical-alerts-topic")
            .partitions(3)
            .replicas(1)
            .build();
    }
    
    @Bean
    public NewTopic iotDeviceTopic() {
        return TopicBuilder.name("iot-device-topic")
            .partitions(3)
            .replicas(1)
            .build();
    }
}