package org.ladocuploader.app.submission;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.EncryptRequest;
import com.google.crypto.tink.subtle.Hex;

import java.nio.ByteBuffer;

public class StringEncryptor {
  private final AWSKMS kmsClient;

  private final String keyArn;

  public StringEncryptor(String keyArn, String accessKey, String secretKey) {
    AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
    kmsClient = AWSKMSClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
        .withRegion("us-east-2")
        .build();
    this.keyArn = keyArn;
  }

  public String decrypt(String ciphertext) {
    DecryptRequest decryptRequest = new DecryptRequest()
        .withEncryptionAlgorithm("RSAES_OAEP_SHA_256")
        .withKeyId(keyArn)
        .withCiphertextBlob(ByteBuffer.wrap(Hex.decode(ciphertext)));

    ByteBuffer plaintext = kmsClient.decrypt(decryptRequest).getPlaintext();
    return new String(plaintext.array());
  }

  public String encrypt(String plaintext) {
    EncryptRequest req = new EncryptRequest()
        .withEncryptionAlgorithm("RSAES_OAEP_SHA_256")
        .withKeyId(keyArn)
        .withPlaintext(ByteBuffer.wrap(plaintext.getBytes()));

    ByteBuffer ciphertext = kmsClient.encrypt(req).getCiphertextBlob();
    return Hex.encode(ciphertext.array());
  }
}
