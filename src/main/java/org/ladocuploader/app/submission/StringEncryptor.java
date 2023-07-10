package org.ladocuploader.app.submission;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.kms.KmsMasterKeyProvider;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.DecryptResult;
import com.amazonaws.services.kms.model.EncryptRequest;
import com.amazonaws.services.kms.model.EncryptResult;
import com.google.crypto.tink.subtle.Hex;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Cipher;
import java.nio.ByteBuffer;

public class StringEncryptor {

  final AwsCrypto crypto;
  final KmsMasterKeyProvider keyProvider;
  Cipher desCipher;

  AWSKMS kmsClient;

  String keyArn;

  public StringEncryptor(String keyArn, @Value("${form-flow.aws.access_key}") String accessKey,
                         @Value("${form-flow.aws.secret_key}") String secretKey) {
    // 1. Instantiate the SDK
    // This builds the AwsCrypto client with the RequireEncryptRequireDecrypt commitment policy,
    // which enforces that this client only encrypts using committing algorithm suites and enforces
    // that this client will only decrypt encrypted messages that were created with a committing algorithm suite.
    // This is the default commitment policy if you build the client with `AwsCrypto.builder().build()`
    // or `AwsCrypto.standard()`.
    crypto = AwsCrypto.standard();
    // 2. Instantiate an AWS KMS master key provider in strict mode using buildStrict().
    // In strict mode, the AWS KMS master key provider encrypts and decrypts only by using the key
    // indicated by keyArn.
    // To encrypt and decrypt with this master key provider, use an &KMS; key ARN to identify the &KMS; keys.
    // In strict mode, the decrypt operation requires a key ARN.
    keyProvider = KmsMasterKeyProvider.builder()
        .withCredentials(new BasicAWSCredentials(accessKey, secretKey))
        .buildStrict(keyArn);
    // 3. Create an encryption context
    // Most encrypted data should have an associated encryption context
    // to protect integrity. This sample uses placeholder values.
    // For more information see:
    // blogs.aws.amazon.com/security/post/Tx2LZ6WBJJANTNW/How-to-Protect-the-Integrity-of-Your-Encrypted-Data-by-Using-AWS-Key-Management

    AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
    kmsClient = AWSKMSClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
        .withRegion("us-east-2")
        .build();
    this.keyArn = keyArn;
  }

  public String decrypt(String ciphertext) {
    // 5. Decrypt the data
    DecryptRequest decryptRequest = new DecryptRequest()
        .withEncryptionAlgorithm("RSAES_OAEP_SHA_256")
        .withKeyId(keyArn)
        .withCiphertextBlob(ByteBuffer.wrap(Hex.decode(ciphertext)));

    DecryptResult result = this.kmsClient.decrypt(decryptRequest);
    return new String(result.getPlaintext().array());
  }

  public String encrypt(String plaintext) {
    // 4. Encrypt the data
    EncryptRequest req = new EncryptRequest()
        .withEncryptionAlgorithm("RSAES_OAEP_SHA_256")
        .withKeyId(keyArn)
        .withPlaintext(ByteBuffer.wrap(plaintext.getBytes()));
    EncryptResult result = kmsClient.encrypt(req);
    ByteBuffer ciphertext = result.getCiphertextBlob();
    return Hex.encode(ciphertext.array());
  }
}
