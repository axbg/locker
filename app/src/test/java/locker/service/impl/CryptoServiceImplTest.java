package locker.service.impl;

import locker.service.CryptoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

public class CryptoServiceImplTest {
    private static final String TEST_VALUE = "TestingCryptoServiceImpl";

    private CryptoService cryptoService;

    @BeforeEach
    public void setup() throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.cryptoService = new CryptoServiceImpl();
    }

    @Test
    public void computeHashTest() {
        String expected = "4cef6c9de7416d77e4d8e9d9fa01697e3239465aed07b6da9b08ac7b2a243e70";
        String hash = hexToString(this.cryptoService.computeHash(TEST_VALUE.getBytes()));

        Assertions.assertEquals(expected.toLowerCase(), hash.toLowerCase());
    }

    private String hexToString(byte[] content) {
        StringBuilder builder = new StringBuilder();

        for (byte b : content) {
            builder.append(String.format("%02X", b));
        }

        return builder.toString();
    }
}
