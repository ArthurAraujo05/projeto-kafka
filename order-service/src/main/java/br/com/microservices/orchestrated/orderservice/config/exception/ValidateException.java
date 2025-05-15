package br.com.microservices.orchestrated.orderservice.config.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidateException extends RuntimeException {
    public ValidateException(String message) {
        super(message);
    }
}

    