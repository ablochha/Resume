/*
 * Author: Andrew Bloch-Hansen
 */

import java.util.Iterator;
import java.util.LinkedList;

public class AVLTree implements AVLTreeInterface{
	
	private int numberOfEntries;		//unique entries in the tree
	private Comparator treeComparator;	//rules for comparing objects
	private AVLnode root;				//the root node
		
	//Constructor that accepts the rules for comparing objects, and initializes the AVL tree
	public AVLTree(Comparator inputComparator)
	{
		
		treeComparator = inputComparator;
		numberOfEntries = 0;
		root = new AVLnode(null, null, null, null);	
		
	} //end AVLTree
	
	//Determines whether a node is an external node
	public boolean external(Position p)
	{
		
		//A node is external if it has no entry
		return (((AVLnode)p).getEntry() == null);
		
	} //end external
	
	//Returns the position of the left child
	public Position left(Position p)
	{
		
		AVLnode node;
		node = (AVLnode)p;
		return (Position)node.left();
		
	} //end left
	
	//Returns the position of the right child
	public Position right(Position p)
	{
		
		AVLnode node;
		node = (AVLnode)p;
		return (Position)node.right();
		
	} //end right
	
	//Returns the position of the root
	public Position giveRoot()
	{
		
		return (Position)root;		
		
	} //end giveRoot
	
	//Returns the number of unique entries in the tree
	public int size()
	{
		
		return getNumberOfEntries();
		
	} //end size
	
	//Returns the height of the tree
	public int treeHeight()
	{
		
		AVLnode node = (AVLnode)giveRoot();
		return node.getHeight();
		
	} //end treeHeight
	
	//Determines whether the tree is empty
	public boolean isEmpty()
	{
		
		if (getNumberOfEntries() == 0)
			
			return true;
		
		else
			
			return false;
		
	} //end isEmpty
	
	//Looks for a key and either returns it, or null
	public DictEntry find(Object key)
	{
		
		AVLnode node = TreeSearch(key, (AVLnode)giveRoot());
		
		//if the node is external, it does not have a value. return null
		if (external((Position)node))
			
			return null;
		
		//the node is internal, return its entry
		else
			
			return node.getEntry();
				
	} //end find
	
	//Binary search tree method for finding a key
	private AVLnode TreeSearch(Object key, AVLnode w)
	{
		
		//if the key isn't in the tree, return the external node where it should go
		if (external(w) == true)
			
			return w;
		
		//check left subtree if our key is less then the node we are looking at
		if (treeComparator.compare(key, w.getEntry().key()) < 0)
			
			return TreeSearch(key, w.left());
		
		//we found the key, return the node its located in
		else if (treeComparator.compare(key, w.getEntry().key()) == 0)
			
			return w;
		
		//check right subtree if our key is more then the node we are looking at
		else
			
			return TreeSearch(key, w.right());
		
	} //end TreeSearch
	
	//Search for a node by key, and change its value
	public void modifyValue(Object key, Object valueNew) throws AVLtreeException
	{
		
		//look for the key
		AVLnode node = TreeSearch(key, (AVLnode)giveRoot());
		
		//If we got an external node back, the key is not in the tree
		if (external((Position)node))
			
			throw new AVLtreeException("This key is not in the tree");
		
		//If we got an internal node back, change its value
		else
		{
			
			DictEntry entry = new DictEntry(key, valueNew);
			node.setEntry(entry);
			
		} //end else
		
	} //end modifyValue
	
	//Insert a new entry into the AVL tree
	public void insertNew(Object key, Object value) throws AVLtreeException
	{
		
		AVLnode z;
		Position w;
		
		//Insert the key into the tree
		w = TreeInsert(key, value, (AVLnode)giveRoot());
		z = (AVLnode)w;
		
		//Re-balance if necessary
		while (z != null)
		{
			
			//recalculate height after insertion
			z.resetHeight();
			
			//If the siblings differ in heights by more then 1
			if (Math.abs(z.left().getHeight()-z.right().getHeight()) > 1)
			{
				
				//Perform TriNodeRestructure to restore balance
				z = (AVLnode)TriNodeRestructure(tallerChild(tallerChild(z)), tallerChild(z), z);
				z.left().resetHeight();
				z.right().resetHeight();
				z.resetHeight();
				break;
				
			} //end if
			
			//Check for imbalances up the tree
			z = z.parent();
			
		} //end while
		
	} //end insertNew
	
