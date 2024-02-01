package org.ladocuploader.app.cli;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component("csvPgpEncryptor")
@Profile({"production", "staging"})
public class PGPCsvEncryptorImpl extends BasePGPEncrpytorImpl {

    public PGPCsvEncryptorImpl(@Value("${pgp.wic_ece.sigkey-password}") String sigkeyPassword,
                            @Value("${pgp.wic_ece.seckey-file-path}") String seckeyFilePath,
                            @Value("${pgp.wic_ece.pubkey-file-path}") String pubkeyFilePath,
                            @Value("${pgp.bucket-name}") String bucketName) {
        super();
        this.sigkeyPassword = sigkeyPassword;
        this.seckeyFilePath = seckeyFilePath;
        this.pubkeyFilePath = pubkeyFilePath;
        this.bucketName = bucketName;
    }
}
