public class Triangle {

    private String side1;
    private String side2;
    private String side3;

    public Triangle() {
        this("0", "0", "0");
    }
    
    public Triangle(String s1, String s2, String s3) {
        this.side1 = s1;
        this.side2 = s2;
        this.side3 = s3;
    }
    
    public String determineTriangleType() {
        
        String type = "";
        
        // This code runs when the user pushes the button
        int s1 = -1, s2=-1, s3 = -1;
        String err = "";
        try {
            s1 = Integer.parseInt(side1);
        } catch (NumberFormatException e) {
           err += "Side 1 is not a number!\n";
        }
        try {
            s2 = Integer.parseInt(side2);
        } catch (NumberFormatException e) {
           err += "Side 2 is not a number!\n";
        }
        try {
            s3 = Integer.parseInt(side3);
        } catch (NumberFormatException e) {
           err += "Side 3 is not a number!\n";
        }
        
        if (s1 <= 0 || s2 <= 0 || s3 <= 0) {
               err += "At least one length is less than 0!\n";            
        }
        
        if ((s1 + s2 <= s3) || (s1 + s3 <= s2) || (s2 + s3 <= s1)) {
               err += "The lengths of the triangles do not form a valid triangle!\n";
        }  
        
        if (s1 + s2 + s3 > 1000000) {
            err += "I feel your triangle is too big\n";
        } 
        
        if (err.length() > 0) {
           type = err;   
        } else {            
            if ((s1 == s2) && (s2 == s3)) {
               type= "Equilateral";
           } else if (( s1 == s2) || (s2 == s3) || (s1 == s3)) {
               type = "Isosceles";
           } else {
               type = "Scalene";
           }                 
        }
        
        return type;        
    }
    
    public static void main(String[] args) {
        Triangle t = new Triangle("15", "15", "15");
        System.out.println("t is:"+t.determineTriangleType());
    }
}
