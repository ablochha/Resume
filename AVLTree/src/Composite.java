/*
 * Author: Andrew Bloch-Hansen
 */

public class Composite {

	private String key;
	private int frequencyCount;
	
	//Constructor for a composite key
	public Composite(String word, Integer frequency)
	{
		
		key = word;
		frequencyCount = frequency;
		
	} //end Composite
	
	//Returns the frequency from the composite key
	public Integer frequency()
	{
		
		return frequencyCount;
		
	} //end frequency
	
	//Returns the word from the composite key
	public String word()
	{
		
		return key;
		
	} //end word
	
} //end Composite
