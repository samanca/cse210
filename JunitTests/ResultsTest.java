import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.turnstile.Results;

/**
 * 
 */

/**
 * @author tsli
 *
 */
public class ResultsTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("\nSETUP CLASS RUNNING -- Nothing to do though");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("\nTEARDOWN CLASS RUNNING -- Nothing to do though");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		System.out.println("\nSETUP IS RUNNING -- Nothing to do though");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		System.out.println("TEARDOWN IS RUNNING -- Nothing to do though");
	}

	/**
	 * Test method for {@link com.turnstile.Results#analyze(int, int, int[], int)}.
	 */
	@Test
	public final void testAnalyze1() {
		 System.out.println("testAnalyze1");
	     Boolean expResult = false;
	     String expErrMsg = "Error: Multiple resident types, day 1, sheet 1, line 0.";
	     int[] lineArray = {1,1,1,1,0};
		 Results.analyze(1, 1, lineArray, 0);
	     List<String> errmsgs = Results.getErrmsgs();
	     boolean empty = errmsgs.isEmpty();
		 assertEquals(expResult, empty);
		 assertEquals(expErrMsg, errmsgs.get(0));
	}
	
	/**
	 * Test method for {@link com.turnstile.Results#analyze(int, int, int[], int)}.
	 */
	@Test
	public final void testAnalyze2() {
		 System.out.println("testAnalyze2");
		 Results.getErrmsgs().clear();
	     Boolean expResult = true;
	     int[] lineArray = {0,0,0,1,0};
		 Results.analyze(1, 1, lineArray, 1);
	     List<String> errmsgs = Results.getErrmsgs();
	     boolean empty = errmsgs.isEmpty();
		 assertEquals(expResult, empty);
	}
	
	/**
	 * Test method for {@link com.turnstile.Results#analyze(int, int, int[], int)}.
	 */
	@Test
	public final void testAnalyze3() {
		 System.out.println("testAnalyze3");
		 Results.getErrmsgs().clear();
		 String expectedErrMsg = "Error: No resident type, day 1, sheet 1, line 1.";
	     Boolean expResult = false;
	     int[] lineArray = {0,0,0,0,0};
		 Results.analyze(1, 1, lineArray, 1);
	     List<String> errmsgs = Results.getErrmsgs();
	     boolean empty = errmsgs.isEmpty();
		 assertEquals(expResult, empty);
		 assertEquals(expectedErrMsg, errmsgs.get(0));
	}
	
	/**
	 * Test method for {@link com.turnstile.Results#analyze(int, int, int[], int)}.
	 */
	@Test
	public final void testAnalyze4() {
		 System.out.println("testAnalyze4");
		 Results.getErrmsgs().clear();
		 String expectedErrMsg = "Error: Multiple resident types, day 1, sheet 1, line 1.";
	     Boolean expResult = false;
	     int[] lineArray = {1,1,0,0,0};
		 Results.analyze(1, 1, lineArray, 1);
	     List<String> errmsgs = Results.getErrmsgs();
	     boolean empty = errmsgs.isEmpty();
		 assertEquals(expResult, empty);
		 assertEquals(expectedErrMsg, errmsgs.get(0));
	}
	
	/**
	 * Test method for {@link com.turnstile.Results#analyze(int, int, int[], int)}.
	 */
	@Test
	public final void testAnalyze5() {
		 System.out.println("testAnalyze5");
		 Results.getErrmsgs().clear();
		 String expectedErrMsg = "Boxes checked error.";
	     Boolean expResult = false;
	     int[] lineArray = {1,1,1,1,1,0};
		 Results.analyze(1, 1, lineArray, 1);
	     List<String> errmsgs = Results.getErrmsgs();
	     boolean empty = errmsgs.isEmpty();
		 assertEquals(expResult, empty);
		 assertEquals(expectedErrMsg, errmsgs.get(0));
	}
}
