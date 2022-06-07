package com.pucp.odiparpackback.controller;

import com.pucp.odiparpackback.model.Historic;
import com.pucp.odiparpackback.request.HistoricListRequest;
import com.pucp.odiparpackback.response.HistoricGeneratorResponse;
import com.pucp.odiparpackback.response.StandardResponse;
import com.pucp.odiparpackback.service.HistoricService;
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
@RequestMapping("/historic")
public class HistoricController {
    @Autowired
    private HistoricService historicService;

    @GetMapping("/list")
    public ResponseEntity<StandardResponse<List<Historic>>> listAll() {
        StandardResponse<List<Historic>> response = historicService.listAll();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/generateByDate")
    public ResponseEntity<StandardResponse<List<HistoricGeneratorResponse>>> listByDate(@RequestBody HistoricListRequest request) {
        StandardResponse<List<HistoricGeneratorResponse>> response = historicService.listByDate(request.getStartDate(), request.getEndDate());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/saveFile")
    public ResponseEntity<StandardResponse<String>> saveFile(@RequestPart("file") MultipartFile historicFile) {
        StandardResponse<String> response = historicService.saveFile(historicFile);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/save")
    public ResponseEntity<StandardResponse<Historic>> saveHistoric(Historic historic) {
        StandardResponse<Historic> response = historicService.saveHistoric(historic);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<StandardResponse<String>> deleteAll() {
        StandardResponse<String> response = historicService.deleteAll();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
