package com.pucp.odiparpackback.service;

import com.pucp.odiparpackback.model.Historic;
import com.pucp.odiparpackback.response.HistoricGeneratorResponse;
import com.pucp.odiparpackback.response.StandardResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HistoricService {
    StandardResponse<List<Historic>> listAll();

    StandardResponse<List<HistoricGeneratorResponse>> listByDate(String startDate, String endDate);

    StandardResponse<String> saveFile(MultipartFile historicFile);

    StandardResponse<Historic> saveHistoric(Historic historic);

    StandardResponse<String> deleteAll();

}
