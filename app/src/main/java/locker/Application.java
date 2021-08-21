package locker;

import locker.service.CommandLineService;
import locker.service.UIService;
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
    public void run(String... args) {
        if (args.length == 0) {
            this.uiService.loadUI(new MainFrame());
        } else {
            this.commandLineService.process(args);
        }
    }
}
