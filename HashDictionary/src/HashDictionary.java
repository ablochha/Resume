/*
 * Author: Andrew Bloch-Hansen
 */

import java.util.Iterator;
import java.util.LinkedList;

public class HashDictionary implements Dictionary {
	
	//array-based hashtable, of type string
	private String[] table;
	
	//value that holds the density of the array
	private float loadFactor;			
	private StringHashCode hashCode;
	
	//Variables N and Q for the hashtable
	private int primeHashTableSizeN = 7;
	private int primeHashTableVariableQ = 5;
	
	//Variables used in the MAD compression function
	private int MADVariableA = 1;
	private int MADVariableB = 0;
	
	//empty marker that we use for the remove function
	private String AVAILABLE;
	
	//total number of elements currently stored in the hashtable
	private int numberOfElements = 0;
	
	//Variables for determining the hashtable efficiency
	private int numOps = 0;
	private int numProbes = 0;			
	
	//Constructor that we dont use, throws exception if called
	public HashDictionary() throws DictionaryException 
	{
		
		throw new DictionaryException("Incorrect call to HashDictionary");
		
	} //end HashDictionary
	
	//Constructor that sets up the HashDictionary
	public HashDictionary(HashCode inputCode, float inputLoadFactor) 
	{
		
		//Initialize the hashtable
		hashCode = (StringHashCode) inputCode;
		loadFactor = inputLoadFactor;
		table = new String[getPrimeHashTableSizeN()];
		AVAILABLE = "qwerty";
		initializeMADVariables();	
		
	} //end HashDictionary
	
	//Randomly generate MAD variable's a and b
	private void initializeMADVariables()
	{
		
		//a>0 and a mod N != 0
		setMADVariableA((int)(Math.random()*Integer.MAX_VALUE));
		
		while ((getMADVariableA() <= 0) || ((getMADVariableA() % getPrimeHashTableSizeN()) == 0))
		{
			
			setMADVariableA((int)(Math.random()*Integer.MAX_VALUE));
			
		} //end while		
		
		//b >= 0
		setMADVariableB((int)(Math.random()*Integer.MAX_VALUE));
		
		while (getMADVariableB() < 0)
		{
			
			setMADVariableB((int)(Math.random()*Integer.MAX_VALUE));
			
		} //end while	
		
	} //end initializeMADVariables
	
	//Perform the MAD Compression Function on the Poly value, using double hashing
	private int MADCompressionFunction(int PolynomialAccumulationValue, int j)
	{
		//h(k)
		int h1 = (Math.abs((getMADVariableA()*PolynomialAccumulationValue) + getMADVariableB()) % getPrimeHashTableSizeN());
		
		//h'(k)
		int h2 = getPrimeHashTableVariableQ() - (h1 % getPrimeHashTableVariableQ());
		
		//hashed index
		return ((h1 + j * h2) % getPrimeHashTableSizeN());
		
	} //end MADCompressionFunction
	
	//insert a key into the hashtable
	public void insert(String key) throws DictionaryException 
	{
		
		incrementNumberOfOperations();
		
		//check if our loadfactor is acceptable still
		if (getCurrentLoadFactor() >= getMaximumAcceptableLoadFactor())
		{
			
			//The loadfactor is too large, make the hashtable bigger
			reSizeHashTable();
			
		} //end if
		
		//number of times we've attempted find the right spot, used in the double hashing function
		int j = 0;
		
		//start trying to place the key
		while (j < getPrimeHashTableSizeN())
		{
			
			incrementNumberOfProbes();
			
			//get the hashvalue for the key
			int i = MADCompressionFunction(hashCode.giveCode(key), j);
			
			//the key is an empty string
			if (i < 0)
			{
				
				throw new DictionaryException("You tried to calculate the polynomial accumulation of an empty string");
				
			} //end if
			
			//check if we've found an empty spot to insert a new key
			if ((getHashTableKey(i) == null) || (elementIsAvailable(i)))
			{
				
				//insert the key
				setHashTableKey(i, key);
				incrementNumberOfElements();
				return;
		
			} //end if
			
			else if (getHashTableKey(i).equals(key))
			{
		
				throw new DictionaryException("The word '" + getHashTableKey(i) + "' is already in the dictionary");
				
			} //end else if
			
			else
			{
				
				//try again to find a new spot
				j++;
				
			} //end else
			
		} //end while
		
	} //end insert
	
