package com.pucp.odiparpackback.service;

import com.pucp.odiparpackback.response.StandardResponse;
import com.pucp.odiparpackback.response.TransportationPlanResponse;

import java.util.List;

public interface TransportationPlanService {
  StandardResponse<List<TransportationPlanResponse>> findAll();
  StandardResponse<Integer> save();
  StandardResponse<TransportationPlanResponse> update();
  StandardResponse<String> delete();
  StandardResponse<String> deleteAll();

}
