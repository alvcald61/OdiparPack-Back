package com.pucp.odiparpackback.service;

import com.pucp.odiparpackback.model.Maintenance;
import com.pucp.odiparpackback.response.StandardResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MaintenanceService {
  StandardResponse<List<Maintenance>> listAll();

  StandardResponse<List<Long>> listByDate(String startDate);

  StandardResponse<String> saveFile(MultipartFile maintenanceFile);

  StandardResponse<Maintenance> saveMaintenance(Maintenance maintenance);

  StandardResponse<String> deleteAll();

}
