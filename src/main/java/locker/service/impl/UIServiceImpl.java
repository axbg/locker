package locker.service.impl;

import locker.event.UIEvent;
import locker.service.UIService;
import locker.ui.Panel;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class UIServiceImpl implements UIService {
    public final static String PANEL_TITLE = "Locker";

    private final Panel panel;

    private File sourceFile;
    private File destinationFile;

    public UIServiceImpl() {
        this.panel = new Panel(PANEL_TITLE, this::handleUIEvent);
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
            default:
                break;
        }
    }
}
