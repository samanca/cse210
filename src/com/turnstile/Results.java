package com.turnstile;

import java.util.LinkedList;
import java.util.List;


/* Linda Pescatore CSE 210 Project Turnstile
 * This class looks at the boxes checked on a line and determines whether to
 *  increment the appropriate resident type or log an error.
 *  There is also a method to print a day's results printDayTotals.
 */

public class Results {
	
	//set debug to true to turn on print msgs
	static boolean DEBUG = false;
	
	// create 31x6 matrix. the rows correspond to days of a month. they won't all be filled. 
	// the columns correspond to resident types in the order of the Enum ResTypes below.
	// NOW THIS IS 32X6 TO ACCOMMODATE BAD DATES (index 0)
	static int[][] tallies = new int[32][6];  
	private static List<String> errmsgs = new LinkedList<String>();

	public enum ResTypes {ERROR, INTERIM, PERM, ALUMNUS, NONRESIDENT, HVRP}

	public static void analyze(int date, int page, int[] lineArray, int lineNumber){
	/* preconditions:
	 *  date between 1 and 31
	 *  page between 1 and 9
	 *  lineArray includes 1's and 0's only
	 *    for each resident type + HVRP
	 * postconditions:
	 *  results matrix incremented when lineArray includes one and only one 1.
	 *  data for dates outside 1-31 thrown into date 0. 
	 * invariants: 
	 *  date, page, lineArray unchanged
	 *  results never below 0
	 */
		
		// all bad dates become zero
		if ((date < 1) || (date > 31)) 	date = 0;
		
		int boxesChecked = 0;
		int checkedBox = 0;
		int boxes = lineArray.length - 1; // look at all boxes except hvrp
		int box;
		String errmsg;

		// check each box in the line to find those that are checked.
		for ( box = 0; box < boxes; box++) {
			if (lineArray[box] == 1) {
				boxesChecked++;
				checkedBox = box;
			} // end if
		} // end for
		
		// increment tallies iff boxesChecked = 1, else add errmsg to list
		switch(boxesChecked) {
		case 0:	// Name filled in but no box checked
			tallies[date][ResTypes.ERROR.ordinal()] += 1;  // increment the error resident type 
			errmsg = "Error: No resident type, day " + date + ", sheet " + page + ", line " + lineNumber + ".";
			getErrmsgs().add(errmsg);
			if (DEBUG) System.out.println(errmsg);
			break;
		case 1:	// Name filled in and 1 box checked; increment that 
			tallies[date][checkedBox]+=1;
			if (DEBUG) System.out.println("Incremented " + ResTypes.values()[checkedBox]);
			break;
		case 2:	// Name filled in and 2, 3, or 4 boxes checked
		case 3:
		case 4:
			tallies[date][ResTypes.ERROR.ordinal()]+=1;
			errmsg = "Error: Multiple resident types, day " + date + ", sheet " + page + ", line " + lineNumber + ".";
			getErrmsgs().add(errmsg);
			if (DEBUG) System.out.println(errmsg);
			break;
		default: // should be unreachable
			errmsg = "Internal error.";
			getErrmsgs().add(errmsg);
			break;
		} // end switch

		// HVRP increment hvrp  when last value of lineArray == 1
		// 'boxes' is defined above as length of the lineArray (all boxes on line)
		if (lineArray[boxes]==1) {
			tallies[date][boxes] += 1;
		}
		
	} // end analyze
	
	public void printDayTotals(int date) {
		/* preconditions:
		 *  date between 1 and 31
		 *  results object exists
		 * postconditions:
		 *  prints day's counts broken down by resident type.
		 *  prints day's errors.
		 * invariants: 
		 *  date, data unchanged.....
		 */

		for (int x = 0; x < ResTypes.values().length; x++) {
			System.out.println("Resident type " + ResTypes.values()[x] + " total: " + tallies[date][x] + " on day " + date);
		}
		for (String err : getErrmsgs()) {
			System.out.println(err);
		}
	}

	
	public static List<String> getErrmsgs() {
		return errmsgs;
	}

} // end class
		
