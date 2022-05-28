package com.pucp.odiparpackback.controller;

import com.pucp.odiparpackback.controller.json.ErrorJson;
import com.pucp.odiparpackback.controller.json.ResponseJson;
import com.pucp.odiparpackback.exceptions.GenericCustomException;
import com.pucp.odiparpackback.service.AlgorithmService;
import com.pucp.odiparpackback.service.json.AlgorithmServerRequestJson;
import com.pucp.odiparpackback.service.json.AlgorithmServerResponseJson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/algorithm")
public class AlgorithmController {

  private final AlgorithmService algorithmService;

  public AlgorithmController(AlgorithmService algorithmService) {
    this.algorithmService = algorithmService;
  }

  @PostMapping
  public ResponseEntity<ResponseJson<AlgorithmServerResponseJson>> getPath(@RequestBody AlgorithmServerRequestJson algorithmServerRequestJson) {
    try {
      AlgorithmServerResponseJson response = algorithmService.getPath(algorithmServerRequestJson);
      return ResponseEntity.ok(new ResponseJson<>(response));
    } catch (GenericCustomException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseJson<>(new ErrorJson(e.getMessage())));
    }
  }
}
