package br.com.microservices.orchestrated.productvalidationservice.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.microservices.orchestrated.productvalidationservice.core.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Boolean existsByCode(String code);
}
