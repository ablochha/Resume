/*
 * Author: Andrew Bloch-Hansen
 */

import java.io.*;
import java.util.Iterator;
 
public class Spell {
	
	//These are the dictionary's to be used in the spellchecking
	private static HashDictionary D,S;
	private static ListDictionary L1, L2;
	
	//These are the initial values for the hashDictionary
	private static StringHashCode emptyStringHashCode = new StringHashCode();
	private static float loadFactor = (float) 0.5;
	
	//This counts the number of words in the first file read, the dictionary
	private static int numberOfWordsInDictionary = 0;
	
	//These variables are used in the list implementation, to let the Linkedlist run, set 'toggle' to true
	private static boolean listImplementation = false;
	private static boolean listImplementationToggle = false;	//set to true for linked list	
	
	//This function performs the letter Substitution modifications
	private static void letterSubstitution(StringBuffer sb)
	{
	
		//This variable helps manipulate each word
		StringBuffer temp; 
		
		//for every letter in the word
		for (int i = 0; i < sb.length(); i++)
		{
			
			//create a copy of the original word to modify
			temp = new StringBuffer(sb.toString());
			
			//for every other character in the alphabet
			for (int j = 0; j <25; j++ )
			{
			  		
				//after z, a will be tried next
				if (temp.charAt(i) == 'z')
			 	{
			  			
					temp.setCharAt(i, (char)((int)'z' - 26));
			  			
				} //end if
			  		
				//change the letter
				temp.setCharAt(i, (char)(temp.charAt(i)+1));
				
				//If the hashtable finds this new word			  		
				if ((!listImplementation()) && (checkHashDictionaryForElement(temp.toString())))
				{
			  		
					//try to add this new word to the hashtable
					try
					{
							 
						S.insert(temp.toString());
							 	
					} //end try
						 
					catch (DictionaryException e) 
					{
							 
						System.out.println(e);
							 
					} //end catch
			  			
			  	} //end if
				
				//If the linked list finds this new word
				if ((listImplementation()) && (checkListDictionaryForElement(temp.toString())))
				{
					
					//try to add this new word to the linked list
					try
					{
						
						L2.insert(temp.toString());
						
					} //end try
					
					catch (DictionaryException e)
					{
						
						System.out.println(e);
						
					} //end catch
					
				} //end if
			  		
			} //end for
			  
		} //end for	
		
		return;
		
	} //end letterSubstitution
	
	//This function performs the letter omission modifications
	private static void letterOmission(StringBuffer sb)
	{
		//This variable helps manipulate each word
		StringBuffer temp;
		
		//for every letter in the word
		for (int i = 0; i < sb.length(); i++)
		{
			
			//copy the original word and delete a letter
			temp = new StringBuffer(sb.toString());
			temp.deleteCharAt(i);
			
			//If the hashtable finds this new word
			if ((!listImplementation()) && (checkHashDictionaryForElement(temp.toString())))
			{
				
				//try to add this new word to the hashtable
				try
				{
					
					S.insert(temp.toString());
					
				} //end try
				
				catch (DictionaryException e)
				{
					
					System.out.println(e);
					
				} //end catch
				
			} //end if
			
			//If the linked list finds this new word
			if ((listImplementation()) && (checkListDictionaryForElement(temp.toString())))
			{
				
				//try to add this new word to the linked list
				try
				{
					
					L2.insert(temp.toString());
					
				} //end try
				
				catch (DictionaryException e)
				{
					
					System.out.println(e);
					
				} //end catch
				
			} //end if
			
		} //end for
		
		return;
		
	} //end letterOmission
	
