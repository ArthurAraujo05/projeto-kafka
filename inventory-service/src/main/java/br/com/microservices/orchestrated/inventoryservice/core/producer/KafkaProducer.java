package br.com.microservices.orchestrated.inventoryservice.core.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topic.orchestrator}")
    private String orchestratorTopic;

    public void sendEvent(String payload) {
        try {
            log.info("Sending event to Kafka topic {}: with data{}", orchestratorTopic, payload);
            kafkaTemplate.send(orchestratorTopic, payload).get();
            log.info("Event sent to Kafka topic {}: with data{}", orchestratorTopic, payload);
        }catch (Exception ex) {
            log.error("Error sending event to Kafka topic {}: with data{}", orchestratorTopic, payload, ex);
        }
    }

}
