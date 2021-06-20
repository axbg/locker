package locker.service.impl;

import locker.event.OperationMode;
import locker.service.CryptoService;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class CryptoServiceImpl implements CryptoService {
    @Override
    public byte[] doContentOperation(OperationMode operationMode, File file, String password) {
        return new byte[0];
    }

    @Override
    public String doNameOperation(OperationMode operationMode, String name, String password) {
        return null;
    }
}
