package com.pucp.odiparpackback.controller;

import com.pucp.odiparpackback.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/business")
@RequiredArgsConstructor
public class BusinessController {
  private final BusinessService businessService;

  @GetMapping
  public void startService() {
    businessService.run();
  }
}
