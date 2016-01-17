package asn3;

/**
 * This interface supplies a easy-to-use comparator.
 * 
 * @author Andrew Bloch-Hansen
 */
public class IntegerComparator implements Comparator {
	
	/**
	 * Empty constructor.
	 */
	public IntegerComparator() {
	  
	  
	} //end IntegerComparator
 
	/**
	 * This method will compare two objects using the rules of Integers.
	 * @param a the first object
	 * @param b the second object
	 * @return the value indicating which was bigger
	 */
	public int compare(Object a, Object b) throws ClassCastException {
		
		Integer aComp = (Integer) a;
		Integer bComp = (Integer) b;
      
		if (aComp.intValue() < bComp.intValue()) 
			
			return(-1);
		
		else if (aComp.intValue() == bComp.intValue()) 
			
			return(0);
		
		else 
			
			return(1);
		
	} //end compare
	
} //end IntegerComparator