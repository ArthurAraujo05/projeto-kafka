package br.com.microservices.orchestrated.productvalidationservice.core.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import br.com.microservices.orchestrated.productvalidationservice.core.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class ProductValidationConsumer {

    private final JsonUtil jsonUtil;

    @KafkaListener(
        groupId = "${spring.kafka.consumer.group-id}", 
        topics = "${spring.kafka.topic.product-validation-success}")

    public void consumeSuccessEvent(String payloyd){
        log.info("Received success event {}, from product-validation-success topic", payloyd);
        var event = jsonUtil.toEvent(payloyd);
        log.info(event.toString());
    }

    @KafkaListener(
        groupId = "${spring.kafka.consumer.group-id}", 
        topics = "${spring.kafka.topic.product-validation-fail}")

    public void consumeFailEvent(String payloyd){
        log.info("Received rollback event {}, from product-validation-fail topic", payloyd);
        var event = jsonUtil.toEvent(payloyd);
        log.info(event.toString());
    }
}