	//Binary search tree method for inserting an entry
	private AVLnode TreeInsert(Object key, Object value, AVLnode node) throws AVLtreeException
	{
		
		//Look for the key or the spot it should go
		AVLnode z = TreeSearch(key, node);
		
		//TreeSearch found the key already in the tree
		if (!external((Position)z))
		{
			
			//For entries using the value field, increment the value
			if (Integer.valueOf(String.valueOf(z.getEntry().value())) > 0)
				
				increaseFrequency(z);
			
			throw new AVLtreeException("This key is already in the tree");
			
		} //end if
		
		//TreeSearch returned the spot the key should go
		else
		{
			
			//Put the new entry into an external node
			DictEntry entry = new DictEntry(key, value);
			insertAtExternal(z, entry);
						
		} //end else
		
		//return the node that holds the new key
		return z;	
		
	} //end TreeInsert
	
	//Place a new entry into an external node, and give it two external children
	private void insertAtExternal(AVLnode z, DictEntry entry)
	{
		
		//Create two new children and create relationships with the parent
		AVLnode newLeft, newRight;			
		newLeft = new AVLnode(null, z, null, null);
		newRight = new AVLnode(null, z, null, null);		
		z.setLeft(newLeft);
		z.setRight(newRight);
		
		//Put the entry in the node
		z.setEntry(entry);
		increaseNumberOfEntries();
				
	} //end insertAtExternal
	
	//Re-balance the AVL tree
	private Position TriNodeRestructure(AVLnode x, AVLnode y, AVLnode z)
	{
		
		//variables for comparing x, y, z keys
		Object xKey, yKey, zKey;
		AVLnode a, b, c;
		a = null;
		b = null;
		c = null;
		
		//get the keys
		xKey = (x.getEntry()).key();	//child of y
		yKey = (y.getEntry()).key();	//child of z
		zKey = (z.getEntry()).key();	//z is unbalanced
		
		//If z is smallest, x is in the middle, y is largest
		if ((treeComparator.compare(zKey, xKey) <= 0) && (treeComparator.compare(xKey, yKey) <= 0))
		{
			
			a = z;
			b = x;
			c = y;
			
		} //end if
		
		//If y is smallest, x is in the middle, z is largest
		else if ((treeComparator.compare(zKey, xKey) >= 0) && (treeComparator.compare(xKey, yKey) >= 0))
		{
			
			a = y;
			b = x;
			c = z;
			
		} //end if
		
		//If z is smallest, y is in the middle, x is largest
		else if ((treeComparator.compare(zKey, yKey) <= 0) && (treeComparator.compare(yKey, xKey) <= 0))
		{
			
			a = z;
			b = y;
			c = x;
			
		} //end if
		
		//If x is smallest, y is in the middle, z is largest
		else if ((treeComparator.compare(zKey, yKey) >= 0) && (treeComparator.compare(yKey, xKey) >= 0))
		{
			
			a = x;
			b = y;
			c = z;
			
		} //end if
		
		//If the unbalance is occuring at the root
		if (z == (AVLnode)giveRoot())
		{
			
			//root changes to be the middle key
			setRoot(b);
			b.setParent(null);
			
		} //end if
		
		//The unbalance is not at the root
		else
		{
			
			//If the unbalanced node is a left child
			if (z.parent().left() == z)
				
				//Then the parent gets the new middle node for its left child
				MakeLeftChild(z.parent(), b);
			
			//If the unbalanced node is a right child
			else
				
				//Then the parent gets the new middle node for its right child
				MakeRightChild(z.parent(), b);
			
		} //end else
		
		//If the middle node's left child is not among our tri nodes
		if ((b.left() != x) && (b.left() != y) && (b.left() != z))
			
			//Move the orphaned subtree to the right subtree of the smallest tri node
			MakeRightChild(a, b.left());
		
		//If the middle node's right child is not among our tri nodes
		if ((b.right() != x) && (b.right() != y) && (b.right() != z))
			
			//Move the orphaned subtree to the left subtree of the largest tri node
			MakeLeftChild(c, b.right());
		
		//Set the relationships of the new tri nodes
		MakeLeftChild(b, a);
		MakeRightChild(b, c);
		
		//return the position of the middle node
		return (Position) b;
		
	} //end TriNodeRestructure

