package br.com.microservices.orchestrated.inventoryservice.core.consumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import br.com.microservices.orchestrated.inventoryservice.core.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class InventoryConsumer {

    private final JsonUtil jsonUtil;

    @KafkaListener(
        groupId = "${spring.kafka.consumer.group-id}", 
        topics = "${spring.kafka.topic.inventory-success}")

    public void consumeSuccessEvent(String payloyd){
        log.info("Received success event {}, from inventory-success topic", payloyd);
        var event = jsonUtil.toEvent(payloyd);
        log.info(event.toString());
    }

    @KafkaListener(
        groupId = "${spring.kafka.consumer.group-id}", 
        topics = "${spring.kafka.topic.inventory-fail}")

    public void consumeFailEvent(String payloyd){
        log.info("Received rollback event {}, from inventory-fail topic", payloyd);
        var event = jsonUtil.toEvent(payloyd);
        log.info(event.toString());
    }
}