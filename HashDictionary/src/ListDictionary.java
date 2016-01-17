/*
 * Author: Andrew Bloch-Hansen
 */

import java.util.Iterator;
import java.util.LinkedList;

public class ListDictionary implements Dictionary 
{
	
	private LinkedList<String> table;
	private int numberOfElements;

	//create a new linked list
	public ListDictionary() {
		
		table = new LinkedList<String>();		
		
	} //end ListDictionary
	
	//insert a key into the listdictionary
	public void insert(String key) throws DictionaryException
	{
		
		if (find(key))
			
			throw new DictionaryException("The word '" + key + "' is already in the dictionary");
		
		else
			
			table.add(key);
			incrementNumberOfElements();
		
	} //end insert
	
	//find a key in the listdictionary
	public boolean find(String key) {
		
		Iterator<String> it = table.listIterator(0);
		
		while (it.hasNext())
		{
			
			String entry = it.next();
			
			if (entry.equals(key))
			{
				
				return true;
				
			} //end if
		
		} //end while
		
		return false;
		
	} //end find
	
	//return an iterator of the listdictionary
	public Iterator<String> elements() 
	{
		
		return table.listIterator(0);
				
	} //end Iterator
	
	//remove a key from the listdictionary
	public void remove(String key) throws DictionaryException{
		
		Iterator<String> it = table.listIterator(0);
		
		while (it.hasNext())
		{
			
			String entry = it.next();
			
			if (entry.equals(key))
			{
				
				table.remove(entry);
				decrementNumberOfElements();
				return;
				
			} //end if
			
		} //end while
		
		throw new DictionaryException("The word '" + key + "' was not found in the dictionary.");
		
	} //end remove	
	
	//increase the number of keys in the dictionary
	private void incrementNumberOfElements()
	{
		
		numberOfElements++;
		return;
		
	} //end incrementNumberOfElements
	
	//decrease the number of keys in the dictionary
	private void decrementNumberOfElements()
	{
		
		numberOfElements--;
		return;
		
	} //end decrementNumberOfElements()
	
	//get the total number of keys in the dictionary
	public int size()
	{	
			
		return numberOfElements;
		
	} //end size
	
} //end ListDictionary
