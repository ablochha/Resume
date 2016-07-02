package datastructures;

/**
 * ReverseDoubleComparator.java
 * 
 * Course: CS4445
 * Author: Andrew Bloch-Hansen
 * 
 * Comparator strategy for doubles, in reverse. 
 *
 */
public class ReverseDoubleComparator implements Comparator {
	
	// Constructor
    public ReverseDoubleComparator() {
    	
    	
    } //end DoubleComparator
    
    public int compare(Object a, Object b) throws ClassCastException {
    	
    	Double aComp = (Double) a;
    	Double bComp = (Double) b;
        
    	if (aComp.intValue() < bComp.intValue()){
    		
    		return (1);
    		
    	} //end if
    	
        else if (aComp.intValue() == bComp.intValue()) {
        	
        	return (0);
        	
        } //end else if
        
        else {
        	
        	return (-1);
        	
        } //end else
    	
    }; //end compare
    
} //end ReverseDoubleComparator
