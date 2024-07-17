package org.ladocuploader.app.cli;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component("eceOrleansPgpEncryptor")
@Profile({"production", "staging"})
public class PGPEceOrleansEncryptorImpl extends BasePGPEncrpytorImpl {

    public PGPEceOrleansEncryptorImpl(@Value("${pgp.ece.orleans.sigkey-password}") String sigkeyPassword,
                                      @Value("${pgp.ece.orleans.seckey-file-path}") String seckeyFilePath,
                                      @Value("${pgp.ece.orleans.pubkey-file-path}") String pubkeyFilePath,
                                      @Value("${pgp.ece.orleans.bucket-name}") String bucketName,
                                      @Value("${form-flow.aws.access_key}") String accessKey,
                                      @Value("${form-flow.aws.secret_key}") String secretKey,
                                      @Value("${form-flow.aws.region}") String region) {
        super(sigkeyPassword, seckeyFilePath, pubkeyFilePath, bucketName, accessKey, secretKey, region);

    }
}
