package locker.service;

import locker.exception.AppException;

public interface CommandLineService {
    void process(String... args) throws AppException;
}
