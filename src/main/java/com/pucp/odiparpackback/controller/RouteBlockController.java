package com.pucp.odiparpackback.controller;

import com.pucp.odiparpackback.response.StandardResponse;
import com.pucp.odiparpackback.service.RouteBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/routeBlock")
public class RouteBlockController {

  @Autowired
  private RouteBlockService routeBlockService;

  //  @GetMapping("/list")
//  public ResponseEntity<StandardResponse<List<RouteBlock>>> listAll() {
//    StandardResponse<List<RouteBlock>> response = routeBlockService.listAll();
//    return ResponseEntity.status(response.getStatus()).body(response);
//  }
//
//  @PostMapping("/generateByDate")
//  public ResponseEntity<StandardResponse<List<RouteBlockGeneratorResponse>>> listByDate(@RequestBody RouteBlockListRequest request) {
//    StandardResponse<List<RouteBlockGeneratorResponse>> response = routeBlockService.listByDate(request.getStartDate(), request.getEndDate());
//    return ResponseEntity.status(response.getStatus()).body(response);
//  }
//
  @PostMapping("/saveFile")
  public ResponseEntity<StandardResponse<String>> saveFile(@RequestPart("file") MultipartFile RouteBlockFile) {
    StandardResponse<String> response = routeBlockService.saveFile(RouteBlockFile);
    return ResponseEntity.status(response.getStatus()).body(response);
  }
//
//  @PostMapping("/save")
//  public ResponseEntity<StandardResponse<RouteBlock>> saveRouteBlock(RouteBlock RouteBlock) {
//    StandardResponse<RouteBlock> response = routeBlockService.saveRouteBlock(RouteBlock);
//    return ResponseEntity.status(response.getStatus()).body(response);
//  }
//
//  @DeleteMapping("/clear")
//  public ResponseEntity<StandardResponse<String>> deleteAll() {
//    StandardResponse<String> response = routeBlockService.deleteAll();
//    return ResponseEntity.status(response.getStatus()).body(response);
//  }
}
