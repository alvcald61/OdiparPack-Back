package com.pucp.odiparpackback.controller;

import com.pucp.odiparpackback.controller.json.ErrorJson;
import com.pucp.odiparpackback.controller.json.ResponseJson;
import com.pucp.odiparpackback.dto.CityDto;
import com.pucp.odiparpackback.exceptions.GenericCustomException;
import com.pucp.odiparpackback.model.City;
import com.pucp.odiparpackback.service.CityService;
import com.pucp.odiparpackback.utils.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/city")
public class CityController {

  private final CityService cityService;

  public CityController(CityService cityService) {
    this.cityService = cityService;
  }

  @GetMapping
  public ResponseEntity<ResponseJson<List<CityDto>>> getAllCities() {
    try {
      return ResponseEntity.ok(new ResponseJson<>(cityService.findAll()));
    } catch (GenericCustomException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseJson<>(new ErrorJson(e.getMessage())));
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<ResponseJson<CityDto>> getAllCities(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(new ResponseJson<>(cityService.findById(id)));
    } catch (GenericCustomException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseJson<>(new ErrorJson(e.getMessage())));
    }
  }

  @PostMapping
  public ResponseEntity<ResponseJson<CityDto>> createCity(@RequestBody CityDto city) {
    try {
      return ResponseEntity.ok(new ResponseJson<>(ObjectMapper.cityToDto(cityService.save(city))));
    } catch (GenericCustomException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseJson<>(new ErrorJson(e.getMessage())));
    }
  }

  @PutMapping
  public ResponseEntity<ResponseJson<CityDto>> updateCity(@RequestBody City city) {
    try {
      return ResponseEntity.ok(new ResponseJson<>(ObjectMapper.cityToDto(cityService.update(city))));
    } catch (GenericCustomException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseJson<>(new ErrorJson(e.getMessage())));
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ResponseJson<CityDto>> deleteCity(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(new ResponseJson<>(ObjectMapper.cityToDto(cityService.delete(id))));
    } catch (GenericCustomException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseJson<>(new ErrorJson(e.getMessage())));
    }
  }
}