package com.turnstile;

import javax.swing.*;
import java.awt.*;

/**
 * Main interface class
 * @author Amirsaman Memaripour
 * @version 1.0
 */

public class frmMain {
    private JButton btnLoad;
    private JPanel pnMain;

    public frmMain() {
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("frmMain");
        frame.setContentPane(new frmMain().pnMain);
        frame.setTitle("Turnstile");
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private JButton btnProcess;
    private JProgressBar pbProgress;
    private JTextArea txtOutput;

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
