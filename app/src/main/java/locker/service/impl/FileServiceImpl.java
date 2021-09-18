package locker.service.impl;

import locker.service.FileService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class FileServiceImpl implements FileService {
    private static final List<String> IGNORED_FILES = Collections.singletonList(".DS_STORE");

    @Override
    public File[] getFilesFromDestination(File file) {
        return file.isDirectory() ? filterIgnoredFiles(file.listFiles()) : new File[]{file};
    }

    @Override
    public boolean saveFile(File destination, String name, byte[] content) {
        File file = new File(destination.getAbsolutePath() + "/" + name);

        try {
            Files.write(file.toPath(), content);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    @Override
    public boolean versionAlreadyExisting(File sourceFile, File destination, String newFileName) {
        return Arrays.stream(Objects.requireNonNull(destination.listFiles()))
                .filter(file -> file.getName().equals(newFileName))
                .anyMatch(file -> file.lastModified() > sourceFile.lastModified());
    }

    @Override
    public long wipeAdditionalFiles(List<String> files, File destination) {
        return Arrays.stream(Objects.requireNonNull(destination.listFiles()))
                .filter(file -> !files.contains(file.getName()))
                .map(this::wipeFile)
                .reduce(0, Integer::sum);
    }

    private File[] filterIgnoredFiles(File[] files) {
        return Arrays.stream(files)
                .filter(file -> !IGNORED_FILES.contains(file.getName().toUpperCase()))
                .toArray(File[]::new);
    }

    private int wipeFile(File file) {
        int removed = 0;

        if (file.isDirectory()) {
            for (File innerFile : Objects.requireNonNull(file.listFiles())) {
                removed += this.wipeFile(innerFile);
            }

            //noinspection ResultOfMethodCallIgnored
            file.delete();
        } else {
            removed = file.delete() ? 1 : 0;
        }

        return removed;
    }
}
