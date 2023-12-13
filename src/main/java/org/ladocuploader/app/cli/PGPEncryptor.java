package org.ladocuploader.app.cli;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.bcpg.CompressionAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.jcajce.JcaPGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.operator.KeyFingerPrintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Iterator;

@Slf4j
@Component
public class PGPEncryptor {

  private final String pubkeyFilename;
  private final String sigkeyFilename;
  private final String sigkeyPassword;

  public PGPEncryptor(@Value("${pgp.pubkey_filename:}") String pubkeyFilename, @Value("${pgp.sigkey_filename:}") String sigkeyFilename, @Value("${pgp.sigkey_password:}") String sigkeyPassword) {
    this.pubkeyFilename = pubkeyFilename;
    this.sigkeyFilename = sigkeyFilename;
    this.sigkeyPassword = sigkeyPassword;
  }

  public byte[] signAndEncryptPayload(String filename) throws IOException {
    FileInputStream instream = new FileInputStream(filename);

    log.info("Retrieving keys for signing and encryption");
    PGPSecretKey signingKey = getSecretKey();
    PGPPublicKey pubKey = getPublicKey();

    ByteArrayOutputStream outstream = new ByteArrayOutputStream();
    try {
      log.info("Signing and encrypting payload");
      return signAndEncryptPayload(instream, signingKey, pubKey, outstream);
    } catch (PGPException e) {
      throw new IllegalStateException("There was an issue signing and encrypting the file", e);
    } finally {
      instream.close();
      outstream.close();
      log.info("Completed signing and encrypting payload");
    }
  }

  private PGPPublicKey getPublicKey() throws IOException {
    PGPPublicKey pubKey = null;
    InputStream inputStream = new FileInputStream(pubkeyFilename);
    inputStream = PGPUtil.getDecoderStream(inputStream);
    try {
      JcaPGPPublicKeyRingCollection ringCollection = new JcaPGPPublicKeyRingCollection(inputStream);
      Iterator<PGPPublicKeyRing> keyRingsIterator = ringCollection.getKeyRings();
      while (keyRingsIterator.hasNext()) {
        PGPPublicKeyRing pgpPublicKeyRing = keyRingsIterator.next();
        Iterator<PGPPublicKey> pubKeysIterator = pgpPublicKeyRing.getPublicKeys();
        while (pubKeysIterator.hasNext()) {
          pubKey = pubKeysIterator.next();
        }
      }
    } catch (PGPException e) {
      throw new IllegalArgumentException("Invalid public key");
    } finally {
      inputStream.close();
    }
    return pubKey;
  }

  private PGPSecretKey getSecretKey() throws IOException {
    try (InputStream fileInputStream = new FileInputStream(sigkeyFilename);) {
      InputStream inputStream = PGPUtil.getDecoderStream(fileInputStream);
      KeyFingerPrintCalculator fpCalculator = new JcaKeyFingerprintCalculator();
      PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(inputStream, fpCalculator);

      Iterator<PGPSecretKeyRing> keyRingIter = pgpSec.getKeyRings();
      while (keyRingIter.hasNext()) {
        PGPSecretKeyRing keyRing = keyRingIter.next();

        Iterator<PGPSecretKey> keyIter = keyRing.getSecretKeys();
        while (keyIter.hasNext()) {
          PGPSecretKey key = keyIter.next();

          if (key.isSigningKey()) {
            return key;
          }
        }
      }
    } catch (PGPException e) {
      throw new IllegalArgumentException("Invalid signing key", e);
    }

    throw new IllegalArgumentException("Invalid signing key");
  }

  private PGPPrivateKey getPrivateKey(PGPSecretKey secretKey) throws PGPException {
    return secretKey.extractPrivateKey(
        new JcePBESecretKeyDecryptorBuilder().build(sigkeyPassword.toCharArray()));
  }

  private byte[] signAndEncryptPayload(InputStream inputStream, PGPSecretKey secKey, PGPPublicKey pubKey,
                                      ByteArrayOutputStream outputStream) throws PGPException, IOException {
    int BUFFER_SIZE = 1 << 16;

    // Encryption
    PGPEncryptedDataGenerator encGen = new PGPEncryptedDataGenerator(
        new JcePGPDataEncryptorBuilder(SymmetricKeyAlgorithmTags.AES_256)
            .setWithIntegrityPacket(true)
            .setSecureRandom(new SecureRandom()));
    encGen.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(pubKey));
    OutputStream encOut = encGen.open(outputStream, new byte[BUFFER_SIZE]);

    // Compression
    PGPCompressedDataGenerator cGen = new PGPCompressedDataGenerator(CompressionAlgorithmTags.ZIP);
    OutputStream cOut = cGen.open(encOut);

    // Signing
    PGPSignatureGenerator sGen = new PGPSignatureGenerator(new JcaPGPContentSignerBuilder(secKey.getPublicKey().getAlgorithm(), PGPUtil.SHA1));
    sGen.init(PGPSignature.BINARY_DOCUMENT, getPrivateKey(secKey));

    Iterator<String> it = secKey.getPublicKey().getUserIDs();
    if (it.hasNext()) {
      PGPSignatureSubpacketGenerator spGen = new PGPSignatureSubpacketGenerator();
      spGen.addSignerUserID(false, it.next());
      sGen.setHashedSubpackets(spGen.generate());
    }

    sGen.generateOnePassVersion(false).encode(cOut);

    // Literal Data generator and output stream
    byte[] data = inputStream.readAllBytes();
    PGPLiteralDataGenerator lGen = new PGPLiteralDataGenerator();
    OutputStream lOut = lGen.open(cOut, PGPLiteralData.BINARY, PGPLiteralData.CONSOLE, data.length, new Date());

    lOut.write(data);
    sGen.update(data);

    lOut.close();
    lGen.close();

    sGen.generate().encode(cOut);
    cOut.close();
    cGen.close();
    encGen.close();

    return outputStream.toByteArray();
  }

}