	//Makes b the left child of a
	private void MakeLeftChild(AVLnode a, AVLnode b)
	{
		
		a.setLeft(b);
		b.setParent(a);
		
	} //end MakeLeftChild
	
	//Makes b the right child of a
	private void MakeRightChild(AVLnode a, AVLnode b)
	{
		
		a.setRight(b);
		b.setParent(a);
		
	} //end MakeRightChild
	
	//Returns the child with the larger height
	private AVLnode tallerChild(AVLnode node)
	{
		
		//If the left child is has a larger height
		if (node.left().getHeight() > node.right().getHeight())
			
			return node.left();
		
		//If the right child has a larger height
		else 
			
			return node.right();
		
	} //end tallerChild
	
	//Increases the number of unique entries in the tree
	private void increaseNumberOfEntries()
	{
		
		numberOfEntries++;
		
	} //end increaseNumberOfEntires
	
	//Returns the number of unique entries in the tree
	private int getNumberOfEntries()
	{
		
		return numberOfEntries;
		
	} //end getNumberOfEntires
	
	//Remove an entry from the AVL tree
	public DictEntry remove(Object key) throws AVLtreeException
	{
		
		AVLnode z, removed;
		
		//The node that contains the key
		removed = TreeSearch(key, (AVLnode)giveRoot());
		
		//The node that needs to be checked for balance
		z = TreeDelete(removed);
		
		//Traverse up the tree, checking for balance
		while (z != null)
		{
			
			//Re-calculate the height at node z
			z.resetHeight();
			
			//If there is an imbalance in the siblings
			if (Math.abs(z.left().getHeight()-z.right().getHeight()) > 1)
			{
				
				//Perform trinode restructure on the unbalanced node
				z = (AVLnode)TriNodeRestructure(tallerChild(tallerChild(z)), tallerChild(z), z);					
				z.left().resetHeight();					
				z.right().resetHeight();				
				z.resetHeight();
				
			} //end if
			
			//Traverse up the tree
			z = z.parent();
			
		} //end while
		
		
		//If the key wasn't in the tree, return null
		if (external((Position)removed))
			
			return null;
		
		//Return the entry that was removed
		else
			
			return removed.getEntry();
				
	} //end remove
	
	//Binary Search Tree method for deletion
	private AVLnode TreeDelete(AVLnode v) throws AVLtreeException
	{
		
		AVLnode w;
		
		//TreeSearch found the key
		if (!external((Position)v))
		{
		
			//if node v has two external children
			if ((external((Position)v.left())) && (external((Position)v.right())))
			{
				
				//remove the left external child and its parent
				removeAtExternal(v.left());
				
				//return the node that needs to be checked for balance
				return v.parent();
				
			} //end if
			
			//if node v has a left external child and right internal child
			else if (external((Position)v.left()))
			{
				
				//remove the left external child and its parents
				removeAtExternal(v.left());
				
				//return the node that needs to be checked for balance
				return v.right();
				
			} //end if				
			
			//if node v has a right external child and left internal child
			else if (external((Position)v.right()))
			{
				
				//remove the right external child and its parent
				removeAtExternal(v.right());	
				
				//return the node that needs to be checked for balance
				return v.left();
				
			} //end else if
				
			//if node v has two internal children
			else
			{
				
				DictEntry temp;
				
				//Get the next largest key by inorder traversal
				w = nextInOrder(v.right());
				
				//copy this next largest onto the node containing the key being removed
				temp = w.getEntry();
				v.setEntry(temp);
				
				//remove the original copy of the next largest and return the node to be checked for balance
				removeAtExternal(w.left());
				return w.parent();
				
			} //end else
			
		} //end if
		
		//TreeSearch couldn't find the key, and we are looking at an external node
		else
		{
			
			throw new AVLtreeException("The key is not in the tree");
						
		} //end else
					
	} //end TreeDelete
	
