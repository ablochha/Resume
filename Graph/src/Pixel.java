/*
 * by olga veksler
 */

//package imagesegment;

public class Pixel {
    private int x;
    private int y;
    
    private int redColor;   
    private int greenColor;
    private int blueColor;
     
    public Pixel(int inX, int inY, int red, int green, int blue){
	x = inX;
	y = inY;
        redColor = red;
        greenColor = green;
        blueColor = blue;        
   }
    public int getX(){ return x; }
    public int getY(){ return y; }
    public int getRed(){ return redColor; }
    public int getGreen(){ return greenColor; }
    public int getBlue(){ return blueColor; }
}
