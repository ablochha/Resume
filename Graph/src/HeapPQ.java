/*
 * by olga veksler
 */

//package imagesegment;

import java.util.*;

public class HeapPQ<Key,Value>{
    private Vector<Entry<Key,Value>> heap;

    private int maxSize;
    private int size;
    private Comparator c;

    HeapPQ(int n, Comparator cIn){
	heap     = new Vector<Entry<Key,Value>>(n+1);
        // first element in the heap will not store anything, so add a dummy entry there
        heap.add(new Entry<Key,Value>(null,null,1));
        size    = 0;
        maxSize = n;
        c       = cIn;
    }
 
    public int size(){ return(size);}
    public boolean isEmpty() { return(size == 0); }
    
    // throws HashException if the heap is empty
    public Value removeMin() throws HeapException{
        if (size == 0 ) throw new HeapException("Heap is empty");
        
        if ( size > 1 ) heapSwap(1,size);
        Value toReturn = heap.elementAt(size).getValue();
        
        heap.remove(size);
        
        size = size-1;
        
        int i = 1;
        int childIndex = findSmallestKeyChild(i);
        while ( childIndex != 0 &&  c.compare(heap.elementAt(childIndex).getKey(),heap.elementAt(i).getKey() ) == -1){
            heapSwap(childIndex,i);
            i = childIndex;
            childIndex = findSmallestKeyChild(i);
        }
        return(toReturn);
    }

    // swaps heap entries and their locations
    private void heapSwap(int i, int j){
        Entry<Key,Value> temp = heap.elementAt(i);
        heap.set(i,heap.elementAt(j));
        heap.set(j,temp);
      
        // now update locations of the location-aware entries
        heap.elementAt(i).setLocation(i); 
        heap.elementAt(j).setLocation(j); 
    }

    private void upHeap(int i){
        while ( i > 1 && c.compare(heap.elementAt(i/2).getKey(),heap.elementAt(i).getKey()) > 0 ){
            heapSwap(i,i/2);
            i = i/2;
        }
    }
    // returns the slot where the location of the item will be held
    public Position insert(Key k, Value v) throws HeapException{
        if ( size == maxSize ) throw new HeapException("Heap is full");
        size = size + 1;
        Entry<Key,Value> entry = new Entry<Key,Value>(k,v,size);
        heap.add(entry);
        // now perform upheap
        upHeap(size);
        return((Position) entry);
    }
    
    public void decreaseKey(Position p, Key newKey) throws HeapException{
        
        Entry e = (Entry) p;
        int heapLoc = e.getLocation();
        
        if ( c.compare(heap.elementAt(heapLoc).getKey(),newKey) < 0  ) 
            throw new HeapException("New key must be smaller in decreaseKey()");
        heap.elementAt(heapLoc).changeKey(newKey);
        upHeap(heapLoc);
    }
    
    public Iterator<Value> values(){
        LinkedList<Value> l = new LinkedList<Value>();
        for ( int i = 1; i <= size; i++)
            l.add(heap.elementAt(i).getValue());
        return l.listIterator();
    }
    
    private int findSmallestKeyChild(int i){
        if ( 2*i < size ) // node at index i has 2 children in this case
        {
            if ( c.compare(heap.elementAt(i*2).getKey(),heap.elementAt(i*2+1).getKey()) == -1 )
                return(2*i);
            else return(2*i+1);
        }
        else if ( 2*i == size ) // node at index i has only a left child, no right child
            return(2*i);
        else return(0); // node at index i is a leaf
        
    }
    
    

}