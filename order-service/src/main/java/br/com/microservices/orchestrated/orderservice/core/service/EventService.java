package br.com.microservices.orchestrated.orderservice.core.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import br.com.microservices.orchestrated.orderservice.config.exception.ValidateException;
import br.com.microservices.orchestrated.orderservice.core.document.Event;
import br.com.microservices.orchestrated.orderservice.core.dto.EventFilters;
import br.com.microservices.orchestrated.orderservice.core.repository.EventRepository;
import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
@AllArgsConstructor
public class EventService {
    
    private static final Logger log = LoggerFactory.getLogger(EventService.class);
    private final EventRepository repository;

    public void notifyEnding(Event event) {
        event.setOrderId(event.getOrderId());
        event.setCreatedAt(LocalDateTime.now());
        save(event);
        log.info("Order {} with saga notified! TransactionId: {}", event.getTransactionId(), event.getOrderId());
    }   

    public List<Event> findAll() {
        return repository.findAllByOrderByCreatedAtDesc();

    }

    public Event findByFilters(EventFilters filters) {
        validateEmptyFilters(filters);
        if (!isEmpty(filters.getOrderId())) {
            return findByOrderId(filters.getOrderId());
        } else {
            return findByTrasactionId(filters.getTransactionId());
        }
            
    }

    private Event findByOrderId(String orderId) {
        return repository
            .findTop1ByOrderIdOrderByCreatedAtDesc(orderId)
            .orElseThrow(() -> new ValidateException("Event not found by OrderId"));    
    }

    private Event findByTrasactionId(String transactionId) {
        return repository
            .findTop1ByTransactionIdOrderByCreatedAtDesc(transactionId)
            .orElseThrow(() -> new ValidateException("Event not found by transactionId"));    
    }

    private void validateEmptyFilters( EventFilters filters) {
        if (isEmpty(filters.getOrderId()) && isEmpty(filters.getTransactionId())) {
            throw new ValidateException("OrderId or TransactionId must be informed");
            
        }
    }

    public Event save(Event event) {
        return repository.save(event);
    }

}
