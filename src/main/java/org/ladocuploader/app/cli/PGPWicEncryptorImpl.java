package org.ladocuploader.app.cli;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component("wicPgpEncryptor")
@Profile({"production", "staging"})
public class PGPWicEncryptorImpl extends BasePGPEncrpytorImpl {

    public PGPWicEncryptorImpl(@Value("${pgp.wic.sigkey-password}") String sigkeyPassword,
                               @Value("${pgp.wic.seckey-file-path}") String seckeyFilePath,
                               @Value("${pgp.wic.pubkey-file-path}") String pubkeyFilePath,
                               @Value("${pgp.wic.bucket-name}") String bucketName) {
        super();
        this.sigkeyPassword = sigkeyPassword;
        this.seckeyFilePath = seckeyFilePath;
        this.pubkeyFilePath = pubkeyFilePath;
        this.bucketName = bucketName;
    }
}
