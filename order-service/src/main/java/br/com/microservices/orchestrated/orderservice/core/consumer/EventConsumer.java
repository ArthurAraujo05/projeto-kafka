package br.com.microservices.orchestrated.orderservice.core.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import br.com.microservices.orchestrated.orderservice.core.service.EventService;
import br.com.microservices.orchestrated.orderservice.core.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class EventConsumer {

    private final EventService service;


    private final JsonUtil jsonUtil;

    @KafkaListener(
        groupId = "${spring.kafka.consumer.group-id}", 
        topics = "${spring.kafka.topic.notify-ending}")

    public void consumeNotifyEndingEvent(String payloyd){
        log.info("Received ending notification event {}, from notify-ending topic", payloyd);
        var event = jsonUtil.toEvent(payloyd);
        service.notifyEnding(event);
    }
}
