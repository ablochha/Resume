package asn3;

import java.util.LinkedList;
import java.util.Iterator;

/**
 * This class represents a vertex in the graph.
 * 
 * @author Andrew Bloch-Hansen
 */
public class Vertex<O> {
	
	/**
	 * vertex contains the data for the graph.
	 */
	private O vertex;	
	
	/**
	 * parent contains the MST parent edge of the vertex.
	 */
	private Edge<O> parent;	
	
	/**
	 * adjacencyList contains the list of incident edges of a vertex.
	 */
	private LinkedList<Edge<O>> adjacencyList;	

	/**
	 * visited stores whether a node has been visited in the MST algorithm.
	 */
	private boolean visited;
	
	/**
	 * distance stores the distance from the start node in the MST.
	 */
	private Integer distance;					
	
	/**
	 * locator stores the position of the vertex in the heap.
	 */
	private Position locator;
	
	/**
	 * This constructor fills the vertex with the data.
	 * @param objectIn the data being stored in the vertex
	 */
	public Vertex(O objectIn) {
		
		vertex = objectIn;
		parent = null;
		adjacencyList = new LinkedList<Edge<O>>();
		visited = false;
		distance = 0;
		
	} //end Vertex
	
	/**
	 * This method returns the element contained in the vertex.
	 * @return the element in the vertex
	 */
	public O getObject() {
		
		return vertex;
		
	} //end getObject
	
	/**
	 * This method adds an edge to the adjacency list.
	 * @param e the edge to be added
	 * @throws GraphException when the edge already exists
	 */
	public void addAdjacent(Edge<O> e) throws GraphException {
		
		Vertex<O> eEndPoint1 = e.getEndPoint1();
		Vertex<O> eEndPoint2 = e.getEndPoint2();
		
		Edge<O> otherEdge = new Edge<O>(eEndPoint2, eEndPoint1, e.getWeight());		
		
		if (adjacencyList.contains(e) || adjacencyList.contains(otherEdge)) {
			
			throw new GraphException("This edge is already in the adjacency list");
			
		} //end if
		
		adjacencyList.add(e);
		
	} //end addAdjacent
	
	/**
	 * This method compares two edge objects.
	 * @param e1 the first edge
	 * @param e2 the second edge
	 * @return boolean indicating if the edges were the same
	 */
	private boolean equals(Edge<O> e1, Edge<O> e2) {
		
		//check if both edges are equal
		if ((e1.getEndPoint1().equals(e2.getEndPoint1()) && e1.getEndPoint2().equals(e2.getEndPoint2()) && e1.getWeight()== e2.getWeight()) || (e1.getEndPoint1().equals(e2.getEndPoint2()) && e1.getEndPoint2().equals(e2.getEndPoint1()) && e1.getWeight()== e2.getWeight())) {
			
			return true;
			
		} //end if
		
		else
			
			return false;
		
	} //end equals
	
	/**
	 * This method returns an iterator over all the incident edges of a vertex.
	 * @return iterator over incident edges
	 */
	public Iterator<Edge<O>> incidentEdges() {
		
		return adjacencyList.listIterator(0);
		
	} //end incidentEdges
	
	/**
	 * This method determines if two vertices are adjacent.
	 * @param v the vertex
	 * @return boolean indicating if they are adjacent
	 */
	public boolean isAdjacent(Vertex<O> v) {
		
		Iterator<Edge<O>> it = this.incidentEdges();
		
		//Loop through all incident edges
		while (it.hasNext()) {
			
			Edge<O> temp = it.next();
			Vertex<O> endPoint1 = temp.getEndPoint1();
			Vertex<O> endPoint2 = temp.getEndPoint2();
			
			//check if the two vertices are on the same edge in the adjacency list
			if ((this.equals(endPoint1) && v.equals(endPoint2)) || (this.equals(endPoint2) && v.equals(endPoint1))) {
				
				return true;
				
			} //end if
						
		} //end while
		
		return false;
		
	} //end isAdjacent

	/**
	 * This method deletes the adjacency list for a vertex.
	 */
	public void resetEdges() {
		
		adjacencyList.clear();
		
	} //end resetEdges
	
	/**
	 * This method sets the label of a vertex.
	 * @param visit whether the vertex has been visited
	 */
	public void setLabel(boolean visit) {
		
		visited = visit;
		
	} //end setLabel
	
	/**
	 * This method returns the label of a vertex.
	 * @return the label of a vertex
	 */
	public boolean getLabel() {
		
		return visited;
		
	} //end getLabel
	
	/**
	 * This method sets the distance to a vertex.
	 * @param d the distance to the vertex
	 */
	public void setDistance(Integer d) {
		
		distance = d;
		
	} //end setDistance
	
	/**
	 * This method returns the distance to a vertex.
	 * @return the distance to the vertex
	 */
	public Integer getDistance() {
		
		return distance;
		
	} //end getDistance
	
	/**
	 * This method sets the parent edge of a vertex.
	 * @param p the parent edge
	 */
	public void setParent(Edge<O> p) {
		
		parent = p;
		
	} //end setParent
	
	/**
	 * This method returns the parent edge of a vertex.
	 * @return the parent edge
	 */
	public Edge<O> getParent() {
		
		return parent;
		
	} //end getParent
	
	/**
	 * This method sets the position of the vertex in the heap.
	 * @param l the position of the vertex in the heap
	 */
	public void setLocator(Position l) {
			
		locator = l;
			
	} //end setLocator
		
	/**
	 * This method returns the position of the vertex in the heap.
	 * @return the position of the vertex in the heap
	 */
	public Position getLocator() {
			
		return locator;
			
	} //end getLocator
	
} //end Vertex