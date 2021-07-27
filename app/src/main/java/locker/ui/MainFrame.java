/*
 * Created by JFormDesigner on Sun Jul 04 11:28:38 EEST 2021
 */

package locker.ui;

import locker.event.OperationMode;
import locker.event.UIEvent;
import locker.event.UIEventHandler;
import locker.object.Preference;

import javax.swing.*;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.io.File;
import java.util.List;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.OK_OPTION;

public class MainFrame extends JFrame {
    private static final String PREFERENCE_PLACEHOLDER = "---------";
    private static final OperationMode[] OPERATION_MODES = new OperationMode[]{OperationMode.ENCRYPT, OperationMode.DECRYPT};
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Alex Bisag
    private final JLabel loadPreferenceLabel;
    private final JComboBox<String> loadPreferenceComboBox;
    private final JLabel sourceLabel;
    private final JTextField sourceLocationField;
    private final JButton sourceButton;
    private final JLabel destinationLabel;
    private final JTextField destinationLocationField;
    private final JButton destinationButton;
    private final JLabel passwordLabel;
    private final JPasswordField passwordField;
    private final JLabel operationLabel;
    private final JComboBox<OperationMode> operationComboBox;
    private final JButton removePreferenceButton;
    private final JButton savePreferenceButton;
    private final JButton startOperationButton;
    private final JButton exportPreferencesButton;
    private final JButton importPreferencesButton;
    private final JButton showPasswordButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
    private final JFileChooser sourceChooser;
    private final JFileChooser destinationChooser;
    private final JFileChooser exportPreferencesChooser;

    private boolean showPasswordToggle = false;
    private UIEventHandler eventHandler;

    public MainFrame() {
        this.loadPreferenceLabel = new JLabel();
        this.loadPreferenceComboBox = new JComboBox<>();
        this.removePreferenceButton = new JButton();

        this.sourceLabel = new JLabel();
        this.sourceLocationField = new JTextField();
        this.sourceButton = new JButton();
        this.sourceChooser = new JFileChooser();

        this.destinationLabel = new JLabel();
        this.destinationLocationField = new JTextField();
        this.destinationButton = new JButton();
        this.destinationChooser = new JFileChooser();

        this.passwordLabel = new JLabel();
        this.passwordField = new JPasswordField();
        this.showPasswordButton = new JButton();

        this.operationLabel = new JLabel();
        this.operationComboBox = new JComboBox<>(OPERATION_MODES);

        this.savePreferenceButton = new JButton();
        this.startOperationButton = new JButton();

        this.importPreferencesButton = new JButton();
        this.exportPreferencesButton = new JButton();
        this.exportPreferencesChooser = new JFileChooser();

        initComponents();
    }

