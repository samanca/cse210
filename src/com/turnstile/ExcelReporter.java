package com.turnstile;

import java.io.File;
import jxl.*;
import jxl.write.*;
import jxl.write.Number;

import java.io.IOException;
import java.util.ArrayList;

public class ExcelReporter {

    static ExcelReporter singleton = null;

    public static ExcelReporter SingleInstance() {
        if (singleton == null) singleton = new ExcelReporter();
        return singleton;
    }

    public boolean Export(ArrayList<TSheet> sheets, String outputPath) {


        WritableWorkbook workbook;
        try {
            workbook = Workbook.createWorkbook(new File(outputPath));

        }
        catch (Exception ex) {
            //TODO handle this
            return false;
        }

        int sheetIndex = 0;
        for (TSheet rawSheet : sheets) {
            WritableSheet sheet = workbook.createSheet(rawSheet.label, sheetIndex++);

            // Month | Day
            Label label = new Label(0, 0, rawSheet.isSummary ? "Month" : "Day");
            try {
                sheet.addCell(label);
            }
            catch (WriteException ex) {
                //TODO handle this
            }

            // Visitor types
            for (int i = 0; i < rawSheet.columns.length; i++) {
                Label t;
                if (!rawSheet.isSummary) {
                    t = new Label(i + 1, 0, rawSheet.columns[i]);
                }
                else {
                    t = new Label(0, i + 1, rawSheet.columns[i]);
                }
                try {
                    sheet.addCell(t);
                }
                catch (WriteException ex) {
                    //TODO handle this
                }
            }

            // Export data
            int rowNumber = 1;
            for(String[] row : rawSheet.rows) {
                for(int i = 0; i < row.length; i++) {
                    WritableCell t;
                    if (!rawSheet.isSummary) {
                    	t = new Number(i, rowNumber, Double.parseDouble(row[i]));
                    }
                    else {
                    	t = new Formula(rowNumber, i, row[i]);
                    }
                    try {
                        sheet.addCell(t);
                    }
                    catch (WriteException ex) {
                        //TODO handle this
                    }
                }
                rowNumber++;
            }
            
            
            // Add statistics
            if (rawSheet.isSummary) {
                try {
                    Label t = new Label(0, 11, "Average");
                    sheet.addCell(t);
                    t = new Label(1, 11, sheets.get(0).average);
                    sheet.addCell(t);
                    t = new Label(0, 12, "High");
                    sheet.addCell(t);
                    t = new Label(1, 12, sheets.get(0).high);
                    sheet.addCell(t);
                    t = new Label(0, 13, "Low");
                    sheet.addCell(t);
                    t = new Label(1, 13, sheets.get(0).low);
                    sheet.addCell(t);
                }
                catch (WriteException ex) {
                    //TODO handle this
                }
            }
        }

        try {
            workbook.write();
            workbook.close();
        }
        catch (Exception ex) {
            //TODO handle this
            return false;
        }

        return true;
    }
}
