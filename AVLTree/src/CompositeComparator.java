/*
 * Author: Andrew Bloch-Hansen
 */

public class CompositeComparator implements Comparator{
	
	//Compares two composite keys, first by frequency, then alphabetically
	public int compare(Object a, Object b) throws ClassCastException
	{
		
		int aFrequency, bFrequency;
		String aWord, bWord;
		
		//get the frequencies from the two composite keys
		aFrequency = ((Composite)a).frequency();
		bFrequency = ((Composite)b).frequency();
		
		//get the words from the two composite keys
		aWord = ((Composite)a).word();
		bWord = ((Composite)b).word();
		
		//If the frequencies are different
		if (aFrequency != bFrequency)
		{
			
			//If the first frequency is greater than the second
			if (aFrequency > bFrequency)
				
				return 1;
			
			//If the first frequency is less than the second
			else if (aFrequency < bFrequency)
				
				return -1;
			
		} //end if
		
		//If the frequencies are the same
		else 
		{
			
			//If the first word comes before the second
			if (aWord.compareTo(bWord) > 0)
				
				return 1;
			
			//If the first word comes after the second
			else if (aWord.compareTo(bWord) < 0)
				
				return -1;
			
		} //end else
		
		//The keys are the same
		return 0;
		
	} //end compare

} //end CompositeComparator
