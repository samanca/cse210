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
}
