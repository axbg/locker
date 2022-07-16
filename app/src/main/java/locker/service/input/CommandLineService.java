package locker.service.input;

import locker.exception.AppException;

public interface CommandLineService {
    void process(String... args) throws AppException;
}
