package com.turnstile;


import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;



public class ImageProcess { 	
	
	//Handles processing the image, right now it does only one image at a time
//	public static int[][] processImage(Mat Image) changed return value to Results -lp
	public static Results processImage(Mat Image)

	{
		//first we need to check the rotation of an image and where the alignment box is
		Image = chkRotate(Image);
		//Get day number
		int day = getDay(Image);
		//Get sheet number
		int sheet = getSheet(Image);
		Results results = new Results();
		
//		int[][] DATA = new int[32][5];         -lp
		
		//find the data for each line on the sign in sheet
		for(int i = 0; i<20; i++)
		{
			int xpos = 47;
			int ypos = i*21 + 127;
			//We need to determine if a line has a name on it before we check for line data
			if(meanArea(Image,xpos,ypos,xpos+161,ypos+21) <= 240) //was 220, got no results
			{
				int[] lineData = getLine(Image,ypos);
				Results.analyze(day, sheet, lineData, i); 
			}
		}
		
		
		//Here is where we need to do handling of the results of line data
		//For errors I suggest making an error class/object with the values [int day,int page,int line,string error]
		//and then making a queue or stack or linked list of these errors.
		
		//Until this is done this module will only output a data int matrix
		//When that is done we will need a class of [double[][] data, error ERRORqueue] for output
		
		// Mostly for debugging; un-comment to see day's totals
		//results.printDayTotals(day);
		
//		return DATA;                  chgd to return results (both tallies and errors) -lp
		return results;
	}


	
	public static int[] getLine(Mat Image, int ypos)
	{
		//Loops through the user information and determines if the value is below a threshold that we have set
		//if it is below the threshold then we consider it to be a valid scan
		int[] lineInfo = new int[5];
		for(int i = 0; i < 4; i++)
		{
			int xpos = i*55 + 229;
			if (meanArea(Image,xpos,ypos,xpos+15,ypos+15) < 200)
				lineInfo[i] = 1;
			else
				lineInfo[i] = 0;
		}
		
		//We do the same for HVRP
		if(meanArea(Image,873,ypos,888,ypos+15) < 200)
			lineInfo[4] = 1;
		else
			lineInfo[4] = 0;
		
		return lineInfo;
	}
	
	public static int getSheet(Mat Image)
	{
		//Finds the sheet number by checking the average pixel value of all the associated boxes
		//and then finds which position had the minimum sheet number
		double[] sheetNo = new double[9];
		for(int i = 0; i < 9; i++)
		{
			int xpos = i*22 + 718;
			int ypos = 58;
			sheetNo[i] = meanArea(Image,xpos,ypos,xpos+8,ypos+8);
		}
		return(min(sheetNo)+1);
	}
	
	
	public static int getDay(Mat Image)
	{
		//method to get the day information, we need to scan both the month boxes and the day boxes
		double[] month= new double[4];
		double[] day = new double[10];
		for(int i = 0; i < 4; i++)
		{
			int xpos = i*30 + 488;
			int ypos = 63;
			month[i]= meanArea(Image,xpos,ypos,xpos+7,ypos+7);
		}
		for(int i = 0; i < 10; i++)
		{
			int xpos = i*25 + 488;
			int ypos = 79;
			day[i] = meanArea(Image,xpos,ypos,xpos+7,ypos+7);
		}
		
		//Since the boxes are small and the quality of the compression isn't very good
		//taking the scanned box with the most black pixels and assume thats the correct box (for now)
		int tempD = min(day);
		int tempM = min(month);
		if (tempD == 9)
			tempD = 0;
		
		return (tempD + 1 + tempM*10);
	}
	

	public static int min(double[] arr)
	{
		//Loops through our array containing the average scanned value and finds which box was the lowest
		double minVal = 257;
		int minLoc = 0;
		for(int i = 0; i < arr.length; i++)
			if(arr[i] <= minVal)
			{
				minVal = arr[i];
				minLoc = i;
			}
		return minLoc;
	}
	
	public static Mat chkRotate(Mat Image)
	{
		
		//Check to see if the image is landscape or portrait
		if(Image.width() < Image.height())
		{	
			//If its portrait we perform a transpose and a rotation depending on the alignment box location
			Image = Image.t();
			double value = meanArea(Image, 102, 34, 112, 45);
			if(value < 150)
				Core.flip(Image, Image, 0);
			else
				Core.flip(Image, Image, 1);
		}//If its its already landscape then we a rotation depending on alignment box
		else if( meanArea(Image, 895,34, 906, 46) > 150)
		{
			Core.flip(Image, Image, -1);
		}
		
		return Image;
	}
	

	 static public double meanArea(Mat Image, int uLX, int uLY, int lRX, int lRY)
	 {
		//computes the average pixel value in the image matrix, closer to 255 is white where lower is more black
		 double test = 0;
		 int count = 0;
		 for(int i = uLX; i <= lRX; i++)
			 for(int j = uLY; j<= lRY; j++)
			 {
				 test += Image.get(j,i)[0];
				 count++;
			 }
		 
		 return(test/count);
	 }
	 
	 public static void main( String[] args )
	   {
		 //Simple dummy pain for testing, it loads an image on my desktop for processing
	      System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	      Mat m = Highgui.imread("C:/Users/linda/Desktop/signin1.jpg");
	      Imgproc.cvtColor(m, m, Imgproc.COLOR_RGB2GRAY);
	      
	      processImage(m); 
	      
	      
	   }
}
