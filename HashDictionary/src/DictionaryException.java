/*
 * Author: Andrew Bloch-Hansen
 */

public class DictionaryException extends Exception{

	public DictionaryException()
	{
		
		super();
		
	} //end DictionaryException	
	
	public DictionaryException(String message)
	{
		
		super(message);
		
	} //end DictionaryException 
	
	public DictionaryException(String message, Throwable reason)
	{
		
		super(message, reason);
		
	} //end DictionaryException
	
	public DictionaryException(Throwable reason)
	{
		
		super(reason);
		
	} //end DictionaryException
	
} //end DictionaryException
