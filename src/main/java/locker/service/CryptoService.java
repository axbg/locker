package locker.service;

import locker.event.OperationMode;

import java.io.File;

public interface CryptoService {
    byte[] doContentOperation(OperationMode operationMode, File file, String password);

    String doNameOperation(OperationMode operationMode, String name, String password);
}
