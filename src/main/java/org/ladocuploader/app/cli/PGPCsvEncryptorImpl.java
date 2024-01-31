package org.ladocuploader.app.cli;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile({"production", "staging"})
public class PGPCsvEncryptorImpl extends BasePGPEncrpytorImpl {

    public PGPCsvEncryptorImpl(@Value("${pgp.sigkey-password}") String sigkeyPassword,
                               @Value("${pgp.seckey-file-path}") String seckeyFilePath,
                               @Value("${pgp.pubkey-file-path}") String pubkeyFilePath,
                               @Value("${pgp.bucket-name}") String bucketName){
        super(sigkeyPassword, seckeyFilePath, pubkeyFilePath, bucketName);

    }

}
