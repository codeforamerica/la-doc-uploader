package org.formflowstartertemplate.app.inputs;

import formflow.library.data.FlowInputs;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

@Data
public class LaDocUpload extends FlowInputs {

  MultipartFile docUpload;

  // Client Info Screen
  @NotBlank(message = "{client-info.provide-first-name}")
  private String firstName;

  @NotBlank(message = "{client-info.provide-last-name}")
  private String lastName;
}
