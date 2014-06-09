package com.turnstile;

import java.util.ArrayList;

public class TSheet {
    public ArrayList<String[]> rows;
    public String[] columns = {
            "Interim",
            "Perm",
            "Alumni",
            "Nonresident",
            "HVRP",
            "Indeterminate"
    };
    public String label;
    public boolean isSummary = false;

    public TSheet() {
        rows = new ArrayList<String[]>();
    }
    
    /**
     * Generates the TSheet with the data corresponding to a month and also
     * generates the TSheet containing the summary of the data.
     * 
     * @param data  The 32x6 array of data for a specific month
     * @param month The name of the month the data represents
     * @return      An ArrayList containing the mentioned TSheets
     */
    public static ArrayList<TSheet> generateMonth(int[][] data, String month) {
    	ArrayList<TSheet> retVal = new ArrayList<TSheet>(2);
    	TSheet sheet = new TSheet();
        sheet.label = month;
        sheet.isSummary = false;
    	
    	for (int i = 1; i < data.length; i++) {
    		String[] t = new String[sheet.columns.length + 1];
            t[0] = String.valueOf(i);
            for(int j = 1; j < t.length - 1; j++)
                t[j] = "" + data[i][j];
            t[t.length - 1] = "" + data[i][0];
            sheet.rows.add(t);
    	}
    	retVal.add(sheet);
    	
    	// Create summary
    	sheet = new TSheet();
        sheet.label = "Summary";
        sheet.isSummary = true;
        
        int errors = 0;
        // Add up the errors for incorrect dates
        for (int i = 0; i < data[0].length; i++) {
            errors += data[0][i];
        }

        // Add formulas for each resident type
        String[] t = new String[sheet.columns.length + 2];
        t[0] = "1";
        for(int j = 1; j < t.length - 1; j++) {
            t[j] = generateSumFormula(month, j, 2, 32);
            
            if (j == t.length - 2) {
                t[j] = errors + "+" + t[j];
            }
        }

        // Formula for Total
        t[t.length - 1] = "SUM(B2:B5, B7)";
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
    public static String generateSumFormula(String sheet, int column, int startRow, int endRow) {
    	StringBuilder formula = new StringBuilder();
    	formula.append("SUM(");
    	
    	// Append rows
    	for (int i = startRow; i < endRow; i++) {
    		formula.append(sheet + "!" + (char)(65 + column) + i + ",");
    	}
    	
    	// Append last row
    	formula.append(sheet + "!" + (char)(65 + column) + endRow + ")");
    	
    	return formula.toString();
    }
}
