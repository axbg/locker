package locker.service;

import java.io.File;
import java.util.List;

public interface FileService {
    File[] getFilesFromDestination(File source);

    boolean saveFile(File destination, String name, byte[] content);

    boolean versionAlreadyExisting(File file, File destination, String newName);

    long wipeAdditionalFiles(List<String> files, File destination);
}
