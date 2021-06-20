package locker.service;

import java.io.File;

public interface FileService {
    File[] getFilesFromDestination(File source);

    boolean saveFile(File destination, String name, byte[] content);
}
