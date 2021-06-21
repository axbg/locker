package locker.ui;

import locker.event.OperationMode;
import locker.event.UIEvent;
import locker.event.UIEventHandler;

import javax.swing.*;
import java.awt.*;

public class Panel extends JFrame {
    private static final String STATUS_EMPTY_PASSWORD = "Password cannot be empty";
    private static final OperationMode[] OPERATION_MODES = new OperationMode[]{OperationMode.ENCRYPT, OperationMode.DECRYPT};

    private final JLabel sourceLabel = new JLabel();
    private final JButton sourceButton = new JButton();
    private final JFileChooser sourceChooser = new JFileChooser();

    private final JLabel destinationLabel = new JLabel();
    private final JButton destinationButton = new JButton();
    private final JFileChooser destinationChooser = new JFileChooser();

    private final JLabel passwordLabel = new JLabel();
    private final JPasswordField passwordField = new JPasswordField();

    private final JComboBox<OperationMode> operationComboBox = new JComboBox<>(OPERATION_MODES);

    private final JButton startButton = new JButton();

    private final JLabel statusLabel = new JLabel();

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
        this.initFileChooser(sourceLabel, sourceButton, sourceChooser, "Source location", UIEvent.SOURCE_FILE_SELECTED, JFileChooser.FILES_AND_DIRECTORIES, 50, 0);
        this.initFileChooser(destinationLabel, destinationButton, destinationChooser, "Destination location", UIEvent.DESTINATION_FILE_SELECTED, JFileChooser.DIRECTORIES_ONLY, 50, 60);

        this.passwordLabel.setText("Insert your password");
        this.passwordLabel.setBounds(150, 110, 200, 20);
        this.passwordField.setEchoChar('*');
        this.passwordField.setBounds(110, 130, 200, 40);

        this.operationComboBox.setBounds(110, 170, 200, 30);
        this.operationComboBox.addActionListener(a -> this.eventHandler.handle(UIEvent.OPERATION_MODE_SELECTED, this.operationComboBox.getSelectedItem()));

        this.startButton.setText("Do it!");
        this.startButton.setBounds(50, 200, 300, 20);
        this.startButton.addActionListener(a -> {
            if (this.passwordField.getPassword().length != 0) {
                this.setStatus("", Color.BLACK);
                this.eventHandler.handle(UIEvent.START, String.valueOf(this.passwordField.getPassword()));
            } else {
                this.setStatus(STATUS_EMPTY_PASSWORD, Color.RED);
            }
        });

        this.statusLabel.setText("Test status");
        this.statusLabel.setBounds(150, 240, 400, 20);

        this.add(passwordLabel);
        this.add(passwordField);
        this.add(operationComboBox);
        this.add(startButton);
        this.add(statusLabel);
    }

    private void initFileChooser(final JLabel label, final JButton button, final JFileChooser chooser,
                                 String name, UIEvent event, int allowedTypes, int x, int y) {
        label.setBounds(x + 30, y + 30, 400, 20);

        button.setText(name);
        button.setBounds(x, y, 300, 20);
        button.addActionListener(action -> {
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                label.setText(chooser.getSelectedFile().getAbsolutePath());
                this.eventHandler.handle(event, chooser.getSelectedFile());
            }
        });

        chooser.setFileSelectionMode(allowedTypes);

        this.add(label);
        this.add(button);
        this.add(chooser);
    }

    public void setStatus(String status, Color color) {
        this.statusLabel.setText(status);
        this.statusLabel.setForeground(color);
    }
}
