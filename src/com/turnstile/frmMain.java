package com.turnstile;

//import com.sun.org.apache.bcel.internal.generic.NEW;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.filechooser.*;
import java.io.*;
import java.lang.Thread;

/**
 * Main interface class
 * @author Amirsaman Memaripour
 * @version 1.0
 */

public class frmMain implements ActionListener {

    static final String NEWLINE = "\r\n";

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
                System.out.println("input: " + inputFile);
            }
        }
        else if (e.getSource() == btnProcess) {
            if (inputFile != null) {
                txtOutput.setText(process(inputFile));
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

    public static void main(String[] args) {

        frmMain obj = new frmMain();

        JFrame frame = new JFrame("frmMain");
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

    private String process(String input) {

//        String retVal = "Start processing file " + input + NEWLINE;
//        retVal += "Generating output ..." + NEWLINE;
//        for (int i = 0; i < 10; i++) {
//            retVal += "Processing page " + i + " ..." + NEWLINE;
//            try { Thread.sleep(100); } catch (Exception ex) {}
//            pbProgress.setValue(pbProgress.getValue() + 10);
//
//        }
//
//        retVal+="Error reading \"Type of Resident\" in sheet 4, row 5: all checkboxes are blank\n" +
//                "Error reading \"Type of Resident\" in sheet 6, row 7: multiple checkboxs are filled\n" +
//                "Error reading \"HVRP\" in sheet 6, row 7: please make sure the checkbox is either blank or completely filled\n" +
//                "Error reading \"Sheet Number\" in page 3 of the PDF file\n" +
//                "Error reading \"Day of Month\" in page 4 of the PDF file\n" +
//                "Error processing page 5 of the PDF file: page does not match the expected format\n";
//
//        retVal += "Done" + NEWLINE;
//        return retVal;

        String[] pipe1 = PDFReader.SingleInstance().Import(input, "");
        Results pipe2 = Imageprocess.main(pipe1);


//
//        ExcelReporter rep = ExcelReporter.SingleInstance();
//        rep.Export(TSheet.RandomSheets(), "output.xls");

        return "DONE" + NEWLINE;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
