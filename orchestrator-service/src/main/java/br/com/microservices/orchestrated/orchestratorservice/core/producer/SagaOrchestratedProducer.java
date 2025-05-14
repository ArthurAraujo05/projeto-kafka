package br.com.microservices.orchestrated.orchestratorservice.core.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Component
public class SagaOrchestratedProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendEvent(String payload, String topic) {
        try {
            log.info("Sending event to Kafka topic {}: with data{}", topic, payload);
            kafkaTemplate.send(topic, payload).get();
            log.info("Event sent to Kafka topic {}: with data{}", topic, payload);
        }catch (Exception ex) {
            log.error("Error sending event to Kafka topic {}: with data{}", topic, payload, ex);
        }
    }

}
