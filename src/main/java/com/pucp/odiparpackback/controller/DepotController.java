package com.pucp.odiparpackback.controller;

import com.pucp.odiparpackback.response.ErrorResponse;
import com.pucp.odiparpackback.response.StandardResponse;
import com.pucp.odiparpackback.request.DepotRequest;
import com.pucp.odiparpackback.exceptions.GenericCustomException;
import com.pucp.odiparpackback.service.DepotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/depot")
public class DepotController {

  @Autowired
  private DepotService depotService;

  @GetMapping
  public ResponseEntity<StandardResponse<List<DepotRequest>>> findAll() {
    try {
      List<DepotRequest> list = depotService.findAll();
      return ResponseEntity.ok(new StandardResponse<>(list));
    } catch (GenericCustomException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StandardResponse<>(new ErrorResponse(e.getMessage())));
    }
  }

  @PostMapping
  public ResponseEntity<StandardResponse<DepotRequest>> save(DepotRequest depotDto) {
    try {
      DepotRequest depot = depotService.save(depotDto);
      return ResponseEntity.ok(new StandardResponse<>(depot));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StandardResponse<>(new ErrorResponse(e.getMessage())));
    }
  }
}
