package com.pucp.odiparpackback.service.impl;

import com.pucp.odiparpackback.request.TruckRequest;
import com.pucp.odiparpackback.exceptions.GenericCustomException;
import com.pucp.odiparpackback.repository.TruckRepository;
import com.pucp.odiparpackback.service.TruckService;
import com.pucp.odiparpackback.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TruckServiceImpl implements TruckService {

  @Autowired
  private TruckRepository truckRepository;

  private final ObjectMapper objectMapper;

  public TruckServiceImpl(ObjectMapper objectMappexr) {
    this.objectMapper = objectMappexr;
  }

  @Override
  public List<TruckRequest> findAll() {
    try {
      return truckRepository.findByAvailableTrue().stream().map(objectMapper::truckToDto).collect(Collectors.toList());
    } catch (Exception e) {
      throw new GenericCustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  @Override
  public TruckRequest save(TruckRequest truckDto) {
    return objectMapper.truckToDto(truckRepository.save(objectMapper.dtoToTruck(truckDto)));
  }
}
