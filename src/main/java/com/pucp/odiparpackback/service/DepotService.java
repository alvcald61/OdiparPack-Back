package com.pucp.odiparpackback.service;

import com.pucp.odiparpackback.request.DepotRequest;

import java.util.List;

public interface DepotService {

    List<DepotRequest> findAll();

    DepotRequest findById(Long id);

    DepotRequest save(DepotRequest depotDto);

    void delete(Long id);
}
