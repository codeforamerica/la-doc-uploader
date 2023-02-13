package org.formflowstartertemplate.app.inputs;

import formflow.library.data.FlowInputs;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.formflowstartertemplate.app.data.validators.DateWithinRange;
import org.springframework.web.multipart.MultipartFile;

@Data
@DateWithinRange
public class LaDocUpload extends FlowInputs {

  MultipartFile uploadDocuments;

  // Client Info Screen
  @NotBlank(message = "{client-info.provide-first-name}")
  private String firstName;

  @NotBlank(message = "{client-info.provide-last-name}")
  private String lastName;

  @Valid
  private String birthDateYear;


  @Valid
  private String birthDateMonth;

  @Valid
  private String birthDateDay;


}
