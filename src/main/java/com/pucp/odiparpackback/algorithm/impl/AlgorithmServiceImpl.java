package com.pucp.odiparpackback.algorithm.impl;

import com.pucp.odiparpackback.exceptions.GenericCustomException;
import com.pucp.odiparpackback.repository.TruckRepository;
import com.pucp.odiparpackback.algorithm.AlgorithmService;
import com.pucp.odiparpackback.algorithm.request.AlgorithmRequest;
import com.pucp.odiparpackback.algorithm.response.AlgorithmResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AlgorithmServiceImpl implements AlgorithmService {

  public static final String ALGORITHM_ROUTE_URL = "http://inf226g5.inf.pucp.edu.pe:8080/algorithm/route";

  @Autowired
  private TruckRepository truckRepository;
  private final RestTemplate restTemplate;

  public AlgorithmServiceImpl() {
    this.restTemplate = new RestTemplate();
  }

  @Override
  public AlgorithmResponse getPath(AlgorithmRequest request) {
    try {
      return restTemplate.postForObject(ALGORITHM_ROUTE_URL, request, AlgorithmResponse.class);
    } catch (Exception e) {
      throw new GenericCustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }
}
