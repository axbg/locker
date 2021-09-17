package locker.service;

import locker.exception.AppException;

import java.io.File;

public interface OperationService {
    String operate(File source, File destination) throws AppException;
}
