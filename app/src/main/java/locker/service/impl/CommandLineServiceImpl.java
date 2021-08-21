package locker.service.impl;

import locker.object.CommandLineObject;
import locker.object.Preference;
import locker.service.CommandLineService;
import org.springframework.stereotype.Service;

@Service
public class CommandLineServiceImpl implements CommandLineService {
    private static final String SAVE_PREFERENCE_KEYWORD = "preference";

    @Override
    public void process(String... args) {
        CommandLineObject clo = this.parseArgs(args);

        if (args[0].equals(SAVE_PREFERENCE_KEYWORD)) {
            this.savePreference(clo);
        } else {
            this.doOperation(clo);
        }
    }

    private CommandLineObject parseArgs(String... args) {
        if (args.length != 4 && args.length != 5) {
            throw new IllegalArgumentException(
                    "4 or 5 arguments are required: (add_preference) source, destination, operation, preference_name. " +
                            "Only " + args.length + (args.length == 1 ? " was passed. " : " were passed."));
        }

        // parse args

        return new CommandLineObject();
    }

    private void verifyDiskLocation(String arg) {
        // verify if specified location exists on disk
    }

    private Preference getPreference(String preferenceName) {
        return null;
    }

    private void savePreference(CommandLineObject clo) {
        // do save preference routine
        // including password generation
    }

    private void doOperation(CommandLineObject clo) {
        // do operation routine
    }
}
