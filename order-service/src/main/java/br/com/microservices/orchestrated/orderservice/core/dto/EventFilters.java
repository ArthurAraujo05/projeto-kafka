package br.com.microservices.orchestrated.orderservice.core.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventFilters {
    
    private String orderId;
    private String transactionId;

}
