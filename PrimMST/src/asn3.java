package asn3;

import java.io.*;
import java.util.Iterator;

/**
 * This program reads a graph from a text file, and computes the minimum spanning tree using
 * Prim's algorithm.
 * 
 * @author Andrew Bloch-Hansen
 */
public class asn3 {

	/**
	 * The main method will first read the graph data from a file, build a undirected graph, and
	 * finally compute the MST using Prim's algorithm.
	 * @param args input commands
	 * @throws Exception when can't find file
	 */
	public static void main(String[] args) throws Exception {
		
		//Create the graph data structure
		Graph<Integer> graph = new Graph<Integer>();
    	
		int ascii;	//ascii code of the character being read from a file
		int size;
		
		BufferedInputStream inFile = new BufferedInputStream(null);
		String fileName = "mst_graph1.txt";
		
		try {
	          
			inFile  = new BufferedInputStream(new FileInputStream(fileName));           
	           	   
	    } //end try
	        
		// catch exceptions caused by file input/output errors
	    catch (IOException e) {
	    
	    	System.out.println("Check your file name");
	    	System.exit(0);
	           
	    } //end catch 
		
		StringBuilder str = new StringBuilder();
		ascii = inFile.read();
		
		//read the number of vertices
		while (ascii != 13) {
			
			str.append(ascii-48);
			ascii = inFile.read();			
			
		} //end while
		
		size = Integer.valueOf(str.toString());		
		System.out.println("Input Graph:");
		System.out.println("The total number of vertices in the input graph: " + size);
		
		ascii = inFile.read();
		ascii = inFile.read();
		
		//Loop while we are not at the end of file
		while (ascii != -1) {
			
			str = new StringBuilder();
			int i;
			int j;
			int w;
			
			//read the first vertex
			while (ascii != 32) {
				
				str.append(ascii-48);
				ascii = inFile.read();		
				
			} //end while
				
			i = Integer.valueOf(str.toString());
			System.out.print("Edge from vertex " + i + " ");
			
			//skip spaces
			while (ascii == 32)				
				
				ascii = inFile.read();
				
			str = new StringBuilder();
			
			//read the second vertex
			while (ascii != 32) {
				
				str.append(ascii-48);
				ascii = inFile.read();		
				
			} //end while
			
			j = Integer.valueOf(str.toString());
			System.out.print("to vertex " + j + ", ");
			
			//skip spaces
			while (ascii == 32)
				
				ascii = inFile.read();
			
			str = new StringBuilder();
	
			//read the weight
			while (ascii > 47) {
				
				str.append(ascii-48);
				ascii = inFile.read();		
				
			} //end while
				
			w = Integer.valueOf(str.toString());
			System.out.println("with a weight of " + w + ".");
						
			//Skip end of line and new line characters
			if (ascii == 13) {
						
				ascii = inFile.read();
				ascii = inFile.read();
						
			} //end if			
			
			//Try to insert an edge
			try {
						
				graph.insertEdge(graph.insertVertex(i), graph.insertVertex(j), w);
				
			} //end try
			
			catch (Exception e) {
				
				System.out.println(e);
				
			} //end catch
							
		} //end while
				
		inFile.close();
		
		System.out.println("\nVertices: " + graph.getNumVertices() + ", Edges: " + graph.getNumEdges());
		System.out.println("\nMinimum Spanning Tree:");
		
		Iterator<Edge<Integer>> itMST = graph.MST();	
		
		//Traverse the edges in the MST and output them to the screen
		while (itMST.hasNext()) {
			
			Edge<Integer> edge = itMST.next();
			System.out.println("Edge from vertex " + edge.getEndPoint1().getObject() + " to vertex " + edge.getEndPoint2().getObject() + ", with a weight of " + edge.getWeight() + ".");
			
		} //end while
		
	} //end main
		
} //end asn3