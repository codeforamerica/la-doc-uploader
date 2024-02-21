package org.ladocuploader.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"org.ladocuploader.app", "formflow.library"})
@EntityScan(basePackages = {"org.ladocuploader.app", "formflow.library"})
@EnableConfigurationProperties
@EnableScheduling
public class LaDocUploader {

  public static void main(String[] args) {

    SpringApplication.run(LaDocUploader.class, args);
  }

}
