package com.pucp.odiparpackback.repository;

import com.pucp.odiparpackback.model.ProductOrder;
import com.pucp.odiparpackback.utils.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {
  List<ProductOrder> findAllByState(OrderState state);
  List<ProductOrder> findAllByStateLessThanEqual(OrderState state);
}
