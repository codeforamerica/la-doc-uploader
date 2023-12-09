package org.ladocuploader.app.cli;

import org.bouncycastle.bcpg.BCPGOutputStream;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.jcajce.JcaPGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.operator.KeyFingerPrintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyKeyEncryptionMethodGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Iterator;

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

  // Move to its own class
  public byte[] signAndEncryptPayload(String filename) throws IOException {
    FileInputStream instream = new FileInputStream(filename);
    byte[] data = instream.readAllBytes();
    instream.close();

    PGPSecretKey signingKey = getPrivateKey();
    byte[] signedMessage = signMessage(signingKey, data);

    PGPPublicKey pubKey = getPublicKey();
    return encodeData(signedMessage, pubKey);
  }

  private static byte[] encodeData(byte[] data, PGPPublicKey pubKey) throws IOException {
    PGPEncryptedDataGenerator encGen = new PGPEncryptedDataGenerator(
        new JcePGPDataEncryptorBuilder(SymmetricKeyAlgorithmTags.AES_256)
            .setWithIntegrityPacket(true)
            .setSecureRandom(new SecureRandom()));
    encGen.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(pubKey));

    ByteArrayOutputStream encOut = new ByteArrayOutputStream();
    OutputStream cOut;
    try {
      cOut = encGen.open(encOut, new byte[4096]);
    } catch (PGPException e) {
      throw new IllegalStateException("Error creating data generator", e);
    }
    PGPLiteralDataGenerator lData = new PGPLiteralDataGenerator();
    OutputStream pOut = lData.open(cOut, PGPLiteralData.BINARY, PGPLiteralData.CONSOLE, data.length, new Date());
    pOut.write(data);

    pOut.close();
    cOut.close();

    byte[] result = encOut.toByteArray();
    encOut.close();
    return result;
  }

  private byte[] signMessage(PGPSecretKey pgpSec, byte[] data) throws IOException {
    PGPSignatureGenerator sGen;
    try {
      PGPPrivateKey pgpPrivKey = pgpSec.extractPrivateKey(
          new JcePBESecretKeyDecryptorBuilder().build(sigkeyPassword.toCharArray()));
      sGen = new PGPSignatureGenerator(
          new JcaPGPContentSignerBuilder(pgpSec.getPublicKey().getAlgorithm(), PGPUtil.SHA1));

      sGen.init(PGPSignature.BINARY_DOCUMENT, pgpPrivKey);
    } catch (PGPException e) {
      throw new IllegalStateException("Error creating signing generator", e);
    }

    Iterator<String> it = pgpSec.getPublicKey().getUserIDs();
    if (it.hasNext()) {
      PGPSignatureSubpacketGenerator spGen = new PGPSignatureSubpacketGenerator();
      spGen.addSignerUserID(false, it.next());
      sGen.setHashedSubpackets(spGen.generate());
    }

    ByteArrayOutputStream encOut = new ByteArrayOutputStream();
    try {
      PGPCompressedDataGenerator comData = new PGPCompressedDataGenerator(PGPCompressedData.ZLIB);
      BCPGOutputStream bOut = new BCPGOutputStream(comData.open(encOut));
      sGen.generateOnePassVersion(false).encode(bOut);

      PGPLiteralDataGenerator lGen = new PGPLiteralDataGenerator();
      OutputStream lOut = lGen.open(bOut, PGPLiteralData.BINARY, PGPLiteralData.CONSOLE, data.length, new Date());
      lOut.write(data);

      sGen.update(data);

      lOut.close();
      lGen.close();

      sGen.generate().encode(bOut);
      comData.close();
    } catch (PGPException e) {
      throw new IllegalStateException("Error signing message", e);
    }
    byte[] result = encOut.toByteArray();
    encOut.close();
    return result;
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

  private PGPSecretKey getPrivateKey() throws IOException {
    InputStream inputStream = new FileInputStream(sigkeyFilename);
    inputStream = PGPUtil.getDecoderStream(inputStream);
    PGPSecretKeyRingCollection pgpSec;

    KeyFingerPrintCalculator fpCalculator = new JcaKeyFingerprintCalculator();
    try {
      pgpSec = new PGPSecretKeyRingCollection(inputStream, fpCalculator);
    } catch (PGPException e) {
      throw new IllegalArgumentException("Invalid signing key");
    }

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

    throw new IllegalArgumentException("Invalid signing key");
  }
}
