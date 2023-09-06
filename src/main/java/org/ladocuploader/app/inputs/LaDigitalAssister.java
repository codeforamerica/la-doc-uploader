package org.ladocuploader.app.inputs;

import formflow.library.data.FlowInputs;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class LaDigitalAssister extends FlowInputs {

  // Language
  private String languageRead;
  private String languageSpeak;
  private String needInterpreter;

  // Choose programs
  private List<String> programs;
  @NotBlank
  private String whosApplying;

  @NotBlank
  private String firstName;

  @NotBlank
  private String lastName;

  private String highestEducation;

  private String maritalStatus;

  private String sex;

  private String birthDate;

  private String phoneNumber;

  private String emailAddress;

}