	//Attempts to find the key in the hashtable
	public boolean find(String key)
	{
		
		incrementNumberOfOperations();
		
		int j = 0;
		
		//start trying to get the key
		while(j < getPrimeHashTableSizeN())
		{
			
			//get the hashvalue for the key
			incrementNumberOfProbes();
			int i = MADCompressionFunction(hashCode.giveCode(key), j);
			
			//if the string is empty, or we find an empty index, then the keys not in the dictionary
			if (getHashTableKey(i) == null || i < 0)
			{
				
				return false;
				
			} //end if
			
			//if theres a string in the index
			else if (!elementIsAvailable(i))
			{
				
				//check to see if it matches our key
				if (getHashTableKey(i).equals(key))
				{
					
					return true;
					
				} //end if
				
			} //end else if
			
			j++;
			
		} //end while
		
		return false;
		
	} //end find		
		
	//removes a key from the hashdictionary
	public void remove(String key) throws DictionaryException 
	{
		
		incrementNumberOfOperations();
		
		int j = 0;
		
		//start looking for the key
		while (j < getPrimeHashTableSizeN())
		{
			
			incrementNumberOfProbes();
			
			//get the hashvalue for the key
			int i = MADCompressionFunction(hashCode.giveCode(key), j);
			
			if (i < 0)
			{
				
				throw new DictionaryException("You tried to calculate the polynomial accumulation of an empty string");
				
			} //end if
			
			//throw exception if the keys not in the dictionary
			if (getHashTableKey(i) == null)
			{
				
				throw new DictionaryException("The word '" + key + "' was not found in the dictionary.");
				
			} //end if
			
			else if (!elementIsAvailable(i))
			{
				
				//if we find the key we are looking for
				if (getHashTableKey(i).equals(key))
				{
					
					//remove it and repace it with AVAILABLE
					setElementAsAvailable(i);
					decrementNumberOfElements();
					return;
					
				} //end if
				
			} //end else if
			
			//apply the next hash
			j++;
			
		} //end while		
		
	} //end remove
	
	//expand the size of the hashtable
	private void reSizeHashTable() throws DictionaryException
	{
		
		setNumberOfElements(0);
		
		//The size of the hash table increases, and variable q becomes the old n
		setPrimeHashTableVariableQ(getPrimeHashTableSizeN());
		setPrimeHashTableSizeN(2 * getPrimeHashTableSizeN());
		
		//set the size to a prime number
		getNextPrime(getPrimeHashTableSizeN());
		
		//re calculate the MAD variables with the new N
		initializeMADVariables();
		
		//temporarily hold the keys from the hashtable
		String [] temp = new String[getPrimeHashTableVariableQ()];
		
		//loop through the hashtable and transfer the keys
		for (int i = 0; i < getPrimeHashTableVariableQ(); i++) 
		{
			
			if ((getHashTableKey(i) != null) && (!elementIsAvailable(i)))
			{
				
				temp[i] = getHashTableKey(i);
				
			} //end if
			
		} //end for
		
		//set the hashtable to the new size
		table = new String[getPrimeHashTableSizeN()];
		
		//insert all the keys again
		for (int i = 0; i < getPrimeHashTableVariableQ(); i++)
		{
			
			if (temp[i] != null)
			{
				
				insert(temp[i]);
				
			} //end if
			
		} //end for
		
	} //end reHash
	
	//get the number of elements in the hashtable
	public int size()
	{
		
		return getNumberOfElements();
		
	} //end size
	
	//get the average number of probes 
	public float averNumProbes() 
	{
		
		return ((float)getNumberOfProbes()/(float)getNumberOfOperations());		
		
	} //end averNumProbes
	
	//calculate a prime number for N
	private void getNextPrime(int num)
	{
		
		//first check if num % 2 = 0, then 3, 4, until you find a number that always has a remainder(prime)
		num++;
		int i = 2;
		
		while (i < num-1)
		{
			
			if (num % i == 0)
			{
				
				num++;
				i = 2;
				
			} //end if
			
			else
				
				i++;
			
		} //end while
		
		setPrimeHashTableSizeN(num);
		
	} //end getNextPrime
	
