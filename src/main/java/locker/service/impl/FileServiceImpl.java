package locker.service.impl;

import locker.service.FileService;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public File[] getFilesFromDestination(File file) {
        return null;
    }

    @Override
    public boolean saveFile(String name, byte[] content) {
        return false;
    }
}
