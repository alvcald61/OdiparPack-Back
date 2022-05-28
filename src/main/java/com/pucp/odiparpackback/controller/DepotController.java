package com.pucp.odiparpackback.controller;

import com.pucp.odiparpackback.controller.json.ErrorJson;
import com.pucp.odiparpackback.controller.json.ResponseJson;
import com.pucp.odiparpackback.dto.DepotDto;
import com.pucp.odiparpackback.exceptions.GenericCustomException;
import com.pucp.odiparpackback.service.DeportService;
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
  private DeportService deportService;

  @GetMapping
  public ResponseEntity<ResponseJson<List<DepotDto>>> findAll() {
    try {
      List<DepotDto> list = deportService.findAll();
      return ResponseEntity.ok(new ResponseJson<>(list));
    } catch (GenericCustomException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseJson<>(new ErrorJson(e.getMessage())));
    }
  }

  @PostMapping
  public ResponseEntity<ResponseJson<DepotDto>> save(DepotDto depotDto) {
    try {
      DepotDto depot = deportService.save(depotDto);
      return ResponseEntity.ok(new ResponseJson<>(depot));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseJson<>(new ErrorJson(e.getMessage())));
    }
  }
}
