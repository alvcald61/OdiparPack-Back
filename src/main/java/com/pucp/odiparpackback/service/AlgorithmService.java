package com.pucp.odiparpackback.service;

import com.pucp.odiparpackback.exceptions.GenericCustomException;
import com.pucp.odiparpackback.service.json.AlgorithmRequestJson;
import com.pucp.odiparpackback.service.json.AlgorithmResponseJson;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AlgorithmService {

  public static final String ALGORITHM_ROUTE_URL = "http://localhost:8080/algorithm/route";
  private RestTemplate restTemplate;

  public AlgorithmService() {
    this.restTemplate = new RestTemplate();
  }

  public Object getPath(List<AlgorithmRequestJson> algorithmRequestJson) {
    try{
      AlgorithmResponseJson response = restTemplate.postForObject(ALGORITHM_ROUTE_URL, algorithmRequestJson, AlgorithmResponseJson.class);
      return response;
    }
    catch (Exception e){
      throw new GenericCustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }
}
