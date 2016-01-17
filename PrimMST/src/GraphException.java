package asn3;

/**
 * This class outputs error messages depending on what goes wrong.
 * 
 * @author Andrew Bloch-Hansen
 */
public class GraphException extends Exception {

	public GraphException()
	{
		
		super();
		
	} //end GraphException	
	
	public GraphException(String message)
	{
		
		super(message);
		
	} //end GraphException
	
	public GraphException(String message, Throwable reason)
	{
		
		super(message, reason);
		
	} //end GraphException
	
	public GraphException(Throwable reason)
	{
		
		super(reason);
		
	} //end GraphException
	
} //end GraphException