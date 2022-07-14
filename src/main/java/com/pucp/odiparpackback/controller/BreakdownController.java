package com.pucp.odiparpackback.controller;

import com.pucp.odiparpackback.request.BreakdownRequest;
import com.pucp.odiparpackback.response.BreakdownResponse;
import com.pucp.odiparpackback.response.StandardResponse;
import com.pucp.odiparpackback.service.BreakdownService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/breakdown")
@RequiredArgsConstructor
public class BreakdownController {
  private final BreakdownService breakdownService;

  @PostMapping("/create")
  public ResponseEntity<StandardResponse<BreakdownResponse>> createBreakdown(@RequestBody BreakdownRequest request) {
    StandardResponse<BreakdownResponse> response = breakdownService.createBreakdown(request);
    return ResponseEntity.status(response.getStatus()).body(response);
  }
}
