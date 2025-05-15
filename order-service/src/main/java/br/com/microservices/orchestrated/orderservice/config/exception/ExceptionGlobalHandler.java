package br.com.microservices.orchestrated.orderservice.config.exception;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionGlobalHandler {

    @ExceptionHandler(ValidateException.class)
    public ResponseEntity<?> handleValidationException(ValidateException ex) {
        var exceptionDetails = new ExceptionDetails(400, ex.getMessage());
        return new ResponseEntity<>(exceptionDetails, null, 400);
    }
}
