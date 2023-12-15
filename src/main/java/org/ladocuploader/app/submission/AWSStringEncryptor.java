package org.ladocuploader.app.submission;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.EncryptRequest;
import com.google.crypto.tink.subtle.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Component
@Profile({"!test"})
public class AWSStringEncryptor implements StringEncryptor {
  private final AWSKMS kmsClient;

  private final String keyArn;

  public AWSStringEncryptor(@Value("${form-flow.aws.cmk}") String keyArn, @Value("${form-flow.aws.access_key}") String accessKey,
                            @Value("${form-flow.aws.secret_key}") String secretKey) {
    AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
    kmsClient = AWSKMSClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
        .withRegion("us-east-2")
        .build();
    this.keyArn = keyArn;
  }

  @Override
  public String decrypt(String ciphertext) {
    if (ciphertext == null || ciphertext.isEmpty()) {
      return "";
    }

    DecryptRequest decryptRequest = new DecryptRequest()
        .withEncryptionAlgorithm("RSAES_OAEP_SHA_256")
        .withKeyId(keyArn)
        .withCiphertextBlob(ByteBuffer.wrap(Base64.decode(ciphertext)));

    ByteBuffer plaintext = kmsClient.decrypt(decryptRequest).getPlaintext();
    return new String(plaintext.array());
  }

  @Override
  public String encrypt(String plaintext) {
    if (plaintext == null || plaintext.isEmpty()) {
      return "";
    }

    EncryptRequest req = new EncryptRequest()
        .withEncryptionAlgorithm("RSAES_OAEP_SHA_256")
        .withKeyId(keyArn)
        .withPlaintext(ByteBuffer.wrap(plaintext.getBytes()));

    ByteBuffer ciphertext = kmsClient.encrypt(req).getCiphertextBlob();
    return Base64.encode(ciphertext.array());
  }
}
