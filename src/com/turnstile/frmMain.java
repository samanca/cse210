package com.turnstile;

import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.filechooser.*;
import java.io.*;

/**
 * Main interface class
 * @author Amirsaman Memaripour
 * @version 1.0
 */

public class frmMain implements ActionListener {

    private JButton btnLoad;
    private JPanel pnMain;
    private JButton btnProcess;
    private JProgressBar pbProgress;
    private JTextArea txtOutput;

    private String inputFile;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLoad) {
            inputFile = openFileDialog();
            if (inputFile != null) {
                String fileName = FilenameUtils.getName(inputFile);
                txtOutput.setText("Your file, \"" + fileName + "\", loaded successfully. Ready to generate report.");
            }
        }
        else if (e.getSource() == btnProcess) {
            if (inputFile != null) {
                String ext = FilenameUtils.getExtension(inputFile);
                if (ext.equals("pdf")) {
                    txtOutput.setText("");
                    new BackgroundWorker(inputFile, txtOutput).execute();
                }
                else {
                    txtOutput.setText("Incorrect file type. Click Load Scanned Sheets again and choose a PDF file.");
                }
            }
            else {
                txtOutput.setText("No file selected. Click Load Scanned Sheets and select a PDF file.");
            }
        }
        else {
            System.out.println("registered but not handled");
        }
    }

    public frmMain() {
        inputFile = null;
        btnLoad.addActionListener(this);
        btnProcess.addActionListener(this);
    }

    private static String getStartupMessage(){
        return "Turnstile reads scanned sign-in sheets and compiles a report of Depot attendance. " +
                "To begin, click Load Scanned Sheets to choose a PDF file. Click OK to start.";
    }

    public static void main(String[] args) {

        frmMain obj = new frmMain();
        JFrame frame = new JFrame("frmMain");
        JOptionPane.showMessageDialog (null,frmMain.getStartupMessage());
        frame.setContentPane(obj.pnMain);
        frame.setTitle("Turnstile");
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        obj.txtOutput.setMargin(new Insets(5, 5, 5, 5));
        frame.setVisible(true);
    }

    /**
     * @return selected file name
     */
    private String openFileDialog() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("PDF files", "pdf"));
        if (fc.showOpenDialog(pnMain) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            return file.getAbsolutePath();
        }
        return null;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
