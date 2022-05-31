package com.pucp.odiparpackback.service;

import com.pucp.odiparpackback.dto.TruckDto;
import com.pucp.odiparpackback.exceptions.GenericCustomException;
import com.pucp.odiparpackback.repository.TruckRepository;
import com.pucp.odiparpackback.utils.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TruckService {

  private final TruckRepository truckRepository;

  private final ObjectMapper objectMapper;

  public TruckService(TruckRepository truckRepository, ObjectMapper objectMappexr) {
    this.truckRepository = truckRepository;
    this.objectMapper = objectMappexr;
  }


  public List<TruckDto> findAll() {
    try {
      return truckRepository.findByAvailableTrue().stream().map(objectMapper::truckToDto).collect(Collectors.toList());
    } catch (Exception e) {
      throw new GenericCustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }


  public TruckDto save(TruckDto truckDto) {
    return objectMapper.truckToDto(truckRepository.save(objectMapper.dtoToTruck(truckDto)));
  }
}
