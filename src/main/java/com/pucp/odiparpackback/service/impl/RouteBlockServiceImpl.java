package com.pucp.odiparpackback.service.impl;

import com.pucp.odiparpackback.model.Route;
import com.pucp.odiparpackback.model.RouteBlock;
import com.pucp.odiparpackback.repository.RouteBlockRepository;
import com.pucp.odiparpackback.repository.RouteRepository;
import com.pucp.odiparpackback.response.ErrorResponse;
import com.pucp.odiparpackback.response.StandardResponse;
import com.pucp.odiparpackback.service.RouteBlockService;
import com.pucp.odiparpackback.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class RouteBlockServiceImpl implements RouteBlockService {

  @Autowired
  private RouteBlockRepository routeBlockRepository;
  @Autowired
  private RouteRepository routeRepository;

  @Override
  public StandardResponse<List<RouteBlock>> listAll() {
    return null;
  }

  @Override
  public StandardResponse<String> saveFile(MultipartFile routeBlockFile) {
    StandardResponse<String> response;

    if (Objects.isNull(routeBlockFile)) {
      ErrorResponse error = new ErrorResponse(String.format(Message.NULL_FIELD, "file"));
      response = new StandardResponse<>(error, HttpStatus.BAD_REQUEST);
    } else {
      List<RouteBlock> historicList;
      try {
        InputStream is = routeBlockFile.getInputStream();
        String line, name = routeBlockFile.getOriginalFilename();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        historicList = new ArrayList<>();
        while ((line = br.readLine()) != null) {
          String[] splitLine = line.split(";");
          String startCity = splitLine[0].split(" => ")[0];
          String endCity = splitLine[0].split(" => ")[1];
          String[] restLine = splitLine[1].split("==");
          Date startDate = stringToDatetime(restLine[0]);
          Date endDate = stringToDatetime(restLine[1]);
          Route route = routeRepository.findRouteByFromCity_UbigeoAndToCity_Ubigeo(startCity, endCity);
          historicList.add(RouteBlock.builder().route(route).startDate(startDate).endDate(endDate).build());
        }

        routeBlockRepository.saveAll(historicList);
        response = new StandardResponse<>("Se guardo el archivo");
      } catch (Exception ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage());
        response = new StandardResponse<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    return response;
  }

  private Date stringToDatetime(String s) {
    int year = 2022;
    int month = Integer.parseInt(s.substring(0, 2));
    int day = Integer.parseInt(s.substring(2, 4));
    int hour = Integer.parseInt(s.substring(5, 7));
    int minute = Integer.parseInt(s.substring(8, 10));
    return new GregorianCalendar(year, month - 1, day, hour, minute, 0).getTime();
  }

  @Override
  public StandardResponse<RouteBlock> saveRouteBlock(RouteBlock routeBlock) {
    return null;
  }

  @Override
  public StandardResponse<String> deleteAll() {
    return null;
  }
}
