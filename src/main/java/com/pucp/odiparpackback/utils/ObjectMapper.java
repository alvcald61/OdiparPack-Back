package com.pucp.odiparpackback.utils;

import com.pucp.odiparpackback.dto.ProductOrderDto;
import com.pucp.odiparpackback.model.ProductOrder;
import org.modelmapper.ModelMapper;

public class ObjectMapper {
  private static final ModelMapper modelMapper = new ModelMapper();
  
  private static <T, U> T map(U source, Class<T> destinationClass) {
    return modelMapper.map(source, destinationClass);
  }
  
  public static  ProductOrderDto productOrderToDto(ProductOrder source) {
    ProductOrderDto productOrderDto = map(source, ProductOrderDto.class);
    productOrderDto.setState(source.getState());
    productOrderDto.setMaxDeliveryDate(source.getMaxDeliveryDate());
    productOrderDto.setRegistryDate(source.getRegistryDate());
    return productOrderDto;
  }
  
  public static ProductOrder dtoToProductOrder(ProductOrderDto source) {
    ProductOrder productOrder = map(source, ProductOrder.class);
    productOrder.setState(source.getDeliveryState());
    productOrder.setMaxDeliveryDate(source.getMaxDeliveryDate());
    productOrder.setRegistryDate(source.getRegistryDate());
    return productOrder;
  }
  
}
