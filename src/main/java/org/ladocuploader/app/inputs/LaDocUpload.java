package org.ladocuploader.app.inputs;

import formflow.library.data.FlowInputs;
import lombok.Data;

import javax.validation.constraints.*;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.web.multipart.MultipartFile;

@Data
public class LaDocUpload extends FlowInputs {

  MultipartFile uploadDocuments;

  // Client Info Screen
  @NotBlank(message = "{client-info.provide-first-name}")
  private String firstName;

  @NotBlank(message = "{client-info.provide-last-name}")
  private String lastName;

  @Email(message = "{client-info.provide-correct-email}")
  private String emailAddress;

  @Size(min=14, max=14, message="{client-info.provide-10-digit-phone}")
  private String phoneNumber;


  @Pattern(regexp = "^[0-9]*$", message="{client-info.number-format}")

  private String caseNumber;


}