	//This function performs the letter insertion modifications
	private static void letterInsertion(StringBuffer sb)
	{
		
		//This variable helps manipulate each word
		StringBuffer temp;
		
		//for every letter in the word
		for (int i = 0; i <= sb.length(); i++)
		{
			
			//for every other character in the alphabet
			for (char j = 'a'; j <= 'z'; j++)
			{
				
				//create a copy of the original word and insert a new letter
				temp = new StringBuffer(sb.toString());
				temp.insert(i, j);
				
				//If the hashtable finds this new word
				if ((!listImplementation()) && (checkHashDictionaryForElement(temp.toString())))
				{
					
					//try to add this new word to the hashtable
					try
					{
						
						S.insert(temp.toString());
						
					} //end try
					
					catch (DictionaryException e)
					{
						
						System.out.println(e);
						
					} //end catch
					
				} //end if
				
				//If the linked list finds this new word
				if ((listImplementation()) && (checkListDictionaryForElement(temp.toString())))
				{
					
					//try to add this new word to the linked list
					try
					{
						
						L2.insert(temp.toString());
						
					} //end try
					
					catch (DictionaryException e)
					{
						
						System.out.println(e);
						
					} //end catch
					
				} //end if
				
			} //end for
			
		} //end for
		
		return;
		
	} //end letterInsertion
	
	//This function performs the letter reversal modifications
	private static void letterReversal(StringBuffer sb)
	{
		
		//This variable helps manipulate each word
		StringBuffer temp;
		
		//for every letter in the word
		for (int i = 0; i < sb.length()-1; i++)
		{
			
			//swap two adjacent letters
			temp = new StringBuffer(sb.toString());
			temp.setCharAt(i, sb.charAt(i + 1));
			temp.setCharAt(i + 1,  sb.charAt(i));			
			
			//If the hashtable finds this new word
			if ((!listImplementation()) && (checkHashDictionaryForElement(temp.toString())))
			{
				
				//try to add this new word to the hashtable
				try
				{
					
					S.insert(temp.toString());
					
				} //end try
				
				catch (DictionaryException e)
				{
					
					System.out.println(e);
					
				} //end catch
				
			} //end if	
			
			//If the linked list finds this new word
			if ((listImplementation()) && (checkListDictionaryForElement(temp.toString())))
			{
				
				//try to add this new word to the linked list
				try
				{
					
					L2.insert(temp.toString());
					
				} //end try
				
				catch (DictionaryException e)
				{
					
					System.out.println(e);
					
				} //end catch
				
			} //end if	
			
		} //end for
		
		return;
		
	} //end letterReversal
	
	//This function fills the our dictionary structure with words from a file
	private static void readWordsFromDictionary(String dictionaryFileName) throws java.io.IOException
	{
		
		//Declare the variables to read from a file
		BufferedInputStream dictionary;  
		FileWordRead readDictionaryWords;
		
		//Declare the variable to look at each individual word
		String nextWord;
		
		//check that the filename is correct
        try
        {
          
           dictionary  = new BufferedInputStream(new FileInputStream(dictionaryFileName));           
           	   
        } //end try
        
        catch (IOException e) // catch exceptions caused by file input/output errors
        {
       	
           System.out.println("Check your file name");
           System.exit(0);
           
        } //end catch 
        
        //Initialize our dictionary data structures
        D = new HashDictionary(getEmptyStringHashCode(), getLoadFactor());
        L1 = new ListDictionary();
        
        //start reading from the dictionary file
        dictionary = new BufferedInputStream(new FileInputStream(dictionaryFileName));
		readDictionaryWords = new FileWordRead(dictionary);
		
		//keep reading while the dictionary has more words to read
		while ((!listImplementation()) && (readDictionaryWords.hasNextWord())) 
		{
			 
			//count how many words in total
			incrementNumberOfWordsInDictionary();
			
			//grab the next word
			nextWord = readDictionaryWords.nextWord();
			 
			//try to insert this word into the hashtable
			try
			{
				
				D.insert(nextWord);
				
				 
			} //end try
			 
			catch (DictionaryException e) 
			{
				 
				System.out.println(e);
				 
			} //end catch
			
		} //end while
		
		//keep reading while the dictionary has more words to read
		while ((listImplementation()) && (readDictionaryWords.hasNextWord())) 
		{
			
			//grab the next word
			nextWord = readDictionaryWords.nextWord();
			 
			//try to insert this word into the linked list
			try
			{
				
				L1.insert(nextWord);
				
				 
			} //end try
			 
			catch (DictionaryException e) 
			{
				 
				System.out.println(e);
				 
			} //end catch
			
		} //end while
		
		return;
		
	} //end readWordsFromDictionary
	
