package locker.service.impl;

import locker.event.OperationMode;
import locker.exception.AppException;
import locker.service.CryptoService;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Service
public class CryptoServiceImpl implements CryptoService {
    private static final int SHA256_SIZE = 32;

    private final byte[] iv;
    private final MessageDigest messageDigest;
    private final Cipher cipher;

    private int opMode;

    public CryptoServiceImpl() throws NoSuchAlgorithmException, NoSuchPaddingException {
        this.messageDigest = MessageDigest.getInstance("SHA-256");
        this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        this.opMode = Cipher.ENCRYPT_MODE;

        this.iv = new byte[16];
        Arrays.fill(this.iv, (byte) 0x01);
    }

    @Override
    public boolean initCipher(String password, OperationMode mode) {
        this.opMode = mode == OperationMode.ENCRYPT ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;

        byte[] hashedPassword = messageDigest.digest(password.getBytes());

        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(hashedPassword, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(this.iv);
            this.cipher.init(opMode, secretKeySpec, ivParameterSpec);
            return true;
        } catch (InvalidAlgorithmParameterException | InvalidKeyException e) {
            return false;
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
            return null;
        }
    }

    @Override
    public byte[] doContentOperation(byte[] content, String filename) throws AppException {
        try {
            if (opMode == Cipher.ENCRYPT_MODE) {
                byte[] fileHash = this.computeHash(content);
                byte[] combined = new byte[fileHash.length + content.length];

                System.arraycopy(fileHash, 0, combined, 0, fileHash.length);
                System.arraycopy(content, 0, combined, fileHash.length, content.length);

                return cipher.doFinal(combined);
            } else {
                byte[] decrypted = cipher.doFinal(content);

                byte[] hash = new byte[SHA256_SIZE];
                System.arraycopy(decrypted, 0, hash, 0, SHA256_SIZE);

                content = new byte[decrypted.length - SHA256_SIZE];
                System.arraycopy(decrypted, SHA256_SIZE, content, 0, decrypted.length - SHA256_SIZE);

                byte[] computedHash = this.computeHash(content);
                if (!Arrays.equals(hash, computedHash)) {
                    throw new AppException(filename != null ? "File " + filename + " decryption failed: hashes mismatch"
                            : "Content decryption failed - hashes mismatch");
                }

                return content;
            }

        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            throw new AppException(filename != null ? "File " + filename + " decryption failed: malformed"
                    : "Content decryption failed - malformed");
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
