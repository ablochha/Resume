package asn3;

/**
 * This interface supplies a easy-to-use comparator.
 * 
 * @author Andrew Bloch-Hansen
 */
public interface Comparator{

  /* compare returns negative integer if a < b, 0 if a = b, and positive integer if a > b */
  public int compare(Object a, Object b) throws ClassCastException;  
  
} //end Comparator