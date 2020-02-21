/*
 * Andrew Bloch-Hansen
 * 250473076
 */

import java.io.*;

public class asn2 {
	
	//Counting sort performed on the frequency field of a Data object
	public static Data[] countingSort(Data[] A) {		
		
		Data[] B = new Data[A.length];
		int[] C;
		
		//Initialize data structure
		for (int i = 0; i < B.length; i++) {
			
			B[i] = new Data(0,i);
			
		} //end for
		
		//Compute the largest integer in our array	
		int maxValue = A[1].getFrequency(); 		
	    for (int i = 2; i < A.length; i++) {
	    		
	    	if (A[i].getFrequency() > maxValue) {  
	    		
	    		maxValue = A[i].getFrequency();  
	    			
	    	} //end for
	    		
	    } //end for	      
		
	    C = new int[maxValue+1];
	    
	    //Initialize the frequency array
	    for (int i = 0; i < maxValue; i++) {
	    	
	    	C[i] = 0;
	    	
	    } //end for
	    
	    //Create frequency array
		for (int i = 0; i < A.length; i++) {
			
			C[A[i].getFrequency()]++;
			
		} //end if
		
		//Create cumulative frequency array
		for (int i = 1; i <= maxValue; i++) {
			
			C[i] = C[i] + C[i-1];
			
		} //end for
		
		//Sort by cumulative frequency
		for (int i = A.length-1; i >= 1; i--) {
			
			B[C[A[i].getFrequency()]-1].setFrequency(A[i].getFrequency());
			B[C[A[i].getFrequency()]-1].setLabel(A[i].getLabel());
			C[A[i].getFrequency()] = C[A[i].getFrequency()]-1;
			
		} //end for
		
		return B;
		
	} //end if
	
	public static void main(String[] args) throws Exception {
			
		TreeNode[] forest;		//list of nodes in the forest
		UnionFind unionFind;	//the unionfind datastructure
		int ascii;				//ascii code of the character being read from a file
		int count = 0;			//size of unionfind
		int finalNumSets;		//number of connected components
		Data[] frequency;		//array holding size of each component
		BufferedInputStream inFile = new BufferedInputStream(null);
		String fileName = "girl.txt";
		
		try {
          
           inFile  = new BufferedInputStream(new FileInputStream(fileName));           
           	   
        } //end try
        
        catch (IOException e) // catch exceptions caused by file input/output errors
        {
       	
           System.out.println("Check your file name");
           System.exit(0);
           
        } //end catch 
		    
		StringBuilder str = new StringBuilder();
		ascii = inFile.read();
		
		//Loop while we are not at the end of file
		while (ascii != -1) {
			
			//Skip end of line and new line characters
			if (ascii == 13) {
				
				ascii = inFile.read();
				ascii = inFile.read();
				System.out.println("");
				
			} //end if
			
			//build a string of the input, and show the input to the screen
			str.append((char)ascii);
			System.out.print((char)ascii);
			ascii = inFile.read();
			count++;
			
		} //end while
		
		inFile.close();
		
		//Initialize union find data structure to the size of the forest indicated by file
		unionFind = new UnionFind(count);
		forest = new TreeNode[count];		
		System.out.println("");
		
		//Add every character from the file to our datastructure
		for (int i = 0; i < count; i++) {
			
			int up = 71;	//offset to check north neighbour
			int left = 1;	//offset to check west neighbour
			
			//Put the character into the datastructure
			forest[i] = new TreeNode(str.substring(i, i+1), i);
			unionFind.makeSet(forest[i]);
			 
			//Check if a character is connected to its west neighbour
			if (((i%up) > 0) && (str.substring(i, i+1).equals("+")) && (str.substring(i-left, (i-left)+1).equals("+"))) {
				
				//Make sure they aren't already connected
				if (unionFind.findSet(forest[i]) != unionFind.findSet(forest[i-left])) {
				
					unionFind.unionSets(forest[i], forest[i-left]);
					
				} //end if
								
			} //end if
			
			//Check if a character is connected to its north neighbour
			if ((i >= up) && (str.substring(i, i+1).equals("+")) && (str.substring(i-up, (i-up)+1).equals("+"))) {
				
				//Make sure they aren't already connected
				if (unionFind.findSet(forest[i]) != unionFind.findSet(forest[i-up])) {
					
					unionFind.unionSets(forest[i], forest[i-up]);
					
				} //end if
								
			} //end else if			
			
		} //end for
		
		//Count connected components
		finalNumSets = unionFind.finalSets();
		frequency = new Data[finalNumSets+1];
		frequency[0] = new Data(0,0);
		for (int i = 1; i < frequency.length; i++) {
			
			frequency[i] = new Data(1, i);
			
		} //end for
		
		//Compute size of each connected component, and relabel each node to the same across a component
		for (int i = 0; i < count; i++) {
			
			//Change all nodes that are +'s
			if (!(forest[i].getEntry().equals(" ")) && !(forest[i].getEntry().matches(".*\\d.*"))) {
				
				int rep = Integer.parseInt(unionFind.findSet(forest[i]).getEntry());
				frequency[rep].incrementFrequency();
				char c = (char)(rep+96);
				forest[i].setEntry(String.valueOf(c));				
				System.out.print(forest[i].getEntry());
				
			} //end if		
			
			else {
				
				System.out.print(forest[i].getEntry());
				
			} //end else
			
			if (i%71 == 0 && i > 0) {
				
				System.out.println();
				
			} //end if
			
		} //end for
		
		//Sort component sizes using counting sort, use Data object so we don't lose track of what component it was
		frequency = countingSort(frequency);
		System.out.println("\n\n");
		for (int i = frequency.length-1; i > 1; i--) {
			
			System.out.println("Component label: " + frequency[i].getLabel() + "(" + (char)(frequency[i].getLabel()+96) + "), Size: " + frequency[i].getFrequency());
		
		} //end for
				
	} //end main
	
} //end asn2
