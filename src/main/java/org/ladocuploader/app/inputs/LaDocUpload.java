package org.ladocuploader.app.inputs;

import formflow.library.data.FlowInputs;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ladocuploader.app.data.validators.Date;
import org.ladocuploader.app.data.validators.DateWithinRange;
import org.springframework.web.multipart.MultipartFile;

@Data
@EqualsAndHashCode(callSuper = false)
public class LaDocUpload extends FlowInputs {

  MultipartFile uploadDocuments;

  // Client Info Screen
  @NotBlank(message = "{client-info.provide-first-name}")
  private String firstName;

  @NotBlank(message = "{client-info.provide-last-name}")
  private String lastName;

  @DateWithinRange(message = "{client-info.date-range-validation}")
  @Date(message = "{client-info.date-format-validation}")
  private List<String> birthDate;

  @Email(message = "{client-info.provide-correct-email}")
  private String emailAddress;

  @Size(min = 14, max = 14, message = "{client-info.provide-10-digit-phone}")
  private String phoneNumber;

  @Pattern(regexp = "^[0-9]*$", message = "{client-info.number-format}")
  private String caseNumber;

  private String ssn;


}

