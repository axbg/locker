package locker;

import locker.service.UIService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private final UIService uiService;

    public Application(UIService uiService) {
        this.uiService = uiService;
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).headless(false).run();
    }

    @Override
    public void run(String... args) {
        this.uiService.loadUI();
    }
}
