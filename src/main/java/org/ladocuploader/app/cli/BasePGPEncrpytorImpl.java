package org.ladocuploader.app.cli;

import formflow.library.file.CloudFile;
import formflow.library.file.S3CloudFileRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.bcpg.CompressionAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.jcajce.JcaPGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.operator.KeyFingerPrintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

import java.io.*;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Iterator;

@Slf4j
@ComponentScan
@Profile({"production", "staging"})
public class BasePGPEncrpytorImpl {

    private String accessKey;
    private String secretKey;

    private String region;

    private PGPSecretKey signingKey;
    private PGPPublicKey pubKey;

    protected String pubkeyFilePath;
    protected String sigkeyPassword;
    protected String seckeyFilePath;
    protected String bucketName;

    public BasePGPEncrpytorImpl(String sigkeyPassword, String seckeyFilePath, String pubkeyFilePath, String bucketName, String accessKey, String secretKey, String region) {
        this.sigkeyPassword = sigkeyPassword;
        this.seckeyFilePath = seckeyFilePath;
        this.pubkeyFilePath = pubkeyFilePath;
        this.bucketName = bucketName;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.region = region;
    }

    @PostConstruct
    public void init() {
        log.info("Retrieving keys for signing and encryption");
        S3CloudFileRepository repository = new S3CloudFileRepository(accessKey, secretKey, bucketName, region);
        CloudFile pubKey = repository.get(pubkeyFilePath);
        CloudFile sigKey = repository.get(seckeyFilePath);
        try {
            initPubKey(pubKey.getFileBytes());
            initSigKey(sigKey.getFileBytes());
        } catch (IOException e) {
            throw new IllegalStateException("Issue initializing encryption keys", e);
        }
    }

    public byte[] signAndEncryptPayload(String filename) throws IOException {
        FileInputStream instream = new FileInputStream(filename);
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

    private void initPubKey(byte[] fileBytes) throws IOException {
        PGPPublicKey pubKey = null;
        InputStream inputStream = new ByteArrayInputStream(fileBytes);
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
        this.pubKey = pubKey;
    }

    private void initSigKey(byte[] sigkeyFileBytes) throws IOException {
        try (InputStream fileInputStream = new ByteArrayInputStream(sigkeyFileBytes);) {
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
                        signingKey = key;
                        return;
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

