package com.pucp.odiparpackback.service;

import com.pucp.odiparpackback.model.Truck;
import com.pucp.odiparpackback.request.BreakdownRequest;
import com.pucp.odiparpackback.response.BreakdownResponse;
import com.pucp.odiparpackback.response.StandardResponse;

public interface BreakdownService {
  StandardResponse<BreakdownResponse> getTruckBreakdown(Truck truck);
  StandardResponse<BreakdownResponse> createBreakdown(BreakdownRequest request);
}
