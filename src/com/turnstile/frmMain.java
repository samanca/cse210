package com.turnstile;

//import com.sun.org.apache.bcel.internal.generic.NEW;

import org.apache.commons.io.FilenameUtils;

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
                String ext = FilenameUtils.getExtension(inputFile);
                if (ext.equals("pdf"))
                    txtOutput.setText(process(inputFile));
                else
                    txtOutput.setText("Invalid input file detected! Only PDF files are acceptable!");
            }
            else {
                txtOutput.setText("No file selected!");
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

    private String process(String input) {

        // Filter (1): PDF to IMAGE
        String[] images;
        try {
             //images = PDFReader.SingleInstance().Import(input, "temp/"); // Temporary directory
            images = PDFBoxReader.SingleInstance().Import(input, "temp/");
        }
        catch (Exception ex) {
            return ex.getMessage();
        }

        // Filter (2): IMAGE to DATA-ARRAY
        Results results = Imageprocess.process(images);

        // Filter (3): DATA-ARRAY to OUTPUT-LOG
        String log = Logger.SingleInstance().Serialize(results.getErrmsgs());
        Logger.SingleInstance().Write("output.txt", log);

        // Filter (4): DATA-ARRAY to EXCEL-FRIENDLY
        ArrayList<TSheet> sheets = TSheet.generateMonth(results.tallies, "Month"); // Used as the sheet label

        // Filter (5): EXCEL-FRIENDLY to EXCEL
        ExcelReporter reporter = ExcelReporter.SingleInstance();
        reporter.Export(sheets, "output.xls");

        return log;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
