/*
 * Author: Andrew Bloch-Hansen
 */

import java.util.LinkedList;
import java.util.Iterator;
import java.util.Vector;

public class ImageSegment {

	private static Graph<Pixel> graph;				//graph data structure
	private static ImageReader imageReader;			//reads the image file
	private static int imWidth, imHeight;			//dimensions of the image
	private static Vector<Vertex<Pixel>> lookup;	//pairing of image pixel and associated vertex object
	
	//This method reads the pixels from an image, and stores them in the graph data structure
	private static void readImage(String inFileName) throws Exception
	{
		 
		//Check to make sure the filename works
    	try
    	{
    		
    		imageReader = new ImageReader(inFileName);
    		
    	} //end try
    	
    	catch (Exception e)
    	{
    		
    		System.out.println(e);
    		System.exit(0);
    		
    	} //end try
    	
    	//Create the image reader
    	imageReader = new ImageReader(inFileName);    	
    	imWidth = imageReader.getWidth();
    	imHeight = imageReader.getHeight();
    	
    	//Create the Vector of pixel/vertex pairs
    	lookup = new Vector<Vertex<Pixel>>(imWidth*imHeight);
    	lookup.setSize(imWidth*imHeight);
    	
    	//Create the graph data structure
    	graph = new Graph<Pixel>();
    	
    	//Loop through the picture, adding each pixel as a vertex to the graph
    	for (int x = 0; x < imWidth; x++)
    	{
    		
    		for (int y = 0; y < imHeight; y++)
    		{
    			
    			//Insert the pixel into the graph, and store a copy in the lookup vector
    			Pixel pixelObj = new Pixel(x, y, imageReader.getRed(x, y), imageReader.getGreen(x, y), imageReader.getBlue(x, y));
    			lookup.setElementAt(graph.insertVertex(pixelObj), x + y * imWidth);
    			
    		} //end for
    		
    	} //end for
    	
    	//Add horizontal edges
    	for (int x = 1; x < imWidth; x++)
    	{
    		
    		for (int y = 0; y < imHeight; y++)
    		{
    			
    			//Try to insert an edge
    			try
    			{
    				int weight = Math.abs(imageReader.getRed(x, y) - imageReader.getRed(x-1, y)) + Math.abs(imageReader.getBlue(x, y) - imageReader.getBlue(x-1, y)) + Math.abs(imageReader.getGreen(x, y) - imageReader.getGreen(x-1, y));
    				graph.insertEdge(lookup.elementAt((x - 1) + y * imWidth), lookup.elementAt(x + y * imWidth), weight);
    				
    			} //end try
    			
    			catch (Exception e)
    			{
    				
    				System.out.println(e);
    				
    			} //end catch
    			
    		} //end for
    		
    	} //end for
    	
    	//Add vertical edges
    	for (int x = 0; x < imWidth; x++)
    	{
    		
    		for (int y = 1; y < imHeight; y++)
    		{
    			
    			//Try to insert an edge
    			try
    			{
    				
    				int weight = Math.abs(imageReader.getRed(x, y) - imageReader.getRed(x, y-1)) + Math.abs(imageReader.getBlue(x, y) - imageReader.getBlue(x, y-1)) + Math.abs(imageReader.getGreen(x, y) - imageReader.getGreen(x, y-1));
    				graph.insertEdge(lookup.elementAt(x + (y - 1) * imWidth), lookup.elementAt(x + y * imWidth), weight);
    				
    			} //end try
    			
    			catch (Exception e)
    			{
    				
    				System.out.println(e);
    				
    			} //end catch
    			
    		} //end for
    		
    	} //end for
    	
	} //end readImage
	
