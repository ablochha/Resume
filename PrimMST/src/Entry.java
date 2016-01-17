package asn3;

/**
 * This class represents an entry to go into the heap. These entries
 * also store their position in the heap.
 * 
 * @author Andrew Bloch-Hansen
 */
public class Entry<Key,Value> implements Position {
	
	/**
	 * k contains the key of the entry. 
	 */
	private Key k;
	
	/**
	 * v contains the value of the entry.
	 */
	private Value v;
	
	/**
	 * location contains the position in the heap.
	 */
	private int location;
 
	/**
	 * This constructor initializes the entries key, value, and position in the heap.
	 * @param K the key of the entry
	 * @param V the value of the entry
	 * @param loc the position in the heap
	 */
	public Entry(Key K, Value V, int loc) {
	 
		k = K;
		v = V;
		location = loc;
     
	} //end Entry
 
	/**
	 * Returns the key of the entry.
	 * @return the key
	 */
	public Key getKey() {
	 
		return k;
	 
	} //end getKey
 
	/**
	 * Returns the value of the entry.
	 * @return the value
	 */
	public Value getValue() {
	 
		return v;
	 
	} //end getValue
 
	/**
	 * Changes the value to a new value.
	 * @param newV the new value
	 */
	public void changeValue(Value newV) {
	
		v = newV;
	 
	} //end changeValue
 
	/**
	 * Change the key to a new key.
	 * @param newK the new key
	 */
	public void changeKey(Key newK) {
	 
		k = newK;
	 
	} //end changeKey
 
	/**
	 * Returns the location of the vertex in the heap.
	 * @return the location in the heap
	 */
	public int getLocation() {
	 
		return location;
	 
	} //end getLocation
 
	/**
	 * Sets the location of the vertex in the heap.
	 * @param l the location of the vertex in the heap
	 */
	public void setLocation(int l) {
	 
		location = l;
	 
	} //end setLocation
 
	/**
	 * Returns the position of the vertex in the heap.
	 */
	public int givePosition() {
	 
		return(location);
     
	} //end givePosition
 
} //endEntry