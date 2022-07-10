package com.pucp.odiparpackback.scheduled;

import com.pucp.odiparpackback.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTask {
  private static final Logger log = LogManager.getLogger(ScheduledTask.class);
  private final BusinessService businessService;


  @Scheduled(cron = "0 0 0/5 * * ?")
  public void replaning() {
    System.out.println("Scheduler de replaning");
    businessService.run();
  }
}
