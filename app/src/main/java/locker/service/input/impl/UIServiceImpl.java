package locker.service.input.impl;

import locker.event.OperationMode;
import locker.event.UIEvent;
import locker.exception.AppException;
import locker.object.Preference;
import locker.service.func.OperationService;
import locker.service.func.PasswordService;
import locker.service.func.PreferenceService;
import locker.service.input.UIService;
import locker.ui.MainFrame;
import org.springframework.stereotype.Service;

import java.io.File;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

@Service
public class UIServiceImpl implements UIService {
    private static final String SOURCE_FILE_MISSING = "Source file was not selected!";
    private static final String DESTINATION_FILE_MISSING = "Destination file was not selected";
    private static final String PREFERENCES_IMPORT_FINISHED = "The preferences were imported!";
    private static final String PREFERENCES_EXPORT_FINISHED = "The preferences were exported!";

    private final PreferenceService preferenceService;
    private final PasswordService passwordService;
    private final OperationService operationService;

    private MainFrame mainFrame;

    private File sourceFile;
    private File destinationFile;
    private OperationMode operationMode = OperationMode.ENCRYPT;
    private String password;

    public UIServiceImpl(PreferenceService preferenceService, PasswordService passwordService,
                         OperationService operationService) {
        this.preferenceService = preferenceService;
        this.passwordService = passwordService;
        this.operationService = operationService;
    }

    @Override
    public void loadUI(MainFrame frame) {
        this.mainFrame = frame;
        this.mainFrame.setEventHandler(this::handleUIEvent);
        this.mainFrame.setVisible(true);

        try {
            this.preferenceService.loadInitialPreferences();
            this.mainFrame.setPreferences(this.preferenceService.getPreferencesNames(), false);
        } catch (AppException ex) {
            this.mainFrame.displayMessagePrompt(ex.getMessage(), ERROR_MESSAGE);
        }
    }

    private void handleUIEvent(UIEvent event, Object... resource) {
        try {
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
                case GENERATE_PASSWORD:
                    String password = this.passwordService.generateStrongPassword();
                    this.mainFrame.setPassword(password);
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
                    this.preferenceService.importPreferences((String) resource[0], (File) resource[1], (String) resource[2]);
                    this.mainFrame.setPreferences(this.preferenceService.getPreferencesNames(), false);
                    this.mainFrame.displayMessagePrompt(PREFERENCES_IMPORT_FINISHED, INFORMATION_MESSAGE);
                    break;
                case EXPORT_PREFERENCES:
                    this.preferenceService.exportPreferences((String) resource[0], (File) resource[1]);
                    this.mainFrame.displayMessagePrompt(PREFERENCES_EXPORT_FINISHED, INFORMATION_MESSAGE);
                    break;
                case START:
                    this.password = (String) resource[0];
                    startOperation();
                    break;
                default:
                    break;
            }
        } catch (AppException ex) {
            this.mainFrame.displayMessagePrompt(ex.getMessage(), ERROR_MESSAGE);
        }
    }

    private void startOperation() {
        if (isInputValid()) {

            try {
                this.operationService.initCipher(password, operationMode);
                String result = this.operationService.operate(this.sourceFile, this.destinationFile);
                String[] parsedResult = result.split("/");
                this.mainFrame.displayMessagePrompt("Finished! Updated " + parsedResult[0] + " and removed " + parsedResult[1] + " additional files", INFORMATION_MESSAGE);
            } catch (AppException ex) {
                this.mainFrame.displayMessagePrompt(ex.getMessage(), ERROR_MESSAGE);
            }
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
