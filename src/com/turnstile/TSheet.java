package com.turnstile;

import java.util.ArrayList;
import java.util.Random;

public class TSheet {
    public ArrayList<String[]> rows;
    public String[] columns = {
            "Interim",
            "Perm",
            "Alumni",
            "HVRP"
    };
    public String label;
    public boolean isSummary = false;

    public TSheet() {
        rows = new ArrayList<String[]>();
    }

    public static TSheet RandomSheet(String label, boolean isSummary) {
        TSheet sheet = new TSheet();
        sheet.label = label;
        sheet.isSummary = isSummary;

        Random rand = new Random();
        int rowQ = isSummary ? 1 : 31;

        for (int i = 0; i < rowQ; i++) {
            String[] t = new String[sheet.columns.length + 1];
            t[0] = String.valueOf(i + 1);
            for(int j = 1; j < t.length; j++)
                t[j] = String.valueOf(rand.nextInt(100));
            sheet.rows.add(t);
        }

        return sheet;
    }

    public static ArrayList<TSheet> RandomSheets() {
        ArrayList<TSheet> retVal = new ArrayList<TSheet>();

        retVal.add(RandomSheet("Summary", true));
        retVal.add(RandomSheet("April", false));
        retVal.add(RandomSheet("May", false));

        return retVal;
    }
    
    /**
     * Generates the TSheet with the data corresponding to a month and also
     * generates the TSheet containing the summary of the data.
     * 
     * @param data  The 31x4 array of data for a specific month
     * @param month The name of the month the data represents
     * @return      An ArrayList containing the mentioned TSheets
     */
    public static ArrayList<TSheet> generateMonth(int[][] data, String month) {
    	ArrayList<TSheet> retVal = new ArrayList<TSheet>(2);
    	TSheet sheet = new TSheet();
        sheet.label = month;
        sheet.isSummary = false;
    	
    	for (int i = 0; i < data.length; i++) {
    		String[] t = new String[sheet.columns.length + 1];
            t[0] = String.valueOf(i + 1);
            for(int j = 1; j < t.length; j++)
                t[j] = "" + data[i][j - 1];
            sheet.rows.add(t);
    	}
    	retVal.add(sheet);
    	
    	// Create summary
    	sheet = new TSheet();
        sheet.label = "Summary";
        sheet.isSummary = true;

        // Add formulas for each resident type
        String[] t = new String[sheet.columns.length + 2];
        t[0] = "1";
        for(int j = 1; j < t.length - 1; j++) {
            t[j] = generateSumFormula(month, j + 1);
        }
        
        // Formula for Total
        t[t.length - 1] = "SUM(B2:B5)";
        sheet.rows.add(t);
        
        retVal.add(sheet);
    	
    	return retVal;
    }
    
    /**
     * Generates a formula to sum the columns B - AF of a specific row
     * 
     * @param sheet The sheet name
     * @param row   The row to be summed
     * @return      A string representing the formula
     */
    public static String generateSumFormula(String sheet, int row) {
    	StringBuilder formula = new StringBuilder();
    	formula.append("SUM(");
    	
    	// Append columns B - Z
    	for (int i = 0; i < 25; i++) {
    		formula.append(sheet + "!" + (char)(66 + i) + row + ",");
    	}
    	
    	// Append columns AA - AF
    	formula.append(sheet + "!AA" + row + ",");
    	formula.append(sheet + "!AB" + row + ",");
    	formula.append(sheet + "!AC" + row + ",");
    	formula.append(sheet + "!AD" + row + ",");
    	formula.append(sheet + "!AE" + row + ",");
    	formula.append(sheet + "!AF" + row + ")");
    	
    	return formula.toString();
    }
}