	//Binary Search Tree method for removing an external node and its parent
	private void removeAtExternal(AVLnode w)
	{
		
		AVLnode p, nodeToRelink;
		p = w.parent();
		
		//if w is a left child
		if (p.left() == w)
			
			//the right child will need to be relinked
			nodeToRelink = p.right();
		
		//if w is a right child
		else
			
			//the left child will need to be relinked
			nodeToRelink = p.left();
		
		//if we are trying to remove the root
		if (p == (AVLnode)giveRoot())
		{
			
			//the root will need to be relinked
			setRoot(nodeToRelink);
			nodeToRelink.setParent(null);
			
		} //end if
		
		//p has a parent
		else
		{
			
			//if p is a left child
			if (p == (p.parent().left()))
				
				//relink a new left child to p's parent
				p.parent().setLeft(nodeToRelink);
			
			//if p is a right child
			else
				
				//relink a new right child to p's parent
				p.parent().setRight(nodeToRelink);
			
			//relink node to its parent
			nodeToRelink.setParent(p.parent());
			
		} //end else
		
	} //end removeAtExternal
	
	//Gets the entry that follows the input in an inorder traversal
	private AVLnode nextInOrder(AVLnode node)
	{
		AVLnode next = node;
		
		//Recursively check left subtree 
		if (!external(node.left()))
		
			next = nextInOrder(node.left());
			
		//return the value when there is no more left
		return next;
		
	} //end nextInOrder
	
	//Increase the frequency when counting words
	private void increaseFrequency(AVLnode node)
	{
		
		DictEntry entry;
		String key;
		int value;
		
		//get the frequency from the node, and increase it by one
		entry = node.getEntry();
		key = (String)entry.key();
		value = Integer.valueOf(String.valueOf(entry.value()));
		value++;
		
		//put the new frequency back in the node
		entry = new DictEntry(key, value);
		node.setEntry(entry);
		
	} //end increaseFrequency
	
	//Set the root node
	private void setRoot(AVLnode node)
	{
		
		root = node;
		
	} //end setRoot
	
	//Return an iterator over the tree following an inorder traversal
	public Iterator<DictEntry> inOrder()
	{
		
		LinkedList<DictEntry> list = new LinkedList<DictEntry>();
		
		list = inOrder((AVLnode)giveRoot(), list);
		
		return list.listIterator(0);
		
	} //end inOrder
	
	//Use inorder traversal to fill a list with entries
	private LinkedList<DictEntry> inOrder(AVLnode node, LinkedList<DictEntry> list)
	{
		
		//If the left child is internal
		if (!external(node.left()))
			
			//Check the left subtree
			list = inOrder(node.left(), list);
		
		//Update the list
		list.add(node.getEntry());
		
		//If the right child is internal
		if (!external(node.right()))
			
			list = inOrder(node.right(), list);
		
		//Pass the list
		return list;
		
	} //end inOrder
	
	//Returns an iterator over the tree with the n largest keys
	public Iterator<DictEntry> findnLargestKeys(int k)
	{
		
		LinkedList<DictEntry> list = new LinkedList<DictEntry>();
		
		list = reverseOrder((AVLnode)giveRoot(), k, list);
		
		return list.listIterator(0);
		
	} //end findnLargestKeys
	
	//Use reverse-inorder traversal to fill the list with the largest n keys
	private LinkedList<DictEntry> reverseOrder(AVLnode node, int k, LinkedList<DictEntry> list)
	{
					
		//If the right child is internal and the list isn't full
		if ((!external(node.right())) && (list.size() < k))
				
			//Check the right subtree
			list = reverseOrder(node.right(), k, list);
			
		//If the list isn't full
		if (list.size() < k)
			
			//Update the list
			list.add(node.getEntry());
			
		//If the left child is internal and the list isn't full
		if ((!external(node.left())) && (list.size() < k))
				
			//Check the left subtree
			list = reverseOrder(node.left(), k, list);
			
		//Pass the list
		return list;		
		
	} //end reverseOrder
	
} //end AVLTree