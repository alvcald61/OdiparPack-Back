package com.pucp.odiparpackback.service;

import com.pucp.odiparpackback.exceptions.GenericCustomException;
import com.pucp.odiparpackback.repository.TruckRepository;
import com.pucp.odiparpackback.service.json.AlgorithmServerRequestJson;
import com.pucp.odiparpackback.service.json.AlgorithmServerResponseJson;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AlgorithmService {

  public static final String ALGORITHM_ROUTE_URL = "http://localhost:8080/algorithm/route";
  private final TruckRepository truckRepository;
  private RestTemplate restTemplate;

  public AlgorithmService(TruckRepository truckRepository) {
    this.restTemplate = new RestTemplate();
    this.truckRepository = truckRepository;
  }

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
