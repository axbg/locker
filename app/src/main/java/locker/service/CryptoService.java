package locker.service;

import locker.event.OperationMode;
import locker.exception.AppException;

import java.io.File;

public interface CryptoService {
    boolean initCipher(String password, OperationMode mode);

    byte[] computeHash(byte[] content);

    byte[] doContentOperation(File file) throws AppException;

    byte[] doContentOperation(byte[] content, String filename) throws AppException;

    String doNameOperation(String content);
}
