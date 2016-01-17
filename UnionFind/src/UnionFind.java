/*
 * Andrew Bloch-Hansen
 * 250473076
 */

public class UnionFind {
	
	private TreeNode[] _forest;			//Holds all the nodes across the forest
	private TreeNode[] _final_sets;		//Holds the representative nodes
	private int _size;					//The total number of nodes in the forest
	private int _numberOfSets;			//The total number of connected components
	private boolean _locked;			//Locks makeset and unionsets after finalsets
	
	
	//Constructor that makes the forest, and initializes basic values
	public UnionFind(int size) {
			
		_size = size;
		_forest = new TreeNode[_size];
		_numberOfSets = 0;
		_locked = false;
		
	} //end UnionFind

	//Make a new set with one connected component and a rank of 0
	public void makeSet(TreeNode x) {
		
		//check to see if final sets has been called
		if (!(_locked)) {
			
			//Make this new entry the representitive of its set
			_forest[x.getId()] = x;
			x.setParent(x);
			x.setRank(0);
			
			//Only count +'s
			if (x.getEntry().equals("+"))
				_numberOfSets++;
			
		} //end if		
	
	} //end makeSet
	
	
	//Merge two components into one connected component
	public void unionSets(TreeNode x, TreeNode y) {
		
		if (!(_locked)) {
			
			link(findSet(x), findSet(y));
			_numberOfSets--;
			
		} //end if	
		
	} //end unionSets
	
	//Return the representative of a set, also apply path compression
	public TreeNode findSet(TreeNode x) {
		
		if (x != x.getParent()) {
			
			x.setParent(findSet(x.getParent()));
			
		} //end if
		
		return x.getParent();
		
	} //end findSet
	
	//Merge two components together, use rank to decide which root will be the new root
	public void link(TreeNode x, TreeNode y) {
		
		if (x.getRank() > y.getRank()) {
			
			y.setParent(x);
			
		} //end if
		
		else {
			
			x.setParent(y);
			
		} //end else
		
		if (x.getRank() == y.getRank()) {
			
			y.setRank(y.getRank() + 1);
			
		} //end if
		
	} //end link
	
	//Disable makeset and unionsets, compute the number of connected components, and rename set representatives from 1-n
	public int finalSets() {
		
		//Store all the representatives in an array
		_final_sets = new TreeNode[_numberOfSets];
		int count = 0;
		_locked = true;
		boolean addSet;
		
		//Loop through the forest
		for (int i = 0; i < _size; i++) {
			
			//Ignore white spaces
			if (!(_forest[i].getEntry().equals(" "))) {
				
				addSet = true;
				int j = 0;
				
				//Check if a node in the forest is part of a set we havn't already counted
				while (j < count && addSet == true) {
					
					if (findSet(_forest[i]) == findSet(_final_sets[j])) {
						
						addSet = false;
						
					} //end 
					
					j++;
					
				} //end while
				
				//If we found a new connected component, set its representative
				if (addSet == true) {
					
					_final_sets[count] = findSet(_forest[i]);
					_final_sets[count].setEntry(Integer.toString(count+1));
					count++;
					
				} //end if
				
			} //end if
			
		} //end for
		
		return _numberOfSets;
		
	} //end finalSets

} //end Union Find