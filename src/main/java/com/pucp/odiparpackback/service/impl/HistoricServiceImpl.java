package com.pucp.odiparpackback.service.impl;

import com.pucp.odiparpackback.model.City;
import com.pucp.odiparpackback.model.Historic;
import com.pucp.odiparpackback.repository.CityRepository;
import com.pucp.odiparpackback.repository.HistoricRepository;
import com.pucp.odiparpackback.response.ErrorResponse;
import com.pucp.odiparpackback.response.HistoricGeneratorResponse;
import com.pucp.odiparpackback.response.StandardResponse;
import com.pucp.odiparpackback.service.HistoricService;
import com.pucp.odiparpackback.utils.Message;
import com.pucp.odiparpackback.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.pucp.odiparpackback.utils.TimeUtil.validateDates;

@Service
public class HistoricServiceImpl implements HistoricService {
  @Autowired
  HistoricRepository historicRepository;

  @Autowired
  CityRepository cityRepository;

  @Override
  public StandardResponse<List<Historic>> listAll() {
    List<Historic> historicList = historicRepository.findAll();
    return new StandardResponse<>(historicList);
  }

  @Override
  public StandardResponse<List<HistoricGeneratorResponse>> listByDate(String startDate, String endDate) {
    StandardResponse<List<HistoricGeneratorResponse>> response;
    List<HistoricGeneratorResponse> generatedList;
    ErrorResponse errorResponse = validateDates(startDate);
    ErrorResponse errorResponse2 = validateDates(endDate);
    if (Objects.nonNull(errorResponse) || Objects.nonNull(errorResponse2)) {
      response = new StandardResponse<>(errorResponse, HttpStatus.BAD_REQUEST);
      return response;
    }

    try {
      Date start = TimeUtil.parseDate(startDate);
      Date end = TimeUtil.parseDate(endDate);
      List<Historic> historicList = historicRepository.findAllByOrderDateBetween(start, end);
      List<String> ubigeoList = new ArrayList<>();
      for (Historic h : historicList) {
        ubigeoList.add(h.getDestinationNode());
      }

      List<City> cityList = cityRepository.findAllByUbigeoList(ubigeoList);

      generatedList = new ArrayList<>();
      for (Historic h : historicList) {
        City city = cityList.stream().filter(c -> c.getUbigeo().equals(h.getDestinationNode())).findFirst().orElse(null);
        if (Objects.isNull(city)) {
          errorResponse = new ErrorResponse("No se encontro ciudad con ese ubigeo");
          response = new StandardResponse<>(errorResponse, HttpStatus.BAD_REQUEST);
          return response;
        }
        HistoricGeneratorResponse hgr = HistoricGeneratorResponse.builder().orderId(h.getId()).clientId(h.getClientId())
          .date(h.getOrderDate().toString()).startingNode(h.getStartingNode()).destinationNode(h.getDestinationNode())
          .remainingTime(city.getRegion().getMaxHours()).packages(h.getPackages()).build();
        generatedList.add(hgr);
      }
      response = new StandardResponse<>(generatedList);
    } catch (ParseException ex) {
      errorResponse = new ErrorResponse(ex.getMessage());
      response = new StandardResponse<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    return response;
  }

  @Override
  public StandardResponse<String> saveFile(MultipartFile historicFile) {
    StandardResponse<String> response;

    if (Objects.isNull(historicFile)) {
      ErrorResponse error = new ErrorResponse(String.format(Message.NULL_FIELD, "file"));
      response = new StandardResponse<>(error, HttpStatus.BAD_REQUEST);
    } else {
      List<Historic> historicList;
      try {
        InputStream is = historicFile.getInputStream();
        String line, name = historicFile.getOriginalFilename();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        historicList = new ArrayList<>();
        while ((line = br.readLine()) != null) {
          String[] splitLine = line.split(",");
          String date = splitLine[0];
          int demand = Integer.parseInt(splitLine[2].trim());
          int clientId = Integer.parseInt(splitLine[3].trim());

          splitLine = splitLine[1].trim().split(" =>  ");
          String startUbigeo = splitLine[0];
          String endUbigeo = splitLine[1];

          historicList.add(Historic.builder().orderDate(stringToDatetime(date, name)).packages(demand)
            .clientId(clientId).startingNode(startUbigeo).destinationNode(endUbigeo).build());
        }

        historicRepository.saveAll(historicList);
        response = new StandardResponse<>("Se guardo el archivo");
      } catch (Exception ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage());
        response = new StandardResponse<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    return response;
  }

  @Override
  public StandardResponse<Historic> saveHistoric(Historic historic) {
    Historic savedHistoric = historicRepository.save(historic);
    return new StandardResponse<>(savedHistoric);
  }

  @Override
  public StandardResponse<String> deleteAll() {
    String message;
    StandardResponse<String> response;
    try {
      historicRepository.deleteAll();
      message = "Exito";
      response = new StandardResponse<>(message);
    } catch (Exception ex) {
      message = "Error al eliminar los registros";
      System.out.println(ex.getMessage());
      ErrorResponse errorResponse = new ErrorResponse(message);
      response = new StandardResponse<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return response;
  }

  private Date stringToDatetime(String timeString, String fileName) throws ParseException {
    String dateString = fileName.substring(13, 17) + "-" + fileName.substring(17, 19) + "-";
    dateString += timeString + ":00";
    return TimeUtil.parseDate(dateString);
  }

}
