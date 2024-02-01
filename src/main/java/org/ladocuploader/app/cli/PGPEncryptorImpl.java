package org.ladocuploader.app.cli;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component("snapPgpEncryptor")
@Profile({"production", "staging"})
public class PGPEncryptorImpl extends BasePGPEncrpytorImpl {

  public PGPEncryptorImpl(@Value("${pgp.snap.sigkey-password}") String sigkeyPassword,
                          @Value("${pgp.snap.seckey-file-path}") String seckeyFilePath,
                          @Value("${pgp.snap.pubkey-file-path}") String pubkeyFilePath,
                          @Value("${pgp.bucket-name}") String bucketName) {
    super();
    this.sigkeyPassword = sigkeyPassword;
    this.seckeyFilePath = seckeyFilePath;
    this.pubkeyFilePath = pubkeyFilePath;
    this.bucketName = bucketName;

  }
}

