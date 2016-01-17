/*
 * Author: Andrew Bloch-Hansen
 */

import java.io.*;
import java.util.Iterator;

public class FindKeyWords {
	
	//AVL trees for part1-3, and the final tree for alphabetical output
	private static AVLTree tree1, tree2, tree3, tree4;
	
	//Comparators for various objects
	private static StringComparator stringComp = new StringComparator();
	private static CompositeComparator compositeComp = new CompositeComparator();
		
	//This function fills our first AVL tree with the frequencies and words from the first file
	private static void readWordsFromFile(String fileName) throws java.io.IOException
	{
		
		//Declare the variables to read from a file
		BufferedInputStream file;  
		FileWordRead readFileWords;
				
		//Declare the variable to look at each individual word
		String nextWord;
				
		//check that the filename is correct
		try
		{
		          
			file  = new BufferedInputStream(new FileInputStream(fileName));           
		           	   
		} //end try
		        
		catch (IOException e) // catch exceptions caused by file input/output errors
		{
		       	
			System.out.println("Check your file name");
			System.exit(0);
		           
		} //end catch 
		        
		//Initialize our AVL tree data structure
		tree1 = new AVLTree(stringComp);
		
		//Set up the word reader
		file = new BufferedInputStream(new FileInputStream(fileName));
		readFileWords = new FileWordRead(file);
		
		//keep reading while the file has more words to read
		while (readFileWords.hasNextWord()) 
		{
			
			//grab the next word
			nextWord = readFileWords.nextWord();
			 
			//try to insert this word into the AVLtree
			try
			{
				
				tree1.insertNew(nextWord, 1);				
				 
			} //end try
			 
			catch (AVLtreeException e) 
			{
				 
				//System.out.println(e);
				 
			} //end catch
			
		} //end while
		
	} //end readWordsFromFile
	
	//This function sorts our word/frequency entries by frequency
	private static void reSortWords()
	{
		
		//Initialize our AVL tree data structure
		tree2 = new AVLTree(compositeComp);
		
		//Allows us to traverse the contents of the AVL Tree
    	Iterator<DictEntry> it;    	
    	it = tree1.inOrder();
    	
    	//keep reading while the list has more entries
		while (it.hasNext())
		{
			
			//Grab an entry, and isolate its components
			DictEntry entry = it.next();
			String key = (String)entry.key();
			int value = Integer.valueOf(String.valueOf(entry.value()));
			
			//Create a composite key for the next tree
			Composite compositeKey = new Composite(key, value);
			
			//try to insert this entry into the AVLtree
			try
			{
				
				tree2.insertNew(compositeKey, 0);				
				 
			} //end try
			 
			catch (AVLtreeException e) 
			{
				 
				//System.out.println(e);
				 
			} //end catch
			
		} //end while
    	
	} //end reSortWords
	
	//This function removes all but the top k frequencies that are not common words
	private static void removeFrequentWords(int k, String fileName) throws java.io.IOException
	{
		
		//Declare the variables to read from a file
		BufferedInputStream file;  
		FileWordRead readFileWords;
						
		//Declare the variable to look at each individual word
		String nextWord;
						
		//check that the filename is correct
		try
		{
				          
			file  = new BufferedInputStream(new FileInputStream(fileName));           
				           	   
		} //end try
				        
		catch (IOException e) // catch exceptions caused by file input/output errors
		{
				       	
			System.out.println("Check your file name");
			System.exit(0);
				           
		} //end catch 
				        
		//Initialize our AVL tree data structure
		tree3 = new AVLTree(stringComp);
			
		//Set up our word reader
		file = new BufferedInputStream(new FileInputStream(fileName));
		readFileWords = new FileWordRead(file);
				
		//keep reading while the file has more words to read
		while (readFileWords.hasNextWord()) 
		{
				
			//grab the next word
			nextWord = readFileWords.nextWord();
					 
			//try to insert this word into the AVLtree
			try
			{
				
				tree3.insertNew(nextWord, 0);				
						 
			} //end try
					 
			catch (AVLtreeException e) 
			{
					 
				//System.out.println(e);
						 
			} //end catch
					
		} //end while
		
		//Grab a list of the top k frequencies from our first file
		Iterator<DictEntry> it;    	
    	it = tree2.findnLargestKeys(k);
    	tree4 = new AVLTree(stringComp);
    	
    	//Keep reading while the list has more entries
    	while (it.hasNext())
		{
    	
    		//Isolate the components of the entry
    		DictEntry entry = it.next();
    		Composite compositeKey = (Composite)entry.key();
    		String key = (String)compositeKey.word();
    		int frequency = (int)compositeKey.frequency();
    		
    		//if this word is not a common english word
    		if (tree3.find(key) == null)
    		{
    			
    			//build a new tree consisting of the keywords we want
    			try
    			{
    				
    				tree4.insertNew(key, frequency);				
    						 
    			} //end try
    					 
    			catch (AVLtreeException e) 
    			{
    					 
    				//System.out.println(e);
    						 
    			} //end catch
    			
    		} //end if
    		
		} //end while
    	
    	
    	//Check if we have any uncommon keywords
    	if (tree4.size() == 0)
    	
    		System.out.println("No keywords");
    	
    	//Print out the uncommon keywords
    	else
    	{
    		
    		Iterator<DictEntry> it2;    	
        	it2 = tree4.inOrder();
        	
        	while (it2.hasNext())
    		{
        		
        		DictEntry entry2 = it2.next();
    			String stringKey2 = (String)entry2.key();    			
    			int frequency2 = Integer.valueOf(String.valueOf(entry2.value()));
    			System.out.println(stringKey2 + " " + frequency2);
    			
    		} //end while
    		
    	} //end else 	
		
	} //removeFrequentWords	
	
	//Main method takes 3 arguments from the command line, to grab the k most frequent uncommon words from a file
	public static void main(String[] args) throws java.io.IOException
	{
		
		//check if this program is being used correctly
    	if (args.length != 3 ) 
    	{
    		 
    		System.out.println("Usage: FindKeyWords k file.txt MostFrequentEnglishWords.txt ");
    		System.exit(0);
            
        } //end if
    	
    	//Part1: read a file and get frequencies of words, Part2: resort by frequency, Part3: Output k uncommon keywords
    	readWordsFromFile(args[1]);
    	reSortWords();
    	removeFrequentWords(Integer.parseInt(args[0]), args[2]);   	
    	
	} //end main
	
} //end FindKeyWords