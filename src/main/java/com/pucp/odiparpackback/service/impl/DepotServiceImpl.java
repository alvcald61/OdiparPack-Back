package com.pucp.odiparpackback.service.impl;

import com.pucp.odiparpackback.request.DepotRequest;
import com.pucp.odiparpackback.repository.DepotRepository;
import com.pucp.odiparpackback.service.DepotService;
import com.pucp.odiparpackback.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepotServiceImpl implements DepotService {

  @Autowired
  private DepotRepository depotRepository;

  private final ObjectMapper objectMapper;

  public DepotServiceImpl(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public List<DepotRequest> findAll() {
    return depotRepository.findAll().stream().map(objectMapper::depotToDto).collect(Collectors.toList());
  }

  @Override
  public DepotRequest findById(Long id) {
    return objectMapper.depotToDto(depotRepository.findById(id).orElse(null));
  }

  @Override
  public DepotRequest save(DepotRequest depotDto) {
    return objectMapper.depotToDto(depotRepository.save(objectMapper.dtoToDepot(depotDto)));
  }

  @Override
  public void delete(Long id) {
    depotRepository.deleteById(id);
  }

}
