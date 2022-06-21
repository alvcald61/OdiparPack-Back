package com.pucp.odiparpackback.controller;

import com.pucp.odiparpackback.model.Maintenance;
import com.pucp.odiparpackback.response.StandardResponse;
import com.pucp.odiparpackback.service.MaintenanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController()
@RequestMapping("/maintenance")
public class MaintenanceController {

  final
  MaintenanceService maintenanceService;

  public MaintenanceController(MaintenanceService maintenanceService) {
    this.maintenanceService = maintenanceService;
  }

  @PostMapping("/saveFile")
  public ResponseEntity<StandardResponse<String>> saveFile(@RequestPart("file") MultipartFile historicFile) {
    StandardResponse<String> response = maintenanceService.saveFile(historicFile);
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @GetMapping("/list")
  public ResponseEntity<StandardResponse<List<Maintenance>>> listAll() {
    StandardResponse<List<Maintenance>> response = maintenanceService.listAll();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping("/getByDate")
  public ResponseEntity<StandardResponse<List<Long>>> listByDate(@RequestParam("startDate") String startDate) {
    StandardResponse<List<Long>> response = maintenanceService.listByDate(startDate);
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping("/save")
  public ResponseEntity<StandardResponse<Maintenance>> saveHistoric(Maintenance maintenance) {
    StandardResponse<Maintenance> response = maintenanceService.saveMaintenance(maintenance);
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping("/clear")
  public ResponseEntity<StandardResponse<String>> deleteAll() {
    StandardResponse<String> response = maintenanceService.deleteAll();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

}
