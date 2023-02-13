package org.formflowstartertemplate.app.inputs;

import formflow.library.data.FlowInputs;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import org.formflowstartertemplate.app.data.validators.DateWithinRange;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Data
@DateWithinRange
public class LaDocUpload extends FlowInputs {

  MultipartFile uploadDocuments;

  // Client Info Screen
  @NotBlank(message = "{client-info.provide-first-name}")
  private String firstName;

  @NotBlank(message = "{client-info.provide-last-name}")
  private String lastName;
  private String birthDateYear;

  private String birthDateMonth;
  private String birthDateDay;


}
