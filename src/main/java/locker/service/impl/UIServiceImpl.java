package locker.service.impl;

import locker.event.OperationMode;
import locker.event.UIEvent;
import locker.service.CryptoService;
import locker.service.FileService;
import locker.service.UIService;
import locker.ui.Panel;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class UIServiceImpl implements UIService {
    private final static String SOURCE_FILE_MISSING = "Source file was not selected!";
    private final static String DESTINATION_FILE_MISSING = "Destination file was not selected";
    private final static String PANEL_TITLE = "Locker";

    private final Panel panel;
    private final FileService fileService;
    private final CryptoService cryptoService;

    private File sourceFile;
    private File destinationFile;
    private OperationMode operationMode = OperationMode.ENCRYPT;
    private String password;

    public UIServiceImpl(FileService fileService, CryptoService cryptoService) {
        this.panel = new Panel(PANEL_TITLE, this::handleUIEvent);
        this.fileService = fileService;
        this.cryptoService = cryptoService;
    }

    @Override
    public void loadUI() {
        this.panel.setVisible(true);
    }

    public void handleUIEvent(UIEvent event, Object resource) {
        switch (event) {
            case SOURCE_FILE_SELECTED:
                this.sourceFile = (File) resource;
                break;
            case DESTINATION_FILE_SELECTED:
                this.destinationFile = (File) resource;
                break;
            case OPERATION_MODE_SELECTED:
                this.operationMode = (OperationMode) resource;
                break;
            case START:
                this.password = (String) resource;
                startOperation();
            default:
                break;
        }
    }

    private void startOperation() {
        if (isInputValid()) {
            int updated = 0;
            List<String> currentFiles = new ArrayList<>();

            if (!this.cryptoService.initCipher(password, operationMode)) {
                this.panel.setStatus("Error during key initialization", Color.RED);
                return;
            }

            for (File file : this.fileService.getFilesFromDestination(sourceFile)) {
                this.panel.setStatus("Working on " + file.getName(), Color.BLACK);

                String name = this.cryptoService.doNameOperation(file.getName());
                currentFiles.add(name);

                if (!this.fileService.versionAlreadyExisting(file, destinationFile, name)) {
                    updated++;
                    byte[] content = this.cryptoService.doContentOperation(file);

                    if (!fileService.saveFile(destinationFile, name, content)) {
                        this.panel.setStatus("Error occurred during operation on " + file.getName(), Color.RED);
                        break;
                    }
                }
            }

            long removed = this.fileService.wipeAdditionalFiles(currentFiles, destinationFile);

            this.panel.setStatus("Finished! Updated " + updated + " and removed " + removed + " additional files", Color.BLACK);
        }
    }

    private boolean isInputValid() {
        if (this.sourceFile == null) {
            this.panel.setStatus(SOURCE_FILE_MISSING, Color.RED);
            return false;
        } else if (this.destinationFile == null) {
            this.panel.setStatus(DESTINATION_FILE_MISSING, Color.RED);
            return false;
        } else {
            this.panel.setStatus("", Color.BLACK);
            return true;
        }
    }
}
