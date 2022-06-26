package com.pucp.odiparpackback.service;

import com.pucp.odiparpackback.request.TruckRequest;
import com.pucp.odiparpackback.response.StandardResponse;
import com.pucp.odiparpackback.response.TruckResponse;

import java.util.List;

public interface TruckService {

    StandardResponse<List<TruckResponse>> findAll();
    TruckRequest save(TruckRequest truckDto);
}
