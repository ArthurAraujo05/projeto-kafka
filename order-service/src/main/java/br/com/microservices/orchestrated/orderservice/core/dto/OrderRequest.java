package br.com.microservices.orchestrated.orderservice.core.dto;

import lombok.Data;

import java.util.List;

import br.com.microservices.orchestrated.orderservice.core.document.OrderProducts;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest { 

    private List<OrderProducts> products;

}
