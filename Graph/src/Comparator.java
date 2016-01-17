/*
 * by olga veksler
 */

/* Comparator Interface.  Has to have only one method, the method used for comparison */

//package imagesegment;
public interface Comparator{
  
    /* compare returns negative integer if a < b, 0 if a = b, and positive integer if a > b */
    public int compare(Object a, Object b) throws ClassCastException;  
}

