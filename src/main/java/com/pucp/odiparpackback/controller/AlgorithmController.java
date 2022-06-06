package com.pucp.odiparpackback.controller;

import com.pucp.odiparpackback.response.ErrorResponse;
import com.pucp.odiparpackback.response.StandardResponse;
import com.pucp.odiparpackback.exceptions.GenericCustomException;
import com.pucp.odiparpackback.service.AlgorithmService;
import com.pucp.odiparpackback.service.json.AlgorithmServerRequestJson;
import com.pucp.odiparpackback.service.json.AlgorithmServerResponseJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/algorithm")
public class AlgorithmController {

  @Autowired
  private AlgorithmService algorithmService;

  @PostMapping
  public ResponseEntity<StandardResponse<AlgorithmServerResponseJson>> getPath(@RequestBody AlgorithmServerRequestJson algorithmServerRequestJson) {
    try {
      AlgorithmServerResponseJson response = algorithmService.getPath(algorithmServerRequestJson);
      return ResponseEntity.ok(new StandardResponse<>(response));
    } catch (GenericCustomException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StandardResponse<>(new ErrorResponse(e.getMessage())));
    }
  }
}
