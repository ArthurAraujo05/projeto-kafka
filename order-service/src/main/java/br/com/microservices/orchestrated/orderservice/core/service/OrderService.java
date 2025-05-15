package br.com.microservices.orchestrated.orderservice.core.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.microservices.orchestrated.orderservice.core.document.Event;
import br.com.microservices.orchestrated.orderservice.core.document.Order;
import br.com.microservices.orchestrated.orderservice.core.dto.OrderRequest;
import br.com.microservices.orchestrated.orderservice.core.producer.SagaProducer;
import br.com.microservices.orchestrated.orderservice.core.repository.OrderRepository;
import br.com.microservices.orchestrated.orderservice.core.utils.JsonUtil;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderService {

    private static final String TRANSACTION_ID_PATTERN = "%s_%s";

    private final EventService eventService;
    private final JsonUtil jsonUtil;
    private final SagaProducer producer;
    private final OrderRepository repository;
    
    public Order createOrder(OrderRequest orderRequest) {
        var order = Order
                .builder()
                .products(orderRequest.getProducts())
                .createdAt(LocalDateTime.now())
                .trasactionId(
                    String.format(TRANSACTION_ID_PATTERN, Instant.now().toEpochMilli(), UUID.randomUUID())
                )
                .build();
            repository.save(order);
            producer.sendEvent(jsonUtil.toJson(createPayload(order)));
            return order;
    }

    private Event createPayload(Order order) {
        var event = Event
                .builder()
                .transactionId(order.getTrasactionId())
                .orderId(order.getId())
                .payloud(order)
                .createdAt(LocalDateTime.now())
                .build();
        eventService.save(event);
        return event;
        
    }

}
