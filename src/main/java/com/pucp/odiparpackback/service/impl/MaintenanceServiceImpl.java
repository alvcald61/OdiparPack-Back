package com.pucp.odiparpackback.service.impl;

import com.pucp.odiparpackback.model.Maintenance;
import com.pucp.odiparpackback.model.Truck;
import com.pucp.odiparpackback.repository.MaintenanceRepository;
import com.pucp.odiparpackback.repository.TruckRepository;
import com.pucp.odiparpackback.response.ErrorResponse;
import com.pucp.odiparpackback.response.StandardResponse;
import com.pucp.odiparpackback.service.MaintenanceService;
import com.pucp.odiparpackback.utils.Message;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.pucp.odiparpackback.utils.Message.INCORRECT_FORMAT;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

  final
  MaintenanceRepository maintenanceRepository;

  final
  TruckRepository truckRepository;

  public MaintenanceServiceImpl(MaintenanceRepository maintenanceRepository, TruckRepository truckRepository) {
    this.maintenanceRepository = maintenanceRepository;
    this.truckRepository = truckRepository;
  }

  @Override
  public StandardResponse<List<Maintenance>> listAll() {
    List<Maintenance> maintenanceList = maintenanceRepository.findAll();
    return new StandardResponse<>(maintenanceList);
  }

  @Override
  public StandardResponse<List<Long>> listByDate(String startDate) {
    StandardResponse<List<Long>> response;
//    ErrorResponse errorResponse = validateDates(startDate);
//    if (Objects.nonNull(errorResponse)) {
//      response = new StandardResponse<>(errorResponse, HttpStatus.BAD_REQUEST);
//      return response;
//    }
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Date start = null;
    try {
      start = df.parse(startDate);
    } catch (ParseException e) {
      return new StandardResponse<>(new ErrorResponse(String.format(INCORRECT_FORMAT, "initialDate or FinalDate")), HttpStatus.BAD_REQUEST);
    }
    List<Maintenance> maintenanceList = maintenanceRepository.getAllByInitialDate(start);
    List<Long> trucksId = maintenanceList.stream().map(map -> map.getTruck().getId()).collect(Collectors.toList());
    return new StandardResponse<>(trucksId);
  }

  @Override
  public StandardResponse<String> saveFile(MultipartFile historicFile) {
    StandardResponse<String> response;

    if (Objects.isNull(historicFile)) {
      ErrorResponse error = new ErrorResponse(String.format(Message.NULL_FIELD, "file"));
      response = new StandardResponse<>(error, HttpStatus.BAD_REQUEST);
    } else {
      List<Maintenance> maintenanceList;
      try {
        InputStream is = historicFile.getInputStream();
        String line, name = historicFile.getOriginalFilename();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        maintenanceList = new ArrayList<>();
        while ((line = br.readLine()) != null) {
          try {
            String[] splitLine = line.split(":");
            String year = splitLine[0].substring(0, 4);
            String month = splitLine[0].substring(4, 6);
            String day = splitLine[0].substring(6, 8);
            String code = splitLine[1];
            Truck truck = truckRepository.findTruckByCode(code);
            if(truck==null)continue;
            Calendar initialDate = Calendar.getInstance();
            Calendar finalDate = initialDate;
            finalDate.add(Calendar.DATE, 1);
            initialDate.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
            maintenanceList.add(Maintenance.builder().initialDate(initialDate.getTime())
              .truck(truck).build());
          } catch (NumberFormatException e) {
            e.printStackTrace();
            return new StandardResponse<>(new ErrorResponse(String.format(Message.INCORRECT_FORMAT, "file")), HttpStatus.BAD_REQUEST);
          }
        }
        try {
          maintenanceRepository.saveAll(maintenanceList);
        } catch (Exception e) {
          e.printStackTrace();
          return new StandardResponse<>(new ErrorResponse(String.format(INCORRECT_FORMAT, name)), HttpStatus.BAD_REQUEST);
        }
        response = new StandardResponse<>("Se guardo el archivo");
      } catch (Exception ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage());
        response = new StandardResponse<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    return response;
  }

  @Override
  public StandardResponse<Maintenance> saveMaintenance(Maintenance maintenance) {
    StandardResponse<Maintenance> response;
    if (Objects.isNull(maintenance)) {
      ErrorResponse error = new ErrorResponse(String.format(Message.NULL_FIELD, "maintenance"));
      response = new StandardResponse<>(error, HttpStatus.BAD_REQUEST);
    } else {
      Maintenance maintenanceSaved = maintenanceRepository.save(maintenance);
      response = new StandardResponse<>(maintenanceSaved);
    }
    return response;
  }

  @Override
  public StandardResponse<String> deleteAll() {
    maintenanceRepository.deleteAll();
    return new StandardResponse<>("Se eliminaron todos los mantenimientos");
  }

}
