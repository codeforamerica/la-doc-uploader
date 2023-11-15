package org.ladocuploader.app.inputs;

import formflow.library.data.FlowInputs;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ladocuploader.app.data.validators.Date;
import org.ladocuploader.app.data.validators.DateWithinRange;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class LaDocUpload extends FlowInputs {
  private String applicantId;

  MultipartFile uploadDocuments;

  // Client Info Screen
  @NotBlank(message = "{client-info.provide-first-name}")
  private String firstName;

  @NotBlank(message = "{client-info.provide-last-name}")
  private String lastName;

  @DateWithinRange(message = "{client-info.date-range-validation}")
  @Date(message = "{client-info.date-format-validation}")
  private List<String> birthDate;

  @Pattern(regexp = "^[\\w-\\.]+@[\\w-\\.]+\\.[\\w-]{2,4}$", message = "{client-info.provide-correct-email}")
  private String emailAddress;

  @Size(min = 14, max = 14, message = "{client-info.provide-10-digit-phone}")
  private String phoneNumber;

  @Pattern(regexp = "^[0-9]*$", message = "{client-info.number-format}")
  private String caseNumber;

  @Size(min=11, max=11, message="{client-info.provide-9-digit-ssn}")
  private String ssn;

  @NotBlank(message = "{final-confirmation.answer-feedback-question}")
  private String uploadDocumentsFeedback;

  private String uploadDocumentsFeedbackDetail;

  // This csrfField isn't use for anything and should be ignored.
  // It represents a CSRF used in a request.  We opted to include this here so that we
  // could pass validation.  Ideally we'd filter this out, like the library does for '_csrf' field
  // in the validation logic.  However, there's no easy way to inject this field into the "ignore" field list there.
  // Another way to get rid of it is via an Action, but we'd have to put that Action on EVERY POST
  // page, which is really annoying. So, for now, we've just included it here.
  private String csrfField;

}