    public void setEventHandler(UIEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Alex Bisag
        createUIComponents();


        //======== this ========
        setTitle("Locker");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        var contentPane = getContentPane();

        //---- loadPreferenceLabel ----
        this.loadPreferenceLabel.setText("Load preference");
        this.loadPreferenceLabel.setLabelFor(this.loadPreferenceComboBox);

        //---- loadPreferenceComboBox ----
        this.loadPreferenceComboBox.addActionListener(e -> loadPreferenceComboBoxActionPerformed());

        //---- sourceLabel ----
        this.sourceLabel.setText("Source location");
        this.sourceLabel.setLabelFor(this.sourceLocationField);

        //---- sourceLocationField ----
        this.sourceLocationField.setEditable(false);
        this.sourceLocationField.setText("The location that will be used as input...");
        this.sourceLocationField.setEnabled(false);

        //---- sourceButton ----
        this.sourceButton.setText("Browse...");
        this.sourceButton.addActionListener(e -> sourceButtonActionPerformed());

        //---- destinationLabel ----
        this.destinationLabel.setText("Destination location");
        this.destinationLabel.setLabelFor(this.destinationLocationField);

        //---- destinationLocationField ----
        this.destinationLocationField.setEditable(false);
        this.destinationLocationField.setText("The location where the results will be written...");
        this.destinationLocationField.setEnabled(false);

        //---- destinationButton ----
        this.destinationButton.setText("Browse...");
        this.destinationButton.addActionListener(e -> destinationButtonActionPerformed());

        //---- passwordLabel ----
        this.passwordLabel.setText("Password");
        this.passwordLabel.setLabelFor(this.passwordField);

        //---- passwordField ----
        this.passwordField.setEchoChar('*');
        this.passwordField.setToolTipText("Insert a very hard to guess password");

        //---- operationLabel ----
        this.operationLabel.setText("Operation *");
        this.operationLabel.setLabelFor(this.operationComboBox);

        //---- operationComboBox ----
        this.operationComboBox.setToolTipText("Select the desired operation");
        this.operationComboBox.addActionListener(e -> operationComboBoxActionPerformed());

        //---- removePreferenceButton ----
        this.removePreferenceButton.setText("Remove");
        this.removePreferenceButton.addActionListener(e -> removePreferenceButtonActionPerformed());

        //---- savePreferenceButton ----
        this.savePreferenceButton.setText("Save Preference");
        this.savePreferenceButton.setBackground(Color.white);
        this.savePreferenceButton.addActionListener(e -> savePreferenceButtonActionPerformed());

        //---- startOperationButton ----
        this.startOperationButton.setText("Encrypt");
        this.startOperationButton.setBackground(Color.white);
        this.startOperationButton.addActionListener(e -> startOperationButtonActionPerformed());

        //---- exportPreferencesButton ----
        this.exportPreferencesButton.setText("Export Preferences");
        this.exportPreferencesButton.addActionListener(e -> exportPreferencesButtonActionPerformed());

        //---- importPreferencesButton ----
        this.importPreferencesButton.setText("Import Preferences");
        this.importPreferencesButton.addActionListener(e -> importPreferencesButtonActionPerformed());

        //---- showPasswordButton ----
        this.showPasswordButton.setText("Show");
        this.showPasswordButton.addActionListener(e -> showPasswordButtonActionPerformed());

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(this.startOperationButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addComponent(this.sourceLocationField, GroupLayout.PREFERRED_SIZE, 482, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(this.sourceButton, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(this.operationLabel)
                                        .addComponent(this.savePreferenceButton, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(this.passwordLabel)
                                        .addComponent(this.destinationLabel)
                                        .addComponent(this.sourceLabel)
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addComponent(this.loadPreferenceLabel)
                                                .addGap(18, 18, 18)
                                                .addComponent(this.loadPreferenceComboBox, GroupLayout.PREFERRED_SIZE, 364, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(this.removePreferenceButton, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addComponent(this.passwordField, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(this.showPasswordButton))
                                        .addComponent(this.operationComboBox, GroupLayout.PREFERRED_SIZE, 185, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addComponent(this.destinationLocationField, GroupLayout.PREFERRED_SIZE, 482, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(this.destinationButton, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(46, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                .addContainerGap(153, Short.MAX_VALUE)
                                .addComponent(this.importPreferencesButton)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(this.exportPreferencesButton)
                                .addGap(171, 171, 171))
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(this.removePreferenceButton, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(this.loadPreferenceLabel)
                                        .addComponent(this.loadPreferenceComboBox, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
                                .addGap(37, 37, 37)
                                .addComponent(this.sourceLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(this.sourceLocationField, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(this.sourceButton, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(this.destinationLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(this.destinationButton, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(this.destinationLocationField, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
                                .addGap(13, 13, 13)
                                .addComponent(this.passwordLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(this.passwordField, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(this.showPasswordButton, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addComponent(this.operationLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(this.operationComboBox, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(this.savePreferenceButton, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(this.startOperationButton, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(this.exportPreferencesButton, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(this.importPreferencesButton, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );
        pack();
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void createUIComponents() {
        this.sourceChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        this.destinationChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        this.exportPreferencesChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }

    private void savePreferenceButtonActionPerformed() {
        String name = requestStringInput("Insert a name for the new preference", "Save preference", 3);

        if (name == null) {
            return;
        }

        this.eventHandler.handle(UIEvent.SAVE_PREFERENCE, new String(this.passwordField.getPassword()), name);
    }

    private void loadPreferenceComboBoxActionPerformed() {
        this.eventHandler.handle(UIEvent.LOAD_PREFERENCE, this.loadPreferenceComboBox.getSelectedItem());
    }

    private void operationComboBoxActionPerformed() {
        OperationMode selected = (OperationMode) this.operationComboBox.getSelectedItem();
        this.startOperationButton.setText(selected == OperationMode.ENCRYPT ? "Encrypt" : "Decrypt");
        this.eventHandler.handle(UIEvent.OPERATION_MODE_SELECTED, selected);
    }

    private void startOperationButtonActionPerformed() {
        if (this.passwordField.getPassword().length != 0) {
            this.eventHandler.handle(UIEvent.START, String.valueOf(this.passwordField.getPassword()));
        } else {
            this.displayMessagePrompt("Password cannot be empty", ERROR_MESSAGE);
        }
    }

    private void sourceButtonActionPerformed() {
        if (sourceChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            sourceLocationField.setText(sourceChooser.getSelectedFile().getAbsolutePath());
            this.eventHandler.handle(UIEvent.SOURCE_FILE_SELECTED, sourceChooser.getSelectedFile());
        }
    }

    private void destinationButtonActionPerformed() {
        if (destinationChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            destinationLocationField.setText(destinationChooser.getSelectedFile().getAbsolutePath());
            this.eventHandler.handle(UIEvent.DESTINATION_FILE_SELECTED, destinationChooser.getSelectedFile());
        }
    }

    private void removePreferenceButtonActionPerformed() {
        this.eventHandler.handle(UIEvent.REMOVE_PREFERENCE, this.loadPreferenceComboBox.getSelectedItem());
    }

    private void showPasswordButtonActionPerformed() {
        String text = "Show";

        if (this.showPasswordToggle) {
            this.passwordField.setEchoChar('*');
        } else {
            this.passwordField.setEchoChar((char) 0);
            text = "Hide";
        }

        this.showPasswordToggle = !this.showPasswordToggle;
        this.showPasswordButton.setText(text);
    }

    private void importPreferencesButtonActionPerformed() {
        this.exportPreferencesChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (exportPreferencesChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String password = requestPreferencePassword("Insert the password that will be used to decrypt the imported preferences");

            if (password == null) {
                return;
            }

            String postfix = requestStringInput("What prefix would you like to use for the imported preferences?\n" +
                            "If no prefix is used, existing preferences with the same name will be overwritten",
                    "Preferences postfix", 0);

            if (postfix == null) {
                return;
            }

            this.eventHandler.handle(UIEvent.IMPORT_PREFERENCES, password, exportPreferencesChooser.getSelectedFile(), postfix);
        }
    }

    private void exportPreferencesButtonActionPerformed() {
        if (JOptionPane.showConfirmDialog(null,
                "Are you sure you want to export your preferences?",
                "Exporting preferences",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != OK_OPTION) {
            return;
        }

        String password = requestPreferencePassword("Insert a new password that will be used to encrypt the exported preferences");
        if (password == null) {
            return;
        }

        this.exportPreferencesChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (exportPreferencesChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            this.eventHandler.handle(UIEvent.EXPORT_PREFERENCES, password, exportPreferencesChooser.getSelectedFile());
        }
    }

    private String requestStringInput(String message, String title, int limit) {
        String input = (String) JOptionPane.showInputDialog(this, message, title, JOptionPane.PLAIN_MESSAGE, null, null, "");

        if (input != null && input.length() < limit) {
            this.displayMessagePrompt("Input should have at least " + limit + " + characters", ERROR_MESSAGE);
            return null;
        }

        return input;
    }

    private String requestPreferencePassword(String message) {
        JPasswordField preferencesPassword = new JPasswordField();
        preferencesPassword.setToolTipText(message);
        preferencesPassword.addHierarchyListener(e -> {
            if (e.getComponent().isShowing() && (e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0)
                SwingUtilities.invokeLater(e.getComponent()::requestFocusInWindow);
        });

        int passwordInserted = JOptionPane.showConfirmDialog(this, preferencesPassword,
                "New preferences password",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (passwordInserted != OK_OPTION) {
            return null;
        }

        String password = new String(preferencesPassword.getPassword());

        if (password.isBlank() || password.length() < 7) {
            this.displayMessagePrompt("The password should have more than 6 characters", ERROR_MESSAGE);
            return null;
        }

        return password;
    }

    public void setPreferences(List<String> preferences, boolean selectLast) {
        this.loadPreferenceComboBox.removeAllItems();

        this.loadPreferenceComboBox.addItem(PREFERENCE_PLACEHOLDER);

        if (preferences.isEmpty()) {
            this.togglePreferenceControls(false);
            return;
        }

        preferences.forEach(this.loadPreferenceComboBox::addItem);

        if (selectLast) {
            this.loadPreferenceComboBox.setSelectedIndex(preferences.size());
        }

        this.togglePreferenceControls(true);
    }

    private void togglePreferenceControls(boolean state) {
        this.loadPreferenceComboBox.setEnabled(state);
        this.removePreferenceButton.setEnabled(state);
    }

    public void displayPreference(Preference preference) {
        if (preference != null) {
            File source = new File(preference.getSource());
            File destination = new File(preference.getDestination());

            if (!source.exists()) {
                this.displayMessagePrompt("Source file not found", ERROR_MESSAGE);
                return;
            } else if (!destination.exists()) {
                this.displayMessagePrompt("Destination file not found", ERROR_MESSAGE);
                return;
            } else if (preference.getPassword().isBlank()) {
                this.displayMessagePrompt("Loaded password is blank", ERROR_MESSAGE);
                return;
            }

            this.sourceLocationField.setText(preference.getSource());
            this.destinationLocationField.setText(preference.getDestination());
            this.passwordField.setText(preference.getPassword());
            this.operationComboBox.setSelectedItem(preference.getOperationMode());

            this.eventHandler.handle(UIEvent.SOURCE_FILE_SELECTED, source);
            this.eventHandler.handle(UIEvent.DESTINATION_FILE_SELECTED, destination);
        }
    }

    public void displayMessagePrompt(String message, int type) {
        JOptionPane.showMessageDialog(this, message, "Locker", type);
    }
}
