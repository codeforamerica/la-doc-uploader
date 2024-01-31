package org.ladocuploader.app.cli;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component("ecePgpEncryptor")
@Profile({"production", "staging"})
public class PGPEceEncryptorImpl extends BasePGPEncrpytorImpl {

    public PGPEceEncryptorImpl(@Value("${pgp.ece.sigkey-password}") String sigkeyPassword,
                               @Value("${pgp.ece.seckey-file-path}") String seckeyFilePath,
                               @Value("${pgp.ece.pubkey-file-path}") String pubkeyFilePath,
                               @Value("${pgp.ece.bucket-name}") String bucketName,
                               @Value("${form-flow.aws.access_key}") String accessKey,
                               @Value("${form-flow.aws.secret_key}") String secretKey,
                               @Value("${form-flow.aws.region}") String region) {
        super(sigkeyPassword, seckeyFilePath, pubkeyFilePath, bucketName, accessKey, secretKey, region);

    }
}
