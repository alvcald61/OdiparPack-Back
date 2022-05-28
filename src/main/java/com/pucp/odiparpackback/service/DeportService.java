package com.pucp.odiparpackback.service;

import com.pucp.odiparpackback.dto.DepotDto;
import com.pucp.odiparpackback.repository.DepotRepository;
import com.pucp.odiparpackback.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeportService {

  @Autowired
  private DepotRepository depotRepository;

  public List<DepotDto> findAll() {
    return depotRepository.findAll().stream().map(ObjectMapper::depotToDto).collect(Collectors.toList());
  }

  public DepotDto findById(Long id) {
    return ObjectMapper.depotToDto(depotRepository.findById(id).orElse(null));
  }

  public DepotDto save(DepotDto depotDto) {
    return ObjectMapper.depotToDto(depotRepository.save(ObjectMapper.dtoToDepot(depotDto)));
  }

  public void delete(Long id) {
    depotRepository.deleteById(id);
  }

}
