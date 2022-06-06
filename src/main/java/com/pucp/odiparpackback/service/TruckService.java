package com.pucp.odiparpackback.service;

import com.pucp.odiparpackback.request.TruckRequest;

import java.util.List;

public interface TruckService {

    List<TruckRequest> findAll();
    TruckRequest save(TruckRequest truckDto);
}
