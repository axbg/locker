package locker.service.impl;

import locker.event.OperationMode;
import locker.exception.AppException;
import locker.object.CommandLineObject;
import locker.object.Preference;
import locker.service.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommandLineServiceImpl implements CommandLineService {
    private final PreferenceService preferenceService;
    private final FileService fileService;
    private final PasswordService passwordService;
    private final CryptoService cryptoService;

    CommandLineServiceImpl(PreferenceService preferenceService, FileService fileService,
                           PasswordService passwordService, CryptoService cryptoService) {
        this.preferenceService = preferenceService;
        this.fileService = fileService;
        this.passwordService = passwordService;
        this.cryptoService = cryptoService;
    }

    @Override
    public void process(String... args) throws AppException {
        this.preferenceService.loadInitialPreferences();
        CommandLineObject clo = this.parseArgs(args);

        if (args.length == 1) {
            this.doOperation(clo);
        } else {
            this.savePreference(clo);
        }
    }

    private CommandLineObject parseArgs(String... args) {
        if (args.length != 1 && args.length != 4 && args.length != 5) {
            throw new IllegalArgumentException(
                    "4 or 5 arguments are required: (add_preference) source, destination, operation, preference_name. " +
                            "Only " + args.length + " were passed");
        }

        CommandLineObject clo = new CommandLineObject();
        clo.setPreferenceName(args[0]);

        Preference preference = parsePreference(args[0]);
        clo.setPreference(preference);

        if (args.length == 1) {
            if (preference == null) {
                throw new IllegalArgumentException("Requested preference was not found");
            }
        } else {
            clo.setSource(parsePath(args[1]));
            clo.setDestination(parsePath(args[2]));
            clo.setOperationMode(args[3].equals("encrypt") ? OperationMode.ENCRYPT : OperationMode.DECRYPT);

            if (args.length == 5) {
                clo.setPairPreference(parsePreference(args[4]));
            }
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
        int updated = 0;
        List<String> currentFiles = new ArrayList<>();

        Preference preference = clo.getPreference();
        File sourceFile = new File(preference.getSource());
        File destinationFile = new File(preference.getDestination());

        if (!this.cryptoService.initCipher(preference.getPassword(), preference.getOperationMode())) {
            throw new AppException("Exception occurred during secret reading");
        }

        for (File file : this.fileService.getFilesFromDestination(sourceFile)) {
            String name = this.cryptoService.doNameOperation(file.getName());
            currentFiles.add(name);

            if (!this.fileService.versionAlreadyExisting(file, destinationFile, name)) {
                byte[] content = this.cryptoService.doContentOperation(file);
                if (!fileService.saveFile(destinationFile, name, content)) {
                    System.out.println("Error occurred while executing operation on " + file.getName());
                }

                updated++;
            }
        }

        long removed = this.fileService.wipeAdditionalFiles(currentFiles, destinationFile);
        System.out.println("Finished! Updated " + updated + " and removed " + removed + " additional files");
    }
}
