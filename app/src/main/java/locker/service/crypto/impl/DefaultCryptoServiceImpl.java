package locker.service.crypto.impl;

import locker.exception.AppException;
import locker.service.crypto.AbstractCryptoService;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

@Service
public class DefaultCryptoServiceImpl extends AbstractCryptoService {
    private static final int SHA256_SIZE = 32;
    private static final int CBC_IV_LENGTH = 16;
    private static final String AES_CBC_PKCS5_ALG = "AES/CBC/PKCS5Padding";
    private static final String SHA_256_ALG = "SHA-256";
    private static final String AES_KEY_ALG = "AES";

    public DefaultCryptoServiceImpl() throws AppException {
        super();
    }

    @Override
    protected String getHashAlgorithm() {
        return SHA_256_ALG;
    }

    @Override
    protected String getCryptoAlgorithm() {
        return AES_CBC_PKCS5_ALG;
    }

    @Override
    protected int getIvLength() {
        return CBC_IV_LENGTH;
    }

    @Override
    protected void generateIv() {
        Arrays.fill(this.iv, (byte) 0x07);
    }

    @Override
    protected String getCryptoKeyAlgorithm() {
        return AES_KEY_ALG;
    }

    @Override
    protected AlgorithmParameterSpec getIvParameterSpec() {
        return new IvParameterSpec(this.iv);
    }

    @Override
    protected byte[] encrypt(byte[] content, String filename) throws AppException {
        try {
            byte[] fileHash = this.computeHash(content);
            byte[] combined = new byte[fileHash.length + content.length];

            System.arraycopy(fileHash, 0, combined, 0, fileHash.length);
            System.arraycopy(content, 0, combined, fileHash.length, content.length);

            return cipher.doFinal(combined);
        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            throw new AppException(filename != null
                    ? "File " + filename + " encryption failed: malformed"
                    : "Content encryption failed - malformed");
        }
    }

    @Override
    protected byte[] decrypt(byte[] content, String filename) throws AppException {
        try {
            byte[] decrypted = cipher.doFinal(content);

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
}
