package locker.ui;

import locker.event.UIEvent;
import locker.event.UIEventHandler;

import javax.swing.*;

public class Panel extends JFrame {
    private final JLabel sourceLabel = new JLabel();
    private final JButton sourceButton = new JButton();
    private final JFileChooser sourceChooser = new JFileChooser();

    private final JLabel destinationLabel = new JLabel();
    private final JButton destinationButton = new JButton();
    private final JFileChooser destinationChooser = new JFileChooser();

    private UIEventHandler eventHandler;

    public Panel(String title, UIEventHandler eventHandler) {
        super(title);

        this.eventHandler = eventHandler;

        this.initLayout();
        this.initFields();
    }

    private void initLayout() {
        this.setSize(500, 340);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
    }

    private void initFields() {
        this.initFileChooser(sourceLabel, sourceButton, sourceChooser, "Source location", UIEvent.SOURCE_FILE_SELECTED, 0, 0);
        this.initFileChooser(destinationLabel, destinationButton, destinationChooser, "Destination location", UIEvent.DESTINATION_FILE_SELECTED, 0, 60);
    }

    private void initFileChooser(final JLabel label, final JButton button, final JFileChooser chooser, String name, UIEvent event, int x, int y) {
        label.setBounds(x + 30, y + 30, 400, 20);

        button.setText(name);
        button.setBounds(x, y, 300, 20);
        button.addActionListener(action -> {
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                label.setText(chooser.getSelectedFile().getAbsolutePath());
                this.eventHandler.handle(event, chooser.getSelectedFile());
            }
        });

        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        this.add(label);
        this.add(button);
        this.add(chooser);
    }
}
