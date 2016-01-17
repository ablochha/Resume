/*
 * Andrew Bloch-Hansen
 * 250473076
 */

public class TreeNode {

	private TreeNode _parent;     	// reference to the parent node
    private String _entry;    		// reference to the entry stored at the node
    private int _id;				// reference to the index in the forest
    private int _rank;        		// reference to the rank
    
    public TreeNode(String entry, int id) {
    	
    	_entry  = entry;
    	_id = id;
        _rank = 0;
	
    } //end TreeNode

    public TreeNode getParent() {
    	
    	return _parent;
    	
    } //end getParent
    
    public void setParent(TreeNode parent) {
    	
    	_parent = parent; 
    	
    } //end setParent
    
    public int getId() {
    	
    	return _id;
    	
    } //end getId
    
    public void setId(int id) {
    	
    	_id = id;
    	
    } //end setId
    
    public String getEntry() {
    	
    	return _entry; 
    	
    } //end getEntry
    
    public void setEntry(String entry) {
    	
    	_entry = entry;
    	
    } //end setEntry

    public int getRank () {
    	
    	return _rank; 
    	
    } //end getRank
    
    public void setRank(int rank) {
    	
    	_rank = rank;
    	
    } //end setRank
	
} //end TreeNode