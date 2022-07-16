package locker.service.crypto;

import locker.event.OperationMode;
import locker.exception.AppException;

import java.io.File;

public interface CryptoService {
    void initCipher(String password, OperationMode mode) throws AppException;
    byte[] computeHash(byte[] content);
    byte[] doContentOperation(File file) throws AppException;
    byte[] doContentOperation(byte[] content, String filename) throws AppException;
    String doNameOperation(String content);
}
