package br.com.microservices.orchestrated.paymentservice.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import br.com.microservices.orchestrated.paymentservice.core.enums.ESagaStatus;


@Data
@Builder
@AllArgsConstructor 
@NoArgsConstructor
public class Event {

    private String id;
    private String transactionId;
    private String orderId;
    private Order payload;
    private String source;
    private ESagaStatus status;
    private List<History> eveHistory;
    private LocalDateTime createdAt;

}
