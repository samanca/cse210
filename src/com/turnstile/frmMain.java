package com.turnstile;

//import com.sun.org.apache.bcel.internal.generic.NEW;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.filechooser.*;
import java.io.*;
import java.util.ArrayList;

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

        String[] pipe1 = PDFReader.SingleInstance().Import(input, "");
        Results pipe2 = Imageprocess.process(pipe1);
        ArrayList<TSheet> sheets = TSheet.generateMonth(pipe2.tallies, "June");
        ExcelReporter reporter = ExcelReporter.SingleInstance();
        reporter.Export(sheets, "output.xls");
        String log = Logger.SingleInstance().Serialize(pipe2.getErrmsgs());
        Logger.SingleInstance().Write("output.txt", log);

        return log;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
