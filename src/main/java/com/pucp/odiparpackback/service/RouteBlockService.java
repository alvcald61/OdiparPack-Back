package com.pucp.odiparpackback.service;

import com.pucp.odiparpackback.model.RouteBlock;
import com.pucp.odiparpackback.response.RouteBlockResponse;
import com.pucp.odiparpackback.response.StandardResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RouteBlockService {
  StandardResponse<List<RouteBlockResponse>> listAll();

  StandardResponse<String> saveFile(MultipartFile routeBlockFile);

  StandardResponse<RouteBlock> saveRouteBlock(RouteBlock routeBlock);

  StandardResponse<List<RouteBlockResponse>> listByDate(String startDate, String endDate);

  StandardResponse<String> deleteAll();
}
