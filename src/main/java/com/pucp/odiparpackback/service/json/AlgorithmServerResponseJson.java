package com.pucp.odiparpackback.service.json;

import lombok.Data;

import java.util.List;

@Data
public class AlgorithmServerResponseJson {
  private List<AlgorithmResponseJson> orderList;
}
