package asn3;

import java.util.*;

/**
 * This is a priority heap used for Prim's algorithm. It makes use of the decreaseKey method.
 * 
 * @author Andrew Bloch-Hansen
 *
 * @param <Key> the distance from the start MST node
 * @param <Value> the vertex in the graph
 */
public class Heap<Key,Value> {
	
	/**
	 * heap contains a reference to the heap data structure, in a Vector object.
	 */
	private Vector<Entry<Key,Value>> heap;
	
	/**
	 * maxSize contains the number of vertices the input file gives us.
	 */
    private int maxSize;
    
    /**
     * size contains the current size of the heap.
     */
    private int size;
    
    /**
     * c contains a reference to a small integer comparing object.
     */
    private Comparator c;

    /**
     * This constructor initializes a heap with n elements.
     * @param n the number of vertices in the graph
     */
	public Heap(int n) {
		
		c = new IntegerComparator();
		size = 0;
		maxSize = n;
		heap = new Vector<Entry<Key,Value>>(n+1);
		
		// first element in the heap will not store anything, so add a dummy entry there
        heap.add(new Entry<Key,Value>(null,null, 1));        
		
	} //end Heap
	
	/**
	 * This method will insert an element into the heap. I tried to use the heap_ini recommendation, but had 
	 * difficulties tracking the location of the vertices in the heap. By inserting them one at a time, 
	 * the MST algorithm can set the position in the heap of each vertex very easily.
	 * @param k the distance of the node from the start node in MST
	 * @param v a vertex from the graph
	 * @return the position in the heap the entry was stored in
	 * @throws HeapException when the heap is full
	 */
    public Position insert(Key k, Value v) throws HeapException {
    	
        if ( size == maxSize )
        	
        	throw new HeapException("All the vertices are in the heap");
        
        size += 1;
        Entry<Key,Value> entry = new Entry<Key,Value>(k,v,size);
        heap.add(entry);
     
        upHeap(size);
        
        return ((Position)entry);
        
    } //end insert
	
    /**
     * This method checks to see if a vertex is in the heap.
     * @param id the vertex to search for
     * @return boolean indicating if the vertex was found
     */
	public boolean inHeap(Value id) {
		
		Iterator<Value> valuesIt = values();

		while (valuesIt.hasNext()) {
			
			Value v = valuesIt.next();
			
			if (id.equals(v)) 
				
				return true;
			
		} //end while
		
		return false;
		
	} //end in_heap
	
	/**
	 * This method returns the minimum distance from the start node in MST.
	 * @return the minimum distance from the start node
	 */
	public Key minKey() {
		
		if (size == 0 ) 
			
			throw new HeapException("Heap is empty");
		
		return (heap.elementAt(1).getKey());
		
	} //end min_key
	
	/**
	 * This method returns the vertex with the minimum distance from the start node in MST.
	 * @return the vertex with the minimum distance
	 */
	public Value minId() {
		
		if (size == 0)
			
			throw new HeapException("Heap is empty");
		
		return (heap.elementAt(1).getValue());
		
	} //end min_id
	
	/**
	 * This method returns the distance of the vertex sent in.
	 * @param id the vertex being sent in
	 * @return the distance of the vertex
	 */
	public Key key(Value id) {
		
		if (!inHeap(id))
			
			throw new HeapException("Key not in tree");
		
		Iterator<Value> valuesIt = values();
		int index = 0;
		Key toReturn = heap.elementAt(0).getKey();

		while (valuesIt.hasNext()) {
			
			Value v = valuesIt.next();
			index++;
			
			if (id.equals(v)) 
				
				toReturn = heap.elementAt(index).getKey();
			
		} //end while
		
		return toReturn;
		
	} //end key
	
	/**
	 * This method deletes the vertex with the minimum distance from the start node in the MST.
	 * @return the vertex with the minimum distance
	 */
	public Value deleteMin() {
		
		if (size == 0 ) 
			
			throw new HeapException("Heap is empty");
        
        if ( size > 1 )
        	
        	heapSwap(1,size);
        
        Value toReturn = heap.elementAt(size).getValue();        
        heap.remove(size);        
        size -= 1;
        
        int i = 1;
        int childIndex = findSmallestKeyChild(i);
        
        while (childIndex != 0 && c.compare(heap.elementAt(childIndex).getKey(),heap.elementAt(i).getKey()) == -1) {
        	
            heapSwap(childIndex,i);
            i = childIndex;
            childIndex = findSmallestKeyChild(i);
            
        } //end while
        
        return toReturn;
		
	} //end deleteMin
	
	/**
	 * This method will change the key of an entry, and re-balance the heap.
	 * @param p the location in the heap of the vertex
	 * @param newKey the new distance for the vertex
	 * @throws HeapException when the new distance is too large
	 */
	public void decreaseKey(Position p, Key newKey) throws HeapException {
		
		Entry e = (Entry)p;	
		int heapLoc = e.getLocation();		
		
		if (c.compare(heap.elementAt(heapLoc).getKey(),newKey) < 0) 
			
            throw new HeapException("New key must be smaller in decreaseKey()");                
		
		heap.elementAt(heapLoc).changeKey(newKey);
        upHeap(heapLoc);
		
	} //end decreaseKey
	 /**
	  * This method returns the number of elements in the heap.
	  * @return the number of elements in the heap
	  */
	public int size() { 
		
		return size;
		
	} //end size
	
	 /**
	  * This method returns true if there are no elements in the heap.
	  * @return boolean indicating if the heap is empty
	  */
	public boolean isEmpty() {
	
		return size == 0;
		
	} //end isEmpty
	
	/**
	 * This method swaps the location of 2 vertices.
	 * @param i the first vertex to swap
	 * @param j the second vertex to swap
	 */
	private void heapSwap(int i, int j) {
		
        Entry<Key,Value> temp = heap.elementAt(i);
        heap.set(i,heap.elementAt(j));
        heap.set(j,temp);
      
        // now update locations of the location-aware entries
        heap.elementAt(i).setLocation(i); 
        heap.elementAt(j).setLocation(j); 
        
    } //end heapSwap

	/**
	 * This method re-balances the heap, and swaps vertices as necessary.
	 * @param i the location in the heap to re-balance from
	 */
    private void upHeap(int i) {
    	
        while (i > 1 && c.compare(heap.elementAt(i/2).getKey(),heap.elementAt(i).getKey()) > 0) {
        	
            heapSwap(i,i/2);
            i = i/2;
            
        } //end while
        
    } //end upHeap
    
    /**
     * This method returns an iterator over the vertices in the heap.
     * @return an iterator over the vertices in the heap
     */
    public Iterator<Value> values() {
    	
        LinkedList<Value> l = new LinkedList<Value>();
        
        for ( int i = 1; i <= size; i++)
        	
            l.add(heap.elementAt(i).getValue());
        
        return l.listIterator(0);
        
    } //end values    
    
    /**
     * This method returns the location of the smallest child of i.
     * @param i the parent node
     * @return the smallest child of i
     */
    private int findSmallestKeyChild(int i) {
    	
    	// node at index i has 2 children in this case
        if (2*i < size) { 
        
            if (c.compare(heap.elementAt(i*2).getKey(),heap.elementAt(i*2+1).getKey()) == -1)
            	
                return(2*i);
            
            else 
            	
            	return(2*i+1);
            
        } //end if
        
        // node at index i has only a left child, no right child
        else if (2*i == size) 
        	
            return(2*i);
        
        else 
        	
        	return(0); // node at index i is a leaf
        
    } //end findSmallestKeyChild
	
} //end Heap