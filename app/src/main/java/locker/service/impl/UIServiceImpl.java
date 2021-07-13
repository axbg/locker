package locker.service.impl;

import locker.event.OperationMode;
import locker.event.UIEvent;
import locker.object.Preference;
import locker.service.CryptoService;
import locker.service.FileService;
import locker.service.PreferenceService;
import locker.service.UIService;
import locker.ui.MainFrame;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

@Service
public class UIServiceImpl implements UIService {
    private static final String SOURCE_FILE_MISSING = "Source file was not selected!";
    private static final String DESTINATION_FILE_MISSING = "Destination file was not selected";
    private static final String PREFERENCES_IMPORT_FINISHED = "The preferences were imported!";
    private static final String PREFERENCES_IMPORT_ERROR = "An error occurred during preferences importing!";
    private static final String PREFERENCES_EXPORT_FINISHED = "The preferences were exported!";
    private static final String PREFERENCES_EXPORT_ERROR = "An error occurred during preferences exporting!";
    private static final String ERROR_DURING_KEY_INIT = "Error during key initialization";
    private static final String ERROR_DURING_OPERATION = "Error occurred during operation on ";

    private final FileService fileService;
    private final CryptoService cryptoService;
    private final PreferenceService preferenceService;

    private MainFrame mainFrame;

    private File sourceFile;
    private File destinationFile;
    private OperationMode operationMode = OperationMode.ENCRYPT;
    private String password;

    public UIServiceImpl(FileService fileService, CryptoService cryptoService, PreferenceService preferenceService) {
        this.fileService = fileService;
        this.cryptoService = cryptoService;
        this.preferenceService = preferenceService;
    }

    @Override
    public void loadUI(MainFrame frame) {
        this.mainFrame = frame;
        this.mainFrame.setEventHandler(this::handleUIEvent);
        this.mainFrame.setPreferences(this.preferenceService.getPreferencesNames(), false);
        this.mainFrame.setVisible(true);
    }

    private void handleUIEvent(UIEvent event, Object... resource) {
        switch (event) {
            case SOURCE_FILE_SELECTED:
                this.sourceFile = (File) resource[0];
                break;
            case DESTINATION_FILE_SELECTED:
                this.destinationFile = (File) resource[0];
                break;
            case OPERATION_MODE_SELECTED:
                this.operationMode = (OperationMode) resource[0];
                break;
            case SAVE_PREFERENCE:
                this.password = (String) resource[0];
                this.preferenceService.savePreference(new Preference((String) resource[1], this.sourceFile.getAbsolutePath(),
                        this.destinationFile.getAbsolutePath(), this.password, this.operationMode));
                this.mainFrame.setPreferences(this.preferenceService.getPreferencesNames(), true);
                break;
            case LOAD_PREFERENCE:
                this.mainFrame.displayPreference(this.preferenceService.getPreference((String) resource[0]));
                break;
            case REMOVE_PREFERENCE:
                this.preferenceService.removePreference((String) resource[0]);
                this.mainFrame.setPreferences(this.preferenceService.getPreferencesNames(), false);
                break;
            case IMPORT_PREFERENCES:
                boolean importResult = this.preferenceService.importPreferences((String) resource[0], (File) resource[1], (String) resource[2]);

                if (importResult) {
                    this.mainFrame.setPreferences(this.preferenceService.getPreferencesNames(), false);
                }

                this.mainFrame.displayMessagePrompt(importResult ? PREFERENCES_IMPORT_FINISHED : PREFERENCES_IMPORT_ERROR, (importResult) ? 1 : 0);
                break;
            case EXPORT_PREFERENCES:
                boolean exportResult = this.preferenceService.exportPreferences((String) resource[0], (File) resource[1]);
                this.mainFrame.displayMessagePrompt(exportResult ? PREFERENCES_EXPORT_FINISHED : PREFERENCES_EXPORT_ERROR, (exportResult) ? 1 : 0);
                break;
            case START:
                this.password = (String) resource[0];
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
                this.mainFrame.displayMessagePrompt(ERROR_DURING_KEY_INIT, ERROR_MESSAGE);
                return;
            }

            for (File file : this.fileService.getFilesFromDestination(sourceFile)) {
                String name = this.cryptoService.doNameOperation(file.getName());
                currentFiles.add(name);

                if (!this.fileService.versionAlreadyExisting(file, destinationFile, name)) {
                    updated++;
                    byte[] content = this.cryptoService.doContentOperation(file);
                    if (!fileService.saveFile(destinationFile, name, content)) {
                        this.mainFrame.displayMessagePrompt(ERROR_DURING_OPERATION + file.getName(), ERROR_MESSAGE);
                        break;
                    }
                }
            }

            long removed = this.fileService.wipeAdditionalFiles(currentFiles, destinationFile);
            this.mainFrame.displayMessagePrompt("Finished! Updated " + updated + " and removed " + removed + " additional files", INFORMATION_MESSAGE);
        }
    }

    private boolean isInputValid() {
        if (this.sourceFile == null) {
            this.mainFrame.displayMessagePrompt(SOURCE_FILE_MISSING, ERROR_MESSAGE);
            return false;
        } else if (this.destinationFile == null) {
            this.mainFrame.displayMessagePrompt(DESTINATION_FILE_MISSING, ERROR_MESSAGE);
            return false;
        } else {
            return true;
        }
    }
}
