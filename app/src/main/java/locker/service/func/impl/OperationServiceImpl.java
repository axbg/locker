package locker.service.func.impl;

import locker.event.OperationMode;
import locker.exception.AppException;
import locker.object.Preference;
import locker.service.func.FileService;
import locker.service.func.OperationService;
import locker.service.crypto.CryptoService;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final CryptoService aeadCryptoService;

    public OperationServiceImpl(FileService fileService, CryptoService cryptoService,
                                @Qualifier("AEAD") CryptoService aeadCryptoService) {
        this.fileService = fileService;
        this.cryptoService = cryptoService;
        this.aeadCryptoService = aeadCryptoService;
    }

    @Override
    public void initCipher(Preference preference) throws AppException {
        this.initCipher(preference.getPassword(), preference.getOperationMode());
    }

    public void initCipher(String password, OperationMode operationMode) throws AppException {
        this.cryptoService.initCipher(password, operationMode);
        this.aeadCryptoService.initCipher(password, operationMode);
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
                    byte[] content = this.aeadCryptoService.doContentOperation(file);
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
