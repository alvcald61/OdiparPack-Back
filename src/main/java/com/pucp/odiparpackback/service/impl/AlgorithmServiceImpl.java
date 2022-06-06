package com.pucp.odiparpackback.service.impl;

import com.pucp.odiparpackback.exceptions.GenericCustomException;
import com.pucp.odiparpackback.repository.TruckRepository;
import com.pucp.odiparpackback.service.AlgorithmService;
import com.pucp.odiparpackback.service.json.AlgorithmServerRequestJson;
import com.pucp.odiparpackback.service.json.AlgorithmServerResponseJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AlgorithmServiceImpl implements AlgorithmService {

  public static final String ALGORITHM_ROUTE_URL = "http://localhost:8080/algorithm/route";

  @Autowired
  private TruckRepository truckRepository;
  private RestTemplate restTemplate;

  public AlgorithmServiceImpl() {
    this.restTemplate = new RestTemplate();
  }

  @Override
  public AlgorithmServerResponseJson getPath(AlgorithmServerRequestJson algorithmServerRequestJson) {
    try {
      AlgorithmServerRequestJson request = new AlgorithmServerRequestJson();
      request.setOrderList(algorithmServerRequestJson.getOrderList());
      request.setTruckList(algorithmServerRequestJson.getTruckList());
      return restTemplate.postForObject(ALGORITHM_ROUTE_URL, request, AlgorithmServerResponseJson.class);
    } catch (Exception e) {
      throw new GenericCustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }
}
