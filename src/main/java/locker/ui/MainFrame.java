/*
 * Created by JFormDesigner on Sun Jul 04 11:28:38 EEST 2021
 */

package locker.ui;

import locker.event.OperationMode;
import locker.event.UIEventHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author unknown
 */
public class MainFrame extends JFrame {
    private final UIEventHandler eventHandler;
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
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

    public MainFrame(UIEventHandler eventHandler) {
        this.eventHandler = eventHandler;

        this.loadPreferenceLabel = new JLabel();
        this.loadPreferenceComboBox = new JComboBox<>();
        this.sourceLabel = new JLabel();
        this.sourceLocationField = new JTextField();
        this.sourceButton = new JButton();
        this.destinationLabel = new JLabel();
        this.destinationLocationField = new JTextField();
        this.destinationButton = new JButton();
        this.passwordLabel = new JLabel();
        this.passwordField = new JPasswordField();
        this.operationLabel = new JLabel();
        this.operationComboBox = new JComboBox<>();
        this.removePreferenceButton = new JButton();
        this.savePreferenceButton = new JButton();
        this.startOperationButton = new JButton();

        initComponents();
        initFileChooser();
    }

    private void loadPreferenceComboBoxActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void createUIComponents() {
        // TODO: add custom component creation code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
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
        this.loadPreferenceComboBox.addActionListener(e -> loadPreferenceComboBoxActionPerformed(e));

        //---- sourceLabel ----
        this.sourceLabel.setText("Source location");
        this.sourceLabel.setLabelFor(this.sourceLocationField);

        //---- sourceLocationField ----
        this.sourceLocationField.setEditable(false);
        this.sourceLocationField.setText("The location that will be used as input...");
        this.sourceLocationField.setEnabled(false);

        //---- sourceButton ----
        this.sourceButton.setText("Browse...");

        //---- destinationLabel ----
        this.destinationLabel.setText("Destination location");
        this.destinationLabel.setLabelFor(this.destinationLocationField);

        //---- destinationLocationField ----
        this.destinationLocationField.setEditable(false);
        this.destinationLocationField.setText("The location where the results will be written...");
        this.destinationLocationField.setEnabled(false);

        //---- destinationButton ----
        this.destinationButton.setText("Browse...");

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

        //---- removePreferenceButton ----
        this.removePreferenceButton.setText("Remove");

        //---- savePreferenceButton ----
        this.savePreferenceButton.setText("Save Preference");
        this.savePreferenceButton.setBackground(Color.white);

        //---- startOperationButton ----
        this.startOperationButton.setText("Encrypt");
        this.startOperationButton.setBackground(Color.white);

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(this.startOperationButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addComponent(this.destinationLocationField, GroupLayout.PREFERRED_SIZE, 482, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(this.destinationButton, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE))
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
                                        .addComponent(this.passwordField, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(this.operationComboBox, GroupLayout.PREFERRED_SIZE, 185, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(46, Short.MAX_VALUE))
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
                                .addComponent(this.passwordField, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(this.operationLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(this.operationComboBox, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(this.savePreferenceButton, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(this.startOperationButton, GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                                .addGap(17, 17, 17))
        );
        pack();
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void initFileChooser() {
        // do nothing
    }
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
