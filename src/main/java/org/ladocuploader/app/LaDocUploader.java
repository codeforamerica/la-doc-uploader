package org.ladocuploader.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = {"org.ladocuploader.app", "formflow.library"})
@EnableConfigurationProperties
public class LaDocUploader {

  public static void main(String[] args) {

    SpringApplication.run(LaDocUploader.class, args);
  }

}