	//This method creates a minimum spanning tree over the graph, and breaks k-1 most dissimilar edges
	private static void segmentImage(int k) throws Exception
	{
		
		//Create the minimum spanning tree, and look through its edges
		Iterator<Edge<Pixel>> itMST = graph.MST();
    	HeapPQ<Integer, Edge<Pixel>> heap = new HeapPQ<Integer, Edge<Pixel>>(graph.getNumVertices(), new ReverseIntegerComparator());
    	
    	//Loop while there are more edges to look at
    	while (itMST.hasNext())
    	{
    		
    		//insert all edges into a heap that uses a reverse integer comparator
    		Edge<Pixel> e = itMST.next();
    		heap.insert(e.getWeight(), e);
    		
    	} //end while
    	
    	//Create k segments by deleting k-1 most dissimilar edges 
    	for (int i = 0; i < k-1; i++)
    	{
    		
    		//Try to delete an edge
    		try
    		{
    			
    			graph.deleteEdge(heap.removeMin());    	
    			
    		} //end try
    		
    		catch (GraphException e)
    		{
    			
    			System.out.println(e);
    			
    		} //end catch    			
    		
    	} //end for
    	
	} //end segmentImage
	
	//This method calculates each segments average color, and repaints the whole segment as the average color
	private static void repaintSegments(String outFileName)
	{
		
		//Get the list of segments and associated vertices
		Iterator<Iterator<Vertex<Pixel>>> it = graph.ConnectedComponents();
		LinkedList<Vertex<Pixel>> vertices;
		
		int countSegment = 0;
		
		//Loop through each segment
		while (it.hasNext())
    	{
    		
			countSegment++;
			
			//Get the list of all the vertices
    		Iterator<Vertex<Pixel>> comp = it.next();
    		vertices = new LinkedList<Vertex<Pixel>>();
    		
    		//Variables for calculating the average color
    		int redValue = 0;
    		int blueValue = 0;
    		int greenValue = 0;
    		int countPixels = 0;
    		
    		//Loop through a segment's vertices to calculate average color
    		while (comp.hasNext())
    		{
    			
    			//Add the color values to running sums
    			countPixels++;
    			Vertex<Pixel> vertexObj = comp.next();
    			Pixel pixelObj = vertexObj.getObject();
    			vertices.add(vertexObj);
    			redValue += pixelObj.getRed();
    			blueValue += pixelObj.getBlue();
    			greenValue += pixelObj.getGreen();    			
    			
    		} //end while
    		
    		//Calculate the average color values
    		int avgRed = redValue/countPixels;
    		int avgBlue = blueValue/countPixels;
    		int avgGreen = greenValue/countPixels;
    		
    		//Loop through a segment's vertices to paint them the average color
    		for (int i = 0; i < vertices.size(); i++)
    		{
    			
    			Vertex<Pixel> v = vertices.get(i);
    			
    			//Calculate the x,y coordinates of a pixel from the lookup object
    			int index = lookup.indexOf(v);
    			int x = index % imWidth;
    			int y = (index - x) / imWidth;
    			
    			//Paint the pixel with the average color of the segment
    			imageReader.setColor(x, y, avgRed, avgGreen, avgBlue);
    			
    		} //end for
   
    		System.out.println("Segment " + countSegment + " of size " + countPixels + ": Red " + avgRed + " Green " + avgGreen + " Blue " + avgBlue);
    		
    	} //end while
		
		System.out.println("Number of segments is " + countSegment);
		
		//Try to save the image
		try
		{
			
			imageReader.saveImage(outFileName);
			
		} //end try
		
		catch (Exception e)
		{
			
			System.out.println(e);
			
		} //end catch
		
	} //end repaintSegments
	
	//This method checks to make sure the program was called correctly
	private static void checkUsage(String args[])
	{
		
		//check if this program is being used correctly
    	if (args.length != 3 ) 
    	{
    		 
    		System.out.println("Usage: ImageSegment in.jpg out.jpg k");
    		System.exit(0);
            
        } //end if
		
	} //end checkUsage
	
	//This program reads an image from a file, segments the pixels based on similarity, then repaints segments with the average color
	public static void main(String args[]) throws Exception
	{		
    	
		checkUsage(args);		
       	readImage(args[0]);
    	segmentImage(Integer.parseInt(args[2]));
    	repaintSegments(args[1]);    	
    	
	} //end main
	
} //end ImageSegment