package locker.service;

import locker.event.OperationMode;

import java.io.File;

public interface CryptoService {
    boolean initCipher(String password, OperationMode mode);

    byte[] doContentOperation(File file);

    String doNameOperation(String content);
}
