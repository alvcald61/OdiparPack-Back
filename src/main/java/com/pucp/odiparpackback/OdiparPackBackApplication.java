package com.pucp.odiparpackback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OdiparPackBackApplication {

  public static void main(String[] args) {
    SpringApplication.run(OdiparPackBackApplication.class, args);
  }

}
