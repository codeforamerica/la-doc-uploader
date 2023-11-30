package org.ladocuploader.app.cli;

import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.jcajce.JcaPGPPublicKeyRingCollection;
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

  public PGPEncryptor(@Value("${pgp.pubkey_filename:}") String pubkeyFilename) {
    this.pubkeyFilename = pubkeyFilename;
  }

  // Move to its own class
  public byte[] encryptPayload(String filename) throws IOException {
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

    PGPEncryptedDataGenerator encGen = new PGPEncryptedDataGenerator(
        new JcePGPDataEncryptorBuilder(SymmetricKeyAlgorithmTags.AES_256)
            .setWithIntegrityPacket(true)
            .setSecureRandom(new SecureRandom()));
    encGen.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(pubKey));

    FileInputStream instream = new FileInputStream(filename);
    byte[] data = instream.readAllBytes();
    instream.close();

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

    return encOut.toByteArray();
  }
}
