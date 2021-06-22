package locker.ui;

import locker.event.OperationMode;
import locker.event.UIEvent;
import locker.event.UIEventHandler;
import locker.object.Preference;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

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
    private final JLabel preferencesLabel = new JLabel();
    private final JComboBox<String> preferencesComboBox = new JComboBox<>();
    private final JButton preferencesButton = new JButton();

    private final UIEventHandler eventHandler;

    public Panel(String title, UIEventHandler eventHandler) {
        super(title);

        this.eventHandler = eventHandler;

        this.initLayout();
        this.initFields();
    }

    private void initLayout() {
        this.setSize(500, 400);
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

        this.preferencesLabel.setText("Load a preference");
        this.preferencesLabel.setBounds(110, 260, 200, 20);

        this.preferencesComboBox.setBounds(110, 280, 200, 30);

        this.preferencesButton.setText("Load");
        this.preferencesButton.setBounds(110, 320, 50, 20);
        this.preferencesButton.addActionListener(a -> this.eventHandler.handle(UIEvent.LOAD_PREFERENCE, this.preferencesComboBox.getSelectedItem()));

        this.add(passwordLabel);
        this.add(passwordField);
        this.add(operationComboBox);
        this.add(startButton);
        this.add(statusLabel);
        this.add(preferencesLabel);
        this.add(preferencesComboBox);
        this.add(preferencesButton);
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

    public void setPreferences(List<String> preferences) {
        if (preferences.isEmpty()) {
            this.preferencesComboBox.setEnabled(false);
            return;
        }

        this.preferencesComboBox.removeAllItems();
        preferences.forEach(this.preferencesComboBox::addItem);
        this.preferencesComboBox.setEnabled(true);
    }

    public void displayPreference(Preference preference) {
        File source = new File(preference.getSource());
        File destination = new File(preference.getDestination());

        if (!source.exists()) {
            this.eventHandler.handle(UIEvent.LOAD_PREFERENCE_ERROR, "Source file not found");
            return;
        } else if (!destination.exists()) {
            this.eventHandler.handle(UIEvent.LOAD_PREFERENCE_ERROR, "Destination file not found");
            return;
        } else if (preference.getPassword().isBlank()) {
            this.eventHandler.handle(UIEvent.LOAD_PREFERENCE_ERROR, "Loaded password is blank");
            return;
        }

        this.sourceLabel.setText(preference.getSource());
        this.destinationLabel.setText(preference.getDestination());
        this.passwordField.setText(preference.getPassword());
        this.operationComboBox.setSelectedItem(preference.getOperationMode());

        this.eventHandler.handle(UIEvent.SOURCE_FILE_SELECTED, source);
        this.eventHandler.handle(UIEvent.DESTINATION_FILE_SELECTED, destination);
    }

    public void setStatus(String status, Color color) {
        this.statusLabel.setText(status);
        this.statusLabel.setForeground(color);
    }
}
