/*
 * Author: Andrew Bloch-Hansen
 */

/* Comparator for Integers */
//package imagesegment;

public class ReverseIntegerComparator implements Comparator{
    
    public int compare(Object a, Object b) throws ClassCastException{
	Integer aComp = (Integer) a;
	Integer bComp = (Integer) b;
        
	if ( aComp.intValue() < bComp.intValue() ) return(1);
        else if  (aComp.intValue() == bComp.intValue() ) return(0);
	else return(-1);
    };  
}

