package locker.service.impl;

import locker.exception.AppException;
import locker.service.CryptoService;
import locker.service.FileService;
import locker.service.OperationService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class OperationServiceImpl implements OperationService {
    private static final String ERROR_DURING_OPERATION = "Error occurred during operation on ";

    private final FileService fileService;
    private final CryptoService cryptoService;

    public OperationServiceImpl(FileService fileService, CryptoService cryptoService) {
        this.fileService = fileService;
        this.cryptoService = cryptoService;
    }

    @Override
    public String operate(File sourceFile, File destinationFile) throws AppException {
        int updated = 0;
        int removed = 0;

        List<String> currentFiles = new ArrayList<>();

        for (File file : this.fileService.getFilesFromDestination(sourceFile)) {
            if (file.isDirectory()) {
                File correspondingDirectory = new File(destinationFile.getAbsolutePath() + "/" + file.getName());
                currentFiles.add(correspondingDirectory.getName());

                if (!correspondingDirectory.exists()) {
                    try {
                        Files.createDirectory(correspondingDirectory.toPath());
                    } catch (IOException ex) {
                        throw new AppException(ERROR_DURING_OPERATION + file.getName());
                    }
                }

                String result = this.operate(file, correspondingDirectory);
                String[] parsedResult = result.split("/");

                updated += Integer.parseInt(parsedResult[0]);
                removed += Integer.parseInt(parsedResult[1]);
            } else {
                String name = this.cryptoService.doNameOperation(file.getName());
                currentFiles.add(name);

                if (!this.fileService.versionAlreadyExisting(file, destinationFile, name)) {
                    byte[] content = this.cryptoService.doContentOperation(file);
                    if (!fileService.saveFile(destinationFile, name, content)) {
                        throw new AppException(ERROR_DURING_OPERATION + file.getName());
                    }

                    updated++;
                }
            }
        }

        removed += this.fileService.wipeAdditionalFiles(currentFiles, destinationFile);
        return updated + "/" + removed;
    }
}
