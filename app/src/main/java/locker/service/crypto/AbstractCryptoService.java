package locker.service.crypto;

import locker.event.OperationMode;
import locker.exception.AppException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

public abstract class AbstractCryptoService implements CryptoService {
    protected final MessageDigest messageDigest;
    protected final Cipher cipher;

    protected byte[] iv;
    protected int opMode;
    protected String password;

    protected abstract String getHashAlgorithm();

    protected abstract String getCryptoAlgorithm();

    protected abstract int getIvLength();

    protected abstract void generateIv() throws AppException;

    protected abstract String getCryptoKeyAlgorithm();

    protected abstract AlgorithmParameterSpec getIvParameterSpec();

    protected abstract byte[] encrypt(byte[] content, String filename) throws AppException;

    protected abstract byte[] decrypt(byte[] content, String filename) throws AppException;

    public AbstractCryptoService() throws AppException {
        try {
            this.messageDigest = MessageDigest.getInstance(getHashAlgorithm());
            this.cipher = Cipher.getInstance(getCryptoAlgorithm());
            this.opMode = Cipher.ENCRYPT_MODE;
            this.iv = new byte[getIvLength()];

            generateIv();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new AppException("Error when instantiating the crypto algorithm");
        }
    }

    @Override
    public void initCipher(String password, OperationMode mode) throws AppException {
        this.password = password;
        this.opMode = mode == OperationMode.ENCRYPT ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;

        reinitializeCipher(true);
    }

    protected void reinitializeCipher(boolean generateIv) throws AppException {
        try {
            if (generateIv) {
                generateIv();
            }

            byte[] hashedPassword = messageDigest.digest(password.getBytes());

            SecretKeySpec secretKeySpec = new SecretKeySpec(hashedPassword, getCryptoKeyAlgorithm());
            this.cipher.init(opMode, secretKeySpec, getIvParameterSpec());
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | AppException e) {
            throw new AppException("An error occurred during cipher initialization");
        }
    }

    @Override
    public byte[] computeHash(byte[] content) {
        return messageDigest.digest(content);
    }

    @Override
    public byte[] doContentOperation(File file) throws AppException {
        try {
            return this.doContentOperation(Files.readAllBytes(file.toPath()), file.getName());
        } catch (IOException ex) {
            throw new AppException("An error occurred during the encryption process");
        }
    }

    @Override
    public byte[] doContentOperation(byte[] content, String filename) throws AppException {
        if (opMode == Cipher.ENCRYPT_MODE) {
            return this.encrypt(content, filename);
        } else {
            return this.decrypt(content, filename);
        }
    }

    @Override
    public String doNameOperation(String content) {
        try {
            return opMode == Cipher.ENCRYPT_MODE ? hexToString(cipher.doFinal(content.getBytes()))
                    : new String(this.cipher.doFinal(stringToHex(content)));
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            return null;
        }
    }

    private String hexToString(byte[] content) {
        StringBuilder builder = new StringBuilder();

        for (byte b : content) {
            builder.append(String.format("%02X", b));
        }

        return builder.toString();
    }

    private byte[] stringToHex(String content) {
        String[] hexBytes = content.replaceAll("..(?!$)", "$0 ").split(" ");
        byte[] result = new byte[hexBytes.length];

        for (int i = 0; i < hexBytes.length; i++) {
            result[i] = this.hexToByte(hexBytes[i]);
        }

        return result;
    }

    private byte hexToByte(String hex) {
        int first = Character.digit(hex.charAt(0), 16);
        int second = Character.digit(hex.charAt(1), 16);
        return (byte) ((first << 4) + second);
    }
}
