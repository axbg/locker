package locker.ui;

import javax.swing.*;

public class Panel extends JFrame {
    private JLabel placeholderLabel;

    public Panel(String title) {
        super(title);
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
        this.placeholderLabel = new JLabel();
        this.placeholderLabel.setText("hello locker");
        this.placeholderLabel.setBounds(0, 0, 150, 10);

        this.add(this.placeholderLabel);
    }
}
