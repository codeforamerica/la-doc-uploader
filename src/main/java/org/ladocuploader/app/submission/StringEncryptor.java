package org.ladocuploader.app.submission;

import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.CommitmentPolicy;
import com.amazonaws.encryptionsdk.CryptoResult;
import com.amazonaws.encryptionsdk.kms.KmsMasterKey;
import com.amazonaws.encryptionsdk.kms.KmsMasterKeyProvider;

import javax.crypto.Cipher;
import java.util.Collections;
import java.util.Map;

public class StringEncryptor {

  final AwsCrypto crypto;
  final KmsMasterKeyProvider keyProvider;
  final Map<String, String> encryptionContext;
  Cipher desCipher;

  public StringEncryptor(String keyArn) {
    // 1. Instantiate the SDK
    // This builds the AwsCrypto client with the RequireEncryptRequireDecrypt commitment policy,
    // which enforces that this client only encrypts using committing algorithm suites and enforces
    // that this client will only decrypt encrypted messages that were created with a committing algorithm suite.
    // This is the default commitment policy if you build the client with `AwsCrypto.builder().build()`
    // or `AwsCrypto.standard()`.
    crypto = AwsCrypto.builder()
        .withCommitmentPolicy(CommitmentPolicy.RequireEncryptRequireDecrypt)
        .build();
    // 2. Instantiate an AWS KMS master key provider in strict mode using buildStrict().
    // In strict mode, the AWS KMS master key provider encrypts and decrypts only by using the key
    // indicated by keyArn.
    // To encrypt and decrypt with this master key provider, use an &KMS; key ARN to identify the &KMS; keys.
    // In strict mode, the decrypt operation requires a key ARN.
    keyProvider = KmsMasterKeyProvider.builder().buildStrict(keyArn);
    encryptionContext = Collections.singletonMap("ExampleContextKey", "ExampleContextValue");
  }

  public String decrypt(String ciphertext) {
    // 5. Decrypt the data
    final CryptoResult<byte[], KmsMasterKey> decryptResult = crypto.decryptData(keyProvider, ciphertext.getBytes());
    // 6. Verify that the encryption context in the result contains the
    // encryption context supplied to the encryptData method. Because the
    // SDK can add values to the encryption context, don't require that
    // the entire context matches.
    if (!encryptionContext.entrySet().stream()
        .allMatch(
            e -> e.getValue().equals(decryptResult.getEncryptionContext().get(e.getKey())))) {
      throw new IllegalStateException("Wrong Encryption Context!");
    }
    return new String(decryptResult.getResult());
  }

  public String encrypt(String plaintext) {
    // 3. Create an encryption context
    // Most encrypted data should have an associated encryption context
    // to protect integrity. This sample uses placeholder values.
    // For more information see:
    // blogs.aws.amazon.com/security/post/Tx2LZ6WBJJANTNW/How-to-Protect-the-Integrity-of-Your-Encrypted-Data-by-Using-AWS-Key-Management
    // 4. Encrypt the data
    final CryptoResult<byte[], KmsMasterKey> encryptResult = crypto.encryptData(keyProvider, plaintext.getBytes(), encryptionContext);
    return new String(encryptResult.getResult());
  }
}
