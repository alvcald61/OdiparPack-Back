package com.pucp.odiparpackback.controller;

import com.pucp.odiparpackback.exceptions.GenericCustomException;
import com.pucp.odiparpackback.service.AlgorithmService;
import com.pucp.odiparpackback.service.json.AlgorithmRequestJson;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/algorithm")
public class AlgorithmController {

  private final AlgorithmService algorithmService;

  public AlgorithmController(AlgorithmService algorithmService) {
    this.algorithmService = algorithmService;
  }

  @PostMapping
  public ResponseEntity<?> getPath(@RequestBody List<AlgorithmRequestJson> algorithmRequestJson) {
    try {
      Object objeto = algorithmService.getPath(algorithmRequestJson);
      return null;
    }
    catch (GenericCustomException e) {
      return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }
  }
}