	//This function spell checks the second file using the first file
	private static void spellCheckFileToCheck(String fileToCheckName) throws java.io.IOException
	{
		
		//Variables to read from a file
		BufferedInputStream fileToCheck;
		FileWordRead readSpellCheckWords;
		
		//Variables to spellcheck a word
		StringBuffer sb;
		String nextWord;
		
		//check that the second file is correct
		try
        {
          
			fileToCheck  = new BufferedInputStream(new FileInputStream(fileToCheckName));       
			
        } //end try
        
        catch (IOException e) // catch exceptions caused by file input/output errors
        {
       	
           System.out.println("Check your file name");
           System.exit(0);
           
        } //end catch  
		
		//start reading from the second text file
		fileToCheck = new BufferedInputStream(new FileInputStream(fileToCheckName));
		readSpellCheckWords = new FileWordRead(fileToCheck);
		
		//let the hashtable spellcheck all the words in the second file
		while ((!listImplementation()) && (readSpellCheckWords.hasNextWord()))
		{
			
			nextWord = readSpellCheckWords.nextWord();
			
			//create a new hashdictionary for each individual word
			S = new HashDictionary(getEmptyStringHashCode(), getLoadFactor());
			
			//perform word modifications if it isn't found in the dictionary
			if (!checkHashDictionaryForElement(nextWord))
			{
				  
				sb = new StringBuffer(nextWord);
				  
				//perform the four spellchecking methods
				letterSubstitution(sb);	
				letterOmission(sb);
				letterInsertion(sb);
				letterReversal(sb);	
				
				//print out all the potential correct spellings
				printCandidateCorrectSpellings(nextWord);
					  
			} //end if	
						  
		} //end while
		
		//let the linkedlist spellcheck all the words in the second file
		while ((listImplementation()) && (readSpellCheckWords.hasNextWord()))
		{
			
			nextWord = readSpellCheckWords.nextWord();
			
			//create a new linkedlist for each individual word
			L2 = new ListDictionary();
			
			//perform word modifications if it isn't found in the dictionary
			if (!checkListDictionaryForElement(nextWord))
			{
				
				sb = new StringBuffer(nextWord);
				
				//perform the four spellchecking methods
				letterSubstitution(sb);	
				letterOmission(sb);
				letterInsertion(sb);
				letterReversal(sb);	
				
				//print out all the potential correct spellings
				printCandidateCorrectSpellings(nextWord);
				
			} //end if
						  
		} //end while
		
		return;
		
	} //end spellCheckFileToCheck
	
	//Accessor method for loadfactor
	private static float getLoadFactor()
	{
		
		return loadFactor;
		
	} //end getLoadFactor

	//Accessor method for stringhashcode
	private static StringHashCode getEmptyStringHashCode()
	{
		
		return emptyStringHashCode;
		
	} //end getEmptyStringHashCode
	
	//Determines if the linkedlist or hashtable is spellchecking
	private static boolean listImplementation()
	{
		
		if (listImplementation == false)
			
			return false;
		
		else
			
			return true;
				
	} //end listImplementation
	
	//Determines if the users wants the linkedlist to run
	private static boolean listImplementationToggle()
	{
		
		if (listImplementationToggle == false)
			
			return false;
		
		else
			
			return true;
		
	} //end listImplementationToggle
	
	//Setter method for the linkedlist implementation (if its toggled on)
	private static void setListImplementationTrue()
	{
		
		listImplementation = true;
		return;
		
	} //end setListImplementation

	//calls the find method for the hashdictionary
	private static boolean checkHashDictionaryForElement(String key)
	{
		
		if (D.find(key))
			
			return true;
		
		else
			
			return false;
		
	} //end checkHashDictionaryForElement

	//calls the find method for the linkedlist
	private static boolean checkListDictionaryForElement(String key)
	{
		
		if (L1.find(key))
			
			return true;
		
		else
			
			return false;
		
	} //end checkListDictionaryForElement
	
