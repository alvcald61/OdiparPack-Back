package com.pucp.odiparpackback.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import com.pucp.odiparpackback.model.Historic;
import com.pucp.odiparpackback.repository.HistoricRepository;
import com.pucp.odiparpackback.response.ErrorResponse;
import com.pucp.odiparpackback.response.HistoricGeneratorResponse;
import com.pucp.odiparpackback.response.StandardResponse;
import com.pucp.odiparpackback.service.HistoricService;
import com.pucp.odiparpackback.utils.Message;
import com.pucp.odiparpackback.utils.RegexPattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class HistoricServiceImpl implements HistoricService {
    @Autowired
    HistoricRepository historicRepository;


    @Override
    public StandardResponse<List<Historic>> listAll() {
        List<Historic> historicList = historicRepository.findAll();
        StandardResponse<List<Historic>> response = new StandardResponse<>(historicList);
        return response;
    }

    @Override
    public StandardResponse<List<HistoricGeneratorResponse>> listByDate(String startDate, String endDate) {
        StandardResponse<List<HistoricGeneratorResponse>> response;
        List<HistoricGeneratorResponse> generatedList;
        ErrorResponse errorResponse = validateDates(startDate, endDate);
        if (Objects.nonNull(errorResponse)) {
            response = new StandardResponse<>(errorResponse, HttpStatus.BAD_REQUEST);
            return response;
        }

        try {
            int i = 1;
            Date start = stringToDatetime(startDate);
            Date end = stringToDatetime(endDate);
            List<Historic> historicList = historicRepository.findAllByOrderDateBetween(start, end);

            generatedList = new ArrayList<>();
            for (Historic h : historicList) {
                HistoricGeneratorResponse hgr = HistoricGeneratorResponse.builder().orderId(h.getId()).clientId(h.getClientId())
                        .date(h.getOrderDate().toString()).startingNode(h.getStartingNode())
                        .destinationNode(h.getDestinationNode()).packages(h.getPackages()).build();
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
        Locale localePeru = new Locale("es", "pe");
        TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", localePeru);
        return formatDate.parse(dateString);
    }

    private Date stringToDatetime(String dateString) throws ParseException {
        Locale localePeru = new Locale("es", "pe");
        TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", localePeru);
        return formatDate.parse(dateString);
    }

    private ErrorResponse validateDates(String startDate, String endDate) {
        if (!startDate.matches(RegexPattern.DATE_FORMAT)) {
            return new ErrorResponse(String.format(Message.INCORRECT_FORMAT, "startDate"));
        }

        if (!endDate.matches(RegexPattern.DATE_FORMAT)) {
            return new ErrorResponse(String.format(Message.INCORRECT_FORMAT, "endDate"));
        }

        return null;
    }
}
