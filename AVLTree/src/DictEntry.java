/*
 * Author: Andrew Bloch-Hansen
 */

public class DictEntry {
	
	private Object key, value;

	//Constructor for DictEntry
	public DictEntry(Object key, Object value)
	{
		
		this.key = key;
		this.value = value;
		
	} //end DictEntry
	
	//Returns the key from the entry
	public Object key()
	{
		
		return this.key;
		
	} //end key
	
	//Returns the value from the entry
	public Object value()
	{
		
		return this.value;
		
	} //end value
	
} //end DictEnry
