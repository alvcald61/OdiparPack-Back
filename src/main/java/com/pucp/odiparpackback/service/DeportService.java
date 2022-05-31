package com.pucp.odiparpackback.service;

import com.pucp.odiparpackback.dto.DepotDto;
import com.pucp.odiparpackback.repository.DepotRepository;
import com.pucp.odiparpackback.utils.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeportService {

  private final DepotRepository depotRepository;

  private final ObjectMapper objectMapper;

  public DeportService(DepotRepository depotRepository, ObjectMapper objectMapper) {
    this.depotRepository = depotRepository;
    this.objectMapper = objectMapper;
  }

  public List<DepotDto> findAll() {
    return depotRepository.findAll().stream().map(objectMapper::depotToDto).collect(Collectors.toList());
  }

  public DepotDto findById(Long id) {
    return objectMapper.depotToDto(depotRepository.findById(id).orElse(null));
  }

  public DepotDto save(DepotDto depotDto) {
    return objectMapper.depotToDto(depotRepository.save(objectMapper.dtoToDepot(depotDto)));
  }

  public void delete(Long id) {
    depotRepository.deleteById(id);
  }

}
