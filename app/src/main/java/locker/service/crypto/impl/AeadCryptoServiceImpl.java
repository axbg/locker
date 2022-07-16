package locker.service.crypto.impl;

import locker.exception.AppException;
import locker.service.crypto.AbstractCryptoService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.GCMParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

@Service
public class AeadCryptoServiceImpl extends AbstractCryptoService {
    private static final int SHA256_SIZE = 32;
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;
    private static final String AES_GCM_ALG = "AES/GCM/NoPadding";
    private static final String SHA_256_ALG = "SHA-256";
    private static final String AES_KEY_ALG = "AES";

    public AeadCryptoServiceImpl() throws AppException {
        super();
    }

    @Override
    protected String getHashAlgorithm() {
        return SHA_256_ALG;
    }

    @Override
    protected String getCryptoAlgorithm() {
        return AES_GCM_ALG;
    }

    @Override
    protected int getIvLength() {
        return GCM_IV_LENGTH;
    }

    @Override
    protected void generateIv() throws AppException {
        try {
            SecureRandom.getInstanceStrong().nextBytes(this.iv);
        } catch (NoSuchAlgorithmException e) {
            throw new AppException("Error while generating a unique IV");
        }
    }

    @Override
    protected String getCryptoKeyAlgorithm() {
        return AES_KEY_ALG;
    }

    @Override
    protected AlgorithmParameterSpec getIvParameterSpec() {
        return new GCMParameterSpec(GCM_TAG_LENGTH * 8, this.iv);
    }

    @Override
    protected byte[] encrypt(byte[] content, String filename) throws AppException {
        try {
            byte[] fileHash = this.computeHash(content);
            byte[] combined = new byte[fileHash.length + content.length];

            System.arraycopy(fileHash, 0, combined, 0, fileHash.length);
            System.arraycopy(content, 0, combined, fileHash.length, content.length);

            reinitializeCipher(true);
            byte[] encryptedContent = cipher.doFinal(combined);

            byte[] result = new byte[GCM_IV_LENGTH + encryptedContent.length];
            System.arraycopy(this.iv, 0, result, 0, GCM_IV_LENGTH);
            System.arraycopy(encryptedContent, 0, result, GCM_IV_LENGTH, encryptedContent.length);

            return result;
        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            throw new AppException(filename != null
                    ? "File " + filename + " encryption failed: malformed"
                    : "Content encryption failed - malformed");
        }
    }

    @Override
    protected byte[] decrypt(byte[] content, String filename) throws AppException {
        try {
            System.arraycopy(content, 0, this.iv, 0, GCM_IV_LENGTH);

            byte[] encrypted = new byte[content.length - GCM_IV_LENGTH];
            System.arraycopy(content, GCM_IV_LENGTH, encrypted, 0, content.length - GCM_IV_LENGTH);

            reinitializeCipher(false);
            byte[] decrypted = cipher.doFinal(encrypted);

            byte[] hash = new byte[SHA256_SIZE];
            System.arraycopy(decrypted, 0, hash, 0, SHA256_SIZE);

            content = new byte[decrypted.length - SHA256_SIZE];
            System.arraycopy(decrypted, SHA256_SIZE, content, 0, decrypted.length - SHA256_SIZE);

            byte[] computedHash = this.computeHash(content);
            if (!Arrays.equals(hash, computedHash)) {
                throw new AppException(filename != null
                        ? "File " + filename + " decryption failed: hashes mismatch"
                        : "Content decryption failed - hashes mismatch");
            }

            return content;
        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            throw new AppException(filename != null
                    ? "File " + filename + " decryption failed: malformed"
                    : "Content decryption failed - malformed");
        }
    }

    @Override
    public String doNameOperation(String content) {
        throw new NotImplementedException();
    }
}
