package br.com.microservices.orchestrated.orchestratorservice.Config.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionGlobalHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException ex) {
        var exceptionDetails = new ExceptionDetails(400, ex.getMessage());
        return new ResponseEntity<>(exceptionDetails, null, 400);
    }
}
