package locker.service.impl;

import locker.service.UIService;
import locker.ui.Panel;
import org.springframework.stereotype.Service;

@Service
public class UIServiceImpl implements UIService {
    public final static String PANEL_TITLE = "Locker";

    private final Panel panel;

    public UIServiceImpl() {
        this.panel = new Panel(PANEL_TITLE);
    }

    @Override
    public void loadUI() {
        this.panel.setVisible(true);
    }
}
