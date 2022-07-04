package com.pucp.odiparpackback.controller;

import com.pucp.odiparpackback.request.RouteBlockListRequest;
import com.pucp.odiparpackback.response.RouteBlockResponse;
import com.pucp.odiparpackback.response.StandardResponse;
import com.pucp.odiparpackback.service.RouteBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/routeBlock")
public class RouteBlockController {

  @Autowired
  private RouteBlockService routeBlockService;

  @PostMapping("/listByDate")
  public ResponseEntity<StandardResponse<List<RouteBlockResponse>>> listByDate(@RequestBody RouteBlockListRequest request) {
    StandardResponse<List<RouteBlockResponse>> response = routeBlockService.listByDate(request.getStartDate(), request.getEndDate());
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping("/listAll")
  public ResponseEntity<StandardResponse<List<RouteBlockResponse>>> listAll() {
    StandardResponse<List<RouteBlockResponse>> response = routeBlockService.listAll();
    return ResponseEntity.status(response.getStatus()).body(response);
  }


  @PostMapping("/saveFile")
  public ResponseEntity<StandardResponse<String>> saveFile(@RequestPart("file") MultipartFile RouteBlockFile) {
    StandardResponse<String> response = routeBlockService.saveFile(RouteBlockFile);
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping("/clear")
  public ResponseEntity<StandardResponse<String>> deleteAll() {
    StandardResponse<String> response = routeBlockService.deleteAll();
    return ResponseEntity.status(response.getStatus()).body(response);
  }
}
