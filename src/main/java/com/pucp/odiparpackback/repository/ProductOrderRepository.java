package com.pucp.odiparpackback.repository;

import com.pucp.odiparpackback.model.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {
}
