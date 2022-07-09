package com.pucp.odiparpackback.service.impl;

import com.pucp.odiparpackback.model.City;
import com.pucp.odiparpackback.model.RouteBlock;
import com.pucp.odiparpackback.repository.CityRepository;
import com.pucp.odiparpackback.repository.RouteBlockRepository;
import com.pucp.odiparpackback.repository.RouteRepository;
import com.pucp.odiparpackback.response.CityResponse;
import com.pucp.odiparpackback.response.ErrorResponse;
import com.pucp.odiparpackback.response.RouteBlockResponse;
import com.pucp.odiparpackback.response.StandardResponse;
import com.pucp.odiparpackback.service.RouteBlockService;
import com.pucp.odiparpackback.utils.Message;
import com.pucp.odiparpackback.utils.ObjectMapper;
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
import java.util.*;

import static com.pucp.odiparpackback.utils.TimeUtil.validateDates;

@Service
public class RouteBlockServiceImpl implements RouteBlockService {

  @Autowired
  private RouteBlockRepository routeBlockRepository;
  @Autowired
  private RouteRepository routeRepository;
  @Autowired
  private CityRepository cityRepository;
  @Autowired
  private ObjectMapper objectMapper;


  @Override
  public StandardResponse<List<RouteBlockResponse>> listAll() {
    StandardResponse<List<RouteBlockResponse>> response;
    List<RouteBlockResponse> list;

    try {
      List<RouteBlock> blockList = routeBlockRepository.findAll();
      list = new ArrayList<>();

      for (RouteBlock r : blockList) {
        CityResponse startCity = objectMapper.mapCity(r.getStartCity());
        CityResponse endCity = objectMapper.mapCity(r.getEndCity());


        RouteBlockResponse blockResponse = RouteBlockResponse.builder()
                .startCity(startCity)
                .endCity(endCity)
                .startDate(TimeUtil.formatDate(r.getStartDate()))
                .endDate(TimeUtil.formatDate(r.getEndDate()))
                .build();
        list.add(blockResponse);
      }
      response = new StandardResponse<>(list);
      return response;
    } catch (Exception ex) {
      ErrorResponse error = new ErrorResponse(ex.getMessage());
      response = new StandardResponse<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return response;
  }

  @Override
  public StandardResponse<String> saveFile(MultipartFile routeBlockFile) {
    StandardResponse<String> response;

    if (Objects.isNull(routeBlockFile)) {
      ErrorResponse error = new ErrorResponse(String.format(Message.NULL_FIELD, "file"));
      response = new StandardResponse<>(error, HttpStatus.BAD_REQUEST);
    } else {
      List<RouteBlock> blockList;
      try {
        InputStream is = routeBlockFile.getInputStream();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        blockList = new ArrayList<>();
        while ((line = br.readLine()) != null) {
          String[] splitLine = line.split(";");
          String[] ubigeoSplit = splitLine[0].split(" => ");
          String[] restLine = splitLine[1].split("==");
          Date startDate = stringToDatetime(restLine[0]);
          Date endDate = stringToDatetime(restLine[1]);
          City startCity = cityRepository.findByUbigeo(ubigeoSplit[0]);
          City endCity = cityRepository.findByUbigeo(ubigeoSplit[1]);
          blockList.add(RouteBlock.builder().startCity(startCity).endCity(endCity).startDate(startDate).endDate(endDate).build());
        }

        routeBlockRepository.saveAll(blockList);
        response = new StandardResponse<>("Se guardo el archivo");
      } catch (Exception ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage());
        response = new StandardResponse<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    return response;
  }

  private Date stringToDatetime(String s) throws ParseException {
    String dateString = "2022-" + s.substring(0, 2) + "-" + s.substring(2, 4) + " ";
    dateString += s.substring(5, 7) + ":" + s.substring(8, 10) + ":00";
    return TimeUtil.parseDate(dateString);
  }

  @Override
  public StandardResponse<RouteBlock> saveRouteBlock(RouteBlock routeBlock) {
    return null;
  }

  @Override
  public StandardResponse<List<RouteBlockResponse>> listByDate(String startDate, String endDate) {
    StandardResponse<List<RouteBlockResponse>> response;
    List<RouteBlockResponse> list;
    ErrorResponse errorResponse = validateDates(startDate);
    ErrorResponse errorResponse2 = validateDates(endDate);
    if (Objects.nonNull(errorResponse) || Objects.nonNull(errorResponse2)) {
      response = new StandardResponse<>(errorResponse, HttpStatus.BAD_REQUEST);
      return response;
    }

    try {
      Date start = TimeUtil.parseDate(startDate);
      Date end = TimeUtil.parseDate(endDate);
      List<RouteBlock> blockList = routeBlockRepository.findAllBetween(start, end);
      list = new ArrayList<>();

      for (RouteBlock r : blockList) {
        CityResponse startCity = objectMapper.mapCity(r.getStartCity());
        CityResponse endCity = objectMapper.mapCity(r.getEndCity());


        RouteBlockResponse blockResponse = RouteBlockResponse.builder()
                .startCity(startCity)
                .endCity(endCity)
                .startDate(TimeUtil.formatDate(r.getStartDate()))
                .endDate(TimeUtil.formatDate(r.getEndDate()))
                .build();
        list.add(blockResponse);
      }
      response = new StandardResponse<>(list);
      return response;
    } catch (ParseException ex) {
      errorResponse = new ErrorResponse(ex.getMessage());
      response = new StandardResponse<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    return response;
  }

  @Override
  public StandardResponse<String> deleteAll() {
    try {
      routeBlockRepository.deleteAll();
      return new StandardResponse<>("Exito");
    } catch (Exception ex) {
      ErrorResponse error = new ErrorResponse(ex.getMessage());
      return new StandardResponse<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
