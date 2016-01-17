/*
 * by olga veksler
 */

/* This class is for reading from a JPEG file. See its description in the Assignment5.pdf handout */
//package imagesegment;

import java.io.File;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;


public class ImageReader {
    private int pixels[] = null;  
    private int imageWidth;
    private int imageHeight;
    
    /** Opens an image for reading. Will throw Exception if file does not exist */
    public ImageReader(String fileName) throws Exception{
        Image image = Toolkit.getDefaultToolkit().getImage(fileName);
        MediaTracker mediaTracker = new MediaTracker(new Container());
        mediaTracker.addImage(image, 0);
        mediaTracker.waitForID(0);
    
        imageWidth = image.getWidth(null);
        imageHeight = image.getHeight(null);

	if ( imageWidth < 0 || imageHeight < 0 ) throw new Exception("Can't read image file");
    
        pixels = new int[imageWidth*imageHeight];
        PixelGrabber pg = new PixelGrabber(image, 0, 0, imageWidth, imageHeight, pixels, 0, imageWidth);
        try { 
            pg.grabPixels(); 
        }
        catch (InterruptedException e)  { 
            System.out.println("ImageReadError");
            System.exit(0);
        }
    }
    
    /* Returns image height */
    public int getHeight(){
        return(imageHeight);
    }
    
    /* Returns image width */
     public int getWidth(){
        return(imageWidth);
    }
    /* Sets color of pixel (x,y). 0<=y<=imageHeight, 0<=x<=imageWidth */
    public void setPixel(int x, int y, int color)
    {
        pixels[y*imageWidth+x] = color;
    }
    /* Returns red color component of pixel (x,y) */
    public int getRed(int x, int y)
    {
       return((pixels[x+y*imageWidth] & 0x00ff0000) >> 16);
    } 

    /* Returns green color component of pixel (x,y) */
    public int getGreen(int x, int y)
    {
       return((pixels[x+y*imageWidth] & 0x0000ff00) >> 8);
    }  

    /* Returns blue color component of pixel (x,y) */
    public int getBlue(int x, int y)
    {
       return(pixels[x+y*imageWidth] & 0x000000ff);
    }  
       
    /* Sets color of pixel (x,y) to (red, green, blue) */
    public void setColor(int x, int y, int red, int green, int blue)
    {
        pixels[y*imageWidth+x] =  (255 << 24) | (red << 16) | (green << 8) | blue;
    }
    
    private static BufferedImage toBufferedImage(Image src) {
        int w = src.getWidth(null);
        int h = src.getHeight(null);
        int type = BufferedImage.TYPE_INT_RGB;  // other options
        BufferedImage dest = new BufferedImage(w, h, type);
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
        return dest;
    }

    private static void save(BufferedImage image, String ext, String fileName) {
        
        File file = new File(fileName);
        try {
            ImageIO.write(image, ext, file);  // ignore returned boolean
        } catch(IOException e) {
            System.out.println("Write error for " + file.getPath() +
                               ": " + e.getMessage());
        }
    }
    
   /* saves image to a file with given name*/
    public void saveImage (String name) throws java.io.FileNotFoundException,java.io.IOException
    {
     /* Open the files for reading */
        BufferedImage segmImage = new BufferedImage(imageWidth,
                                imageHeight, BufferedImage.TYPE_INT_RGB);
   
        for (int i = 0; i < imageHeight; i++ )
            for (int j= 0; j < imageWidth; j++ )
                segmImage.setRGB(j,i, pixels[i*imageWidth+j]);
   
       // ImageIO.write(segmImage, "jpeg", name);
        save(segmImage,"jpg",name);
     
        
    } 
}
