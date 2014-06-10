package com.turnstile;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class BackgroundWorker extends SwingWorker<String, String> {

    private String input;
    private JTextArea textArea;

    public BackgroundWorker(String inputFile, JTextArea txtOutput) {
        input = inputFile;
        textArea = txtOutput;
    }

    @Override
    protected String doInBackground() throws Exception {

        publish("Processing ...");

        // Filter (1): PDF to IMAGE
        String[] images;
        try {
            images = PDFBoxReader.SingleInstance().Import(input, "temp/");
        }
        catch (Exception ex) {
            return ex.getMessage();
        }

        // Filter (2): IMAGE to DATA-ARRAY
        Results results = Imageprocess.process(images);

        // Filter (3): DATA-ARRAY to OUTPUT-LOG
//        String log = Logger.SingleInstance().Serialize(results.getErrmsgs());
//        Logger.SingleInstance().Write("output.txt", log);
//        publish(log);

        // Filter (4): DATA-ARRAY to EXCEL-FRIENDLY
        ArrayList<TSheet> sheets = TSheet.generateMonth(results.tallies, "Month"); // Used as the sheet label

        // Filter (5): EXCEL-FRIENDLY to EXCEL
        ExcelReporter reporter = ExcelReporter.SingleInstance();
        reporter.Export(sheets, "output.xls");

        publish("Your report is saved in the Turnstile folder as output.xls. You may close this window.");

        return null;
    }

    protected void process(List<String> item) {
        textArea.setText("");
        for (String s : item)
            textArea.append(s + "\n");
    }
}
