package com.pucp.odiparpackback.controller;

import com.pucp.odiparpackback.response.ErrorResponse;
import com.pucp.odiparpackback.response.StandardResponse;
import com.pucp.odiparpackback.request.CityRequest;
import com.pucp.odiparpackback.exceptions.GenericCustomException;
import com.pucp.odiparpackback.model.City;
import com.pucp.odiparpackback.service.impl.CityService;
import com.pucp.odiparpackback.utils.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/city")
public class CityController {

  private final CityService cityService;

  private final ObjectMapper objectMapper;

  public CityController(CityService cityService, ObjectMapper objectMapper) {
    this.cityService = cityService;
    this.objectMapper = objectMapper;
  }

  @GetMapping
  public ResponseEntity<StandardResponse<List<CityRequest>>> getAllCities() {
    try {
      return ResponseEntity.ok(new StandardResponse<>(cityService.findAll()));
    } catch (GenericCustomException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StandardResponse<>(new ErrorResponse(e.getMessage())));
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<StandardResponse<CityRequest>> getAllCities(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(new StandardResponse<>(cityService.findById(id)));
    } catch (GenericCustomException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StandardResponse<>(new ErrorResponse(e.getMessage())));
    }
  }

  @PostMapping
  public ResponseEntity<StandardResponse<CityRequest>> createCity(@RequestBody CityRequest city) {
    try {
      return ResponseEntity.ok(new StandardResponse<>(objectMapper.cityToDto(cityService.save(city))));
    } catch (GenericCustomException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StandardResponse<>(new ErrorResponse(e.getMessage())));
    }
  }

  @PutMapping
  public ResponseEntity<StandardResponse<CityRequest>> updateCity(@RequestBody City city) {
    try {
      return ResponseEntity.ok(new StandardResponse<>(objectMapper.cityToDto(cityService.update(city))));
    } catch (GenericCustomException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StandardResponse<>(new ErrorResponse(e.getMessage())));
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<StandardResponse<CityRequest>> deleteCity(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(new StandardResponse<>(objectMapper.cityToDto(cityService.delete(id))));
    } catch (GenericCustomException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StandardResponse<>(new ErrorResponse(e.getMessage())));
    }
  }
}