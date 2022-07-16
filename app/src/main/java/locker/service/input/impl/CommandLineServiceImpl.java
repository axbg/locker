package locker.service.input.impl;

import locker.event.OperationMode;
import locker.exception.AppException;
import locker.object.CommandLineObject;
import locker.object.Preference;
import locker.service.input.CommandLineService;
import locker.service.func.OperationService;
import locker.service.func.PasswordService;
import locker.service.func.PreferenceService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class CommandLineServiceImpl implements CommandLineService {
    private static final String LIST_COMMAND = "list";
    private static final String EXECUTE_COMMAND = "execute";
    private static final String PREFERENCE_COMMAND = "preference";
    private static final String REMOVE_COMMAND = "remove";

    private final PreferenceService preferenceService;
    private final PasswordService passwordService;
    private final OperationService operationService;

    CommandLineServiceImpl(PreferenceService preferenceService, PasswordService passwordService, OperationService operationService) {
        this.preferenceService = preferenceService;
        this.passwordService = passwordService;
        this.operationService = operationService;
    }

    @Override
    public void process(String... args) throws AppException {
        this.preferenceService.loadInitialPreferences();
        CommandLineObject clo = this.parseArgs(args);

        switch (clo.getCommand()) {
            case PREFERENCE_COMMAND:
                this.savePreference(clo);
                break;
            case EXECUTE_COMMAND:
                this.doOperation(clo);
                break;
            case LIST_COMMAND:
                this.listPreference(clo);
                break;
            case REMOVE_COMMAND:
                this.removePreference(clo);
                break;
        }
    }

    private CommandLineObject parseArgs(String... args) {
        if (args.length < 1 || args.length > 5 || args.length == 3) {
            throw new IllegalArgumentException(
                    "1, 2, 4 or 5 arguments are required: preference_name/'list' source/preference_name, destination, operation, preference_name. " +
                            "Only " + args.length + " were passed");
        }

        CommandLineObject clo = new CommandLineObject();

        switch (args[0]) {
            case LIST_COMMAND:
                clo.setCommand(LIST_COMMAND);

                if (args.length == 2) {
                    Preference preference = parsePreference(args[1]);

                    if (preference == null) {
                        throw new IllegalArgumentException("Requested preference was not found");
                    }

                    clo.setPreference(parsePreference(args[1]));
                }
                break;
            case REMOVE_COMMAND:
                clo.setCommand(REMOVE_COMMAND);

                if (args.length != 2) {
                    throw new IllegalArgumentException("Preference name was not correctly specified");
                }

                clo.setPreferenceName(args[1]);
                break;
            default:
                Preference preference = parsePreference(args[0]);
                clo.setPreference(preference);
                clo.setPreferenceName(args[0]);

                if (args.length == 1) {
                    if (preference == null) {
                        throw new IllegalArgumentException("Requested preference was not found");
                    }

                    clo.setCommand(EXECUTE_COMMAND);
                } else {
                    clo.setCommand(PREFERENCE_COMMAND);
                    clo.setSource(parsePath(args[1]));
                    clo.setDestination(parsePath(args[2]));
                    clo.setOperationMode(args[3].equals("encrypt") ? OperationMode.ENCRYPT : OperationMode.DECRYPT);

                    if (args.length == 5) {
                        clo.setPairPreference(parsePreference(args[4]));
                    }
                }
                break;
        }

        return clo;
    }

    private Path parsePath(String arg) {
        Path path = Path.of(arg);

        if (!Files.exists(path)) {
            throw new IllegalArgumentException("Path " + arg + " does not exist");
        }

        return path;
    }

    private Preference parsePreference(String preferenceName) {
        return this.preferenceService.getPreference(preferenceName);
    }

    private void savePreference(CommandLineObject clo) throws AppException {
        Preference preference = new Preference(clo.getPreferenceName(), clo.getSource().toString(), clo.getDestination().toString(),
                clo.getPairPreference() != null ? clo.getPairPreference().getPassword() : passwordService.generateStrongPassword(),
                clo.getOperationMode());

        this.preferenceService.savePreference(preference);

        System.out.println("Preference " + clo.getPreferenceName() + " was saved");
    }

    private void doOperation(CommandLineObject clo) throws AppException {
        Preference preference = clo.getPreference();
        File sourceFile = new File(preference.getSource());
        File destinationFile = new File(preference.getDestination());

        this.operationService.initCipher(preference);
        String result = this.operationService.operate(sourceFile, destinationFile);
        String[] parsedResult = result.split("/");
        System.out.println("Finished! Updated " + parsedResult[0] + " and removed " + parsedResult[1] + " additional files");
    }

    private void listPreference(CommandLineObject clo) {
        if (clo.getPreference() != null) {
            System.out.println(clo.getPreference().getPrintableFormat());
        } else {
            this.preferenceService.getPreferencesNames().forEach(System.out::println);
        }
    }

    private void removePreference(CommandLineObject clo) {
        try {
            this.preferenceService.removePreference(clo.getPreferenceName());
            System.out.println("Removed preference: " + clo.getPreferenceName());
        } catch (AppException e) {
            System.out.println(e.getMessage());
        }
    }
}
