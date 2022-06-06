package com.pucp.odiparpackback.request;

import com.pucp.odiparpackback.utils.OrderState;
import com.pucp.odiparpackback.utils.TimeUtil;
import lombok.Getter;

import java.util.Date;


public class ProductOrderRequest {
  
  @Getter
  private Long id;
  
  private String registryDate;
  
  private Double amount;
  
  private String state;
  
  private String maxDeliveryDate;
  
  
  public OrderState getDeliveryState() {
    return OrderState.valueOf(state);
  }

  public void setState(OrderState state) {
    this.state = state.name();
  }

  public Date getRegistryDate()  {
    return TimeUtil.parseDate(registryDate);
  }

  public void setRegistryDate(Date registryDate) {
    this.registryDate = TimeUtil.formatDate(registryDate);
  }

  public Date getMaxDeliveryDate()  {
    return TimeUtil.parseDate(maxDeliveryDate);
  }

  public void setMaxDeliveryDate(Date maxDeliveryDate) {
    this.maxDeliveryDate = TimeUtil.formatDate(maxDeliveryDate);
  }
}
