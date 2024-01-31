package org.ladocuploader.app.cli;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile({"production", "staging"})
public class PGPEncryptorImpl extends BasePGPEncrpytorImpl {

  @Value("${pgp.sigkey-password}")
  private String sigkeyPassword;
  @Value("${pgp.seckey-file-path}")
  private String seckeyFilePath;
  @Value("${pgp.pubkey-file-path}")
  private String pubkeyFilePath;
  @Value("${pgp.bucket-name}")
  private String bucketName;

}

