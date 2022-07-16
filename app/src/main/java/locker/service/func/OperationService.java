package locker.service.func;

import locker.event.OperationMode;
import locker.exception.AppException;
import locker.object.Preference;

import java.io.File;

public interface OperationService {
    void initCipher(Preference preference) throws AppException;
    void initCipher(String password, OperationMode operationMode) throws AppException;
    String operate(File source, File destination) throws AppException;
}
