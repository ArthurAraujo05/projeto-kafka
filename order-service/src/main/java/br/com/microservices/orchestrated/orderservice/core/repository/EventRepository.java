package br.com.microservices.orchestrated.orderservice.core.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import br.com.microservices.orchestrated.orderservice.core.document.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends MongoRepository<Event, String>{

    List<Event> findAllByOrderByCreatedAtDesc();    

    Optional<Event> findTop1ByOrderIdOrderByCreatedAtDesc(String OrderId);

    Optional<Event> findTop1ByTransactionIdOrderByCreatedAtDesc(String transactionId);

}
