package datastructures;

/**
 * DoubleComparator.java
 * 
 * Course: CS4445
 * Author: Andrew Bloch-Hansen
 * 
 * Comparator strategy for doubles. 
 *
 */
public class DoubleComparator implements Comparator {
	
	// Constructor
    public DoubleComparator() {
    	
    	
    } //end DoubleComparator
   
    public int compare(Object a, Object b) throws ClassCastException {
    	
    	Double aComp = (Double) a; 
    	Double bComp = (Double) b;
        
    	if (aComp.intValue() < bComp.intValue()) {
    		
    		return (-1);
    		
    	} //end if
    	
        else if (aComp.intValue() == bComp.intValue()) {
        	
        	return (0);
        	
        } //end else if
    	
        else {
        	
        	return (1);
        	
        } //end else
    	
    }; //end compare
    
} //end DoubleComparator
 