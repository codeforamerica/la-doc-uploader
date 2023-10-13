package org.ladocuploader.app.submission.actions;

import formflow.library.data.Submission;
import org.ladocuploader.app.submission.StringEncryptor;

/**
 *
 */
@org.springframework.stereotype.Component
@lombok.extern.slf4j.Slf4j
public class ClientInfoBeforeDisplay implements formflow.library.config.submission.Action {

  private final StringEncryptor encryptor;

  public ClientInfoBeforeDisplay(StringEncryptor stringEncryptor) {
    encryptor = stringEncryptor;
  }

  @Override
  public void run(Submission submission) {
    new DecryptSSNBeforeDisplaying(encryptor).run(submission);
    new SetApplicantId().run(submission);
  }
}
