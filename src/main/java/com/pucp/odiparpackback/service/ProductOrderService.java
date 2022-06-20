package com.pucp.odiparpackback.service;

import com.pucp.odiparpackback.request.ProductOrderRequest;
import com.pucp.odiparpackback.response.ProductOrderResponse;
import com.pucp.odiparpackback.response.StandardResponse;
import java.util.List;

public interface ProductOrderService {

  StandardResponse<List<ProductOrderResponse>> findAll();
  StandardResponse<Long> save(ProductOrderRequest request);
  StandardResponse<Long> update(ProductOrderRequest request);
  StandardResponse<Boolean> delete(Long id);
  StandardResponse<ProductOrderResponse> findOne(Long id);
}