	//return an iterator over the hashdictionary
	public Iterator<String> elements() 
	{
		
		//put all of the keys in a linked list and use the built in iterator
		LinkedList<String> list = new LinkedList<String>();
		
		for (int i = 0; i < getPrimeHashTableSizeN(); i++)
		{
			
			if ((getHashTableKey(i) != null) && (!elementIsAvailable(i)))
				
				list.add(getHashTableKey(i));
			
		} //end for
		
		return list.listIterator(0);		
		
	} //end elements
	
	//return the loadfactor of the hashtable
	private float getCurrentLoadFactor()
	{
		
		return (((float)getNumberOfElements())/((float)getPrimeHashTableSizeN()));
		
	} //end getLoadFactor	
	
	//return the loadfactor specified when the hashdictionary was created
	private float getMaximumAcceptableLoadFactor()
	{
		
		return loadFactor;
		
	} //end getMaximumAcceptableLoadFactor	
	
	//get the key at index i
	private String getHashTableKey(int i)
	{
		
		return table[i];
		
	} //end getKey
	
	//set the key at index i with string key
	private void setHashTableKey(int i, String key)
	{
		
		table[i] = key;
		return;
		
	} //end setKey
	
	//leave a marker in an index after it is removed
	private void setElementAsAvailable(int i)
	{
		
		setHashTableKey(i, AVAILABLE);
		return;
		
	} //end setElementAsAvailable
	
	//check if an index has a marker
	private boolean elementIsAvailable(int i)
	{
		
		if (getHashTableKey(i) == AVAILABLE)
			
			return true;
		
		else
			
			return false;
		
	} //end elementIsAvailable
	
	//return the MAD a
	private int getMADVariableA()
	{
		
		return MADVariableA;
		
	} //end getMADVariableA
	
	//set the MAD a
	private void setMADVariableA(int n)
	{
		
		MADVariableA = n;
		return;
		
	} //end setMADVariableA
	
	//return the MAD b
	private int getMADVariableB()
	{
		
		return MADVariableB;
		
	} //end getMADVariableB
	
	//set the MAD b
	private void setMADVariableB(int n)
	{
		
		MADVariableB = n;
		return;
		
	} //end getMADVariableB
	
	//get the array size N
	private int getPrimeHashTableSizeN()
	{
		
		return primeHashTableSizeN;
		
	} //end getPrimeHashTableSizeN
	
	//set the array size N
	private void setPrimeHashTableSizeN(int n)
	{
		
		primeHashTableSizeN = n;
		return;
		
	} //end setPrimeHashTableSizeN
	
	//get the hash variable q
	private int getPrimeHashTableVariableQ()
	{
		
		return primeHashTableVariableQ;
		
	} //end getPrimeHashTableVariableQ
	
	//set the hash variable q
	private void setPrimeHashTableVariableQ(int n)
	{
		
		primeHashTableVariableQ = n;
		return;
		
	} //end setPrimeHashTableVariableQ
	
	//get the total number of keys in the hashdictionary
	private int getNumberOfElements()
	{
		
		return numberOfElements;
		
	} //end getNumberOfElements
	
	//set the number of elements in the hashdictionary
	private void setNumberOfElements(int n)
	{
		
		numberOfElements = n;
		return;
		
	} //end setNumberOfElements
	
	//increase the number of elements in the hashdictionary
	private void incrementNumberOfElements()
	{
		
		numberOfElements++;
		return;
		
	} //end incrementNumberOfElements
	
	//decrease the number of elements in the dictionary
	private void decrementNumberOfElements()
	{
		
		numberOfElements--;
		return;
		
	} //end decrementNumberOfElements()
	
	//get the number of probes so far
	private int getNumberOfProbes()
	{
		
		return numProbes;
		
	} //end getNumberOfProbes
	
	//increase the total count of probes
	private void incrementNumberOfProbes()
	{
		
		numProbes++;
		return;
		
	} //end incrementNumberOfProbes
	
	//get the total number of operations so far
	private int getNumberOfOperations()
	{
		
		return numOps;
		
	} //end getNumberOfOperations
	
	//increase the total number of operations so far
	private void incrementNumberOfOperations()
	{
		
		numOps++;
		return;
		
	} //end incrementNumberOfOperations

} //End HashDictionary