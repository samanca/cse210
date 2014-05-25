package turnstile;

import java.util.LinkedList;
import java.util.List;


/* Linda Pescatore CSE 210 Project Turnstile
 * This class looks at the boxes checked on a line and determines whether to
 *  increment the appropriate resident type or log an error.
 *  There is also a method to print a day's results.
 *  I made an enumeration of resident types but didn't use it.
 */

public class Results {
	
	static int[][] tallies = new int[32][5];  
	static List<String> errmsgs = new LinkedList<String>();

  enum ResTypes {Interim, Permanent, Alumnus, Nonresident, HVRP, None}
	

	public static void analyze(int date, int page, int[] lineArray, int lineNumber){
	/* preconditions:
	 *  date between 1 and 31
	 *  page between 1 and 9
	 *  lineArray includes 1's and 0's only
	 *    for each resident type + HVRP
	 * postconditions:
	 *  results matrix incremented when lineArray includes one and only one 1.
	 * invariants: 
	 *  date, page, lineArray unchanged
	 *  results never below 0
	 */
		int boxesChecked = 0;
		int checkedBox = 9;
		int boxes = lineArray.length - 1; // except hvrp
		int it;
		String errmsg;

		// check each box in the line to find boxes that are checked.
		for ( it = 0; it < boxes; it++) {
			if (lineArray[it] == 1) {
				boxesChecked++;
				checkedBox = it;
			} // end if
		} // end for
		
		// increment tallies iff boxesChecked = 1, else add errmsg to list
		switch(boxesChecked) {
		case 0:
			errmsg = "Error: No resident type, day " + date + ", sheet " + page + ", line " + lineNumber + ".";
			errmsgs.add(errmsg);
			break;
		case 1:
			tallies[date][checkedBox]+=1;
			break;
		case 2:
		case 3:
			errmsg = "Error: Multiple resident types, day " + date + ", sheet " + page + ", line " + lineNumber + ".";
			errmsgs.add(errmsg);
			break;
		case 4:
			errmsg = "Line erased? All resident types marked, day " + date + ", sheet " + page + ", line " + lineNumber + ".";
			errmsgs.add(errmsg);
			break;
		default:
			errmsg = "Boxes checked error."; // should be unreachable
			break;
		} // end switch
			
		// increment hvrp (last value of lineArray
		if (lineArray[boxes]==1) {
			tallies[date][boxes] += 1;
		}
		
	} // end analyze
	
	protected void printDayTotals(int date) {
		/* preconditions:
		 *  date between 1 and 31
		 *  results object exists
		 * postconditions:
		 *  prints day's counts broken down by resident type.
		 *  prints day's errors.
		 * invariants: 
		 *  date, data unchanged
		 */

		for (int x = 0; x < 5; x++) {
			System.out.println("Resident type " + x + " total: " + tallies[date][x] + " on day " + date);
		}
		for (String err : errmsgs) {
			System.out.println(err);
		}
	}

} // end class
		