	//Counts the number of words added from the first input file
	private static void incrementNumberOfWordsInDictionary()
	{
		
		numberOfWordsInDictionary++;
		return;
		
	} //end incrementNumberOfWordsInDictionary
	
	//Returns number of words added from the first input file
	private static int getNumberOfWordsInDictionary()
	{
		
		return numberOfWordsInDictionary;
		
	} //end getNumberOfWordsInDictionary
	
	//Prints out the potentially correct spellings for a word
 	private static void printCandidateCorrectSpellings(String incorrectlySpeltWord)
	{
		
 		//Allows us to traverse the list of potentially correct spellings
		Iterator<String> it;
		String candidateCorrectSpelling;
		
		System.out.print(incorrectlySpeltWord + "  => ");
		
		//the hashtable will output the potentially correct spellings
		if (!listImplementation())
		{
			
			//if there are no potential correct spellings
			if (S.size() == 0)
			{
				
				System.out.println("You are a hopeless speller!");
				
			} //end if
			
			else if (S.size() > 0)
			{
				
				it = S.elements();
				while (it.hasNext()) 
				{
						  
					candidateCorrectSpelling = it.next();
					System.out.print(candidateCorrectSpelling);
					
					if (it.hasNext())
						
						System.out.print(", ");
					
					else
						
						System.out.println("");
						  
				} //end while
				
			} //end else
			
		} //end if
		
		//the linkedlist will output the potentially correct spellings
		else if (listImplementation())
		{
			
			//if there are no potential correct spellings
			if (L2.size() == 0)
			{
				
				System.out.println("You are a hopeless speller!");
				
			} //end if
			
			else if (L2.size() > 0)
			{
				
				it = L2.elements();
				while (it.hasNext()) 
				{
						  
					candidateCorrectSpelling = it.next();
					System.out.print(candidateCorrectSpelling);
					
					if (it.hasNext())
						
						System.out.print(", ");
					
					else
						
						System.out.println("");
						  
				} //end while
				
			} //end else
			
		} //end if		
		
	} //end printCandidateCorrectSpellings
	
 	//Main method takes to command line arguments, and attempts to spellcheck the second against the first
    public static void main(String[] args) throws java.io.IOException
    { 	        
           
    	//check if this program is being used correctly
    	if (args.length != 2 ) 
    	{
    		 
    		System.out.println("Usage: spell dictionaryFile.txt inputFile.txt ");
    		System.exit(0);
            
        } //end if
    	
    	//variables used to time the dictionaries
    	long hashDictionaryStartTime, hashDictionaryFinishTime;
    	long listDictionaryStartTime, listDictionaryFinishTime;
    	
    	//Let the hashdictionary spellcheck
    	hashDictionaryStartTime = System.currentTimeMillis();    	       
        readWordsFromDictionary(args[0]); 
        spellCheckFileToCheck(args[1]);                 
        hashDictionaryFinishTime = System.currentTimeMillis();
        
        System.out.println("\nThe Hashtable has finished spellchecking");
        
        //check if the user wants the linkedlist to spellcheck
        if (listImplementationToggle())
        {
        	
        	//lets the linkedlist spellcheck
        	System.out.println("\nNow the Linkedlist is spellchecking\n");
        	setListImplementationTrue();
        	listDictionaryStartTime = System.currentTimeMillis();               
            readWordsFromDictionary(args[0]); 
            spellCheckFileToCheck(args[1]);        
            listDictionaryFinishTime = System.currentTimeMillis();
            System.out.println("\nThe Linkedlist implementation took " + (listDictionaryFinishTime-listDictionaryStartTime) + " miliseconds to complete");
            
        } //end if            
        
        System.out.println("The Hashtable implementation took " + (hashDictionaryFinishTime-hashDictionaryStartTime) + " miliseconds to complete");
        System.out.println("The total size of the dictionary is " + getNumberOfWordsInDictionary() + " words\n");
        
    } //end main
    
} //end Spell