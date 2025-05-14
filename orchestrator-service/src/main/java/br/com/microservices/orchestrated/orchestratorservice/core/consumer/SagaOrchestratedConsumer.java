package br.com.microservices.orchestrated.orchestratorservice.core.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import br.com.microservices.orchestrated.orchestratorservice.core.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class SagaOrchestratedConsumer {

    private final JsonUtil jsonUtil;

    @KafkaListener(
        groupId = "${spring.kafka.consumer.group-id}", 
        topics = "${spring.kafka.topic.start-saga}")

    public void consumeStartSagaEvent(String payloyd){
        log.info("Received event {}, from start-saga topic", payloyd);
        var event = jsonUtil.toEvent(payloyd);
        log.info(event.toString());
    }

    @KafkaListener(
        groupId = "${spring.kafka.consumer.group-id}", 
        topics = "${spring.kafka.topic.orchestrator}")

    public void consumeOrchestratorEvent(String payloyd){
        log.info("Received event {}, from orchestrator topic", payloyd);
        var event = jsonUtil.toEvent(payloyd);
        log.info(event.toString());
    }

    @KafkaListener(
        groupId = "${spring.kafka.consumer.group-id}", 
        topics = "${spring.kafka.topic.finish-success}")

    public void consumeFinishSuccessEvent(String payloyd){
        log.info("Received event {}, from finish-success topic", payloyd);
        var event = jsonUtil.toEvent(payloyd);
        log.info(event.toString());
    }

    @KafkaListener(
        groupId = "${spring.kafka.consumer.group-id}", 
        topics = "${spring.kafka.topic.finish-fail}")

    public void consumeFinishFailEvent(String payloyd){
        log.info("Received event {}, from finish-fail topic", payloyd);
        var event = jsonUtil.toEvent(payloyd);
        log.info(event.toString());
    }
}


