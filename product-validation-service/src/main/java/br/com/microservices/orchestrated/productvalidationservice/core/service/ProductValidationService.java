package br.com.microservices.orchestrated.productvalidationservice.core.service;

import org.springframework.stereotype.Service;
import static org.springframework.util.ObjectUtils.isEmpty;
import static br.com.microservices.orchestrated.productvalidationservice.core.enums.ESagaStatus.FAIL;
import static br.com.microservices.orchestrated.productvalidationservice.core.enums.ESagaStatus.ROLLBACK_PENDING;
import static br.com.microservices.orchestrated.productvalidationservice.core.enums.ESagaStatus.SUCCESS;
import br.com.microservices.orchestrated.productvalidationservice.config.exception.ValidationException;
import br.com.microservices.orchestrated.productvalidationservice.core.dto.Event;
import br.com.microservices.orchestrated.productvalidationservice.core.dto.History;
import br.com.microservices.orchestrated.productvalidationservice.core.dto.OrderProducts;
import br.com.microservices.orchestrated.productvalidationservice.core.model.Validation;
import br.com.microservices.orchestrated.productvalidationservice.core.producer.KafkaProducer;
import br.com.microservices.orchestrated.productvalidationservice.core.repository.ProductRepository;
import br.com.microservices.orchestrated.productvalidationservice.core.repository.ValidationRepository;
import br.com.microservices.orchestrated.productvalidationservice.core.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class ProductValidationService {

    private static final String CURRENT_SOURCER = "PRODUCT_VALIDATION_SERVICE";

    private final JsonUtil jsonUtil;
    private final KafkaProducer producer;
    private final ProductRepository productRepository;
    private final ValidationRepository validationRepository;

    public void validateExistingProducts(Event event) {
        try {
            checkCurrentValidation(event);
            createValidation(event, true);
            handleSuccess(event);
        } catch (Exception ex) {
            log.error("Error validating product: {}", ex.getMessage());
            handleFailCurrentNotExecuted(event, ex.getMessage());
        }
        producer.sendEvent(jsonUtil.toJson(event));
    }

    private void validateProductInformed(Event event) {
        if (isEmpty(event.getPayload()) || isEmpty(event.getPayload().getProducts())) {
            throw new ValidationException("Product list is empty!");
        }
        if (isEmpty(event.getPayload().getId()) || isEmpty(event.getTransactionId())) {
            throw new ValidationException("OrderID and TransactionID must be informed!");
        }
    }

    private void checkCurrentValidation(Event event) {
        validateProductInformed(event);
        if (validationRepository.existsByOrderIdAndTransactionId(event.getOrderId(), event.getTransactionId())) {
            throw new ValidationException("This order has already been validated!");
        }
        event.getPayload().getProducts().forEach(product -> {
            validateProductInformed(product);
            validateExistingProduct(product.getProduct() .getCode());
        });
    }

    private void validateProductInformed(OrderProducts product) {
        if (isEmpty(product.getProduct()) || isEmpty(product.getProduct().getCode())) {
            throw new ValidationException("Product code must be informed!");
        }
    }

    private void validateExistingProduct(String code) {
        if (!productRepository.existsByCode(code)) {
            throw new ValidationException(" does not exist!");
        }
    }

    private void createValidation(Event event, boolean Success) {
        var validation = Validation
                .builder()
                .orderId(event.getOrderId())
                .transactionId(event.getTransactionId())
                .success(Success)
                .build();
        validationRepository.save(validation);
    }       

    private void handleSuccess(Event event) {
        event.setStatus(SUCCESS);
        event.setSource(CURRENT_SOURCER);
        addHistory(event, "Product validation successful");
    }

    private void addHistory(Event event, String message) {
        var history = History
                .builder()
                .source(event.getSource())
                .status(event.getStatus())
                .createdAt(LocalDateTime.now())
                .message(message)
                .build();
        event.addToHistory(history);
    }

    private void handleFailCurrentNotExecuted(Event event, String message) {
        event.setStatus(ROLLBACK_PENDING);
        event.setSource(CURRENT_SOURCER);
        addHistory(event, "Product validation failed: " .concat(message));
    }

    public void rollbackEvent(Event event){
        changeValidationToFail(event);
        event.setStatus(FAIL);
        event.setSource(CURRENT_SOURCER);
        addHistory(event, "Product validation rollback executed");
        producer.sendEvent(jsonUtil.toJson(event));
    }

    private void changeValidationToFail(Event event){
        validationRepository
            .findByOrderIdAndTransactionId(event.getPayload().getId(), event.getTransactionId())
            .ifPresentOrElse(validation -> {
                validation.setSuccess(false);
                validationRepository.save(validation);
            },
            () -> createValidation(event, false));
    }
}