package locker;

import locker.exception.AppException;
import locker.service.input.CommandLineService;
import locker.service.input.UIService;
import locker.ui.MainFrame;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private final UIService uiService;
    private final CommandLineService commandLineService;

    public Application(UIService uiService, CommandLineService commandLineService) {
        this.uiService = uiService;
        this.commandLineService = commandLineService;
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).headless(false).run(args);
    }

    @Override
    public void run(String... args) throws AppException {
        if (args.length == 0) {
            this.uiService.loadUI(new MainFrame());
        } else {
            this.commandLineService.process(args);
        }
    }
}
