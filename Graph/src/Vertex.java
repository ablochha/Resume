/*
 * Author: Andrew Bloch-Hansen
 */

import java.util.LinkedList;
import java.util.Iterator;

public class Vertex<O> {
	
	private O vertex;							//container for vertex
	private Edge<O> parent;						//reference to MST parent edge
	private LinkedList<Edge<O>> adjacencyList;	//List of incident edges of a vertex
	private boolean visited;					//Marker for visiting a vertex during traversals
	private Integer distance;					//Cumulative weight to a vertex from a start point for MST
	private Position locator;					//Reference to position in heap
	
	//Constructor for Vertex object
	public Vertex(O objectIn)
	{
		
		vertex = objectIn;
		parent = null;
		adjacencyList = new LinkedList<Edge<O>>();
		visited = false;
		distance = 0;
		
	} //end Vertex
	
	//This method returns the element contained in the vertex
	public O getObject()
	{
		
		return vertex;
		
	} //end getObject
	
	//This method adds an edge to the adjacency list
	public void addAdjacent(Edge<O> e) throws GraphException
	{
		
		Vertex<O> eEndPoint1 = e.getEndPoint1();
		Vertex<O> eEndPoint2 = e.getEndPoint2();
		
		Edge<O> otherEdge = new Edge<O>(eEndPoint2, eEndPoint1, e.getWeight());		
		
		if (adjacencyList.contains(e) || adjacencyList.contains(otherEdge))
		{
			
			throw new GraphException("This edge is already in the adjacency list");
			
		} //end if
		
		adjacencyList.add(e);
		
	} //end addAdjacent
	
	//This method deletes an edge from the graph, it is public so the adjacencylist can be updated
	public void deleteAdjacentEdge(Edge<O> e)
	{
		
		Edge<O> removed = null;
		Iterator<Edge<O>> edges = incidentEdges();
		
		//Look through the incident edges
		while (edges.hasNext())
		{
			Edge<O> temp = edges.next();
			
			if (equals(e, temp))

				removed = temp;		
			
		} //end while
		
		if (removed != null)
			
			adjacencyList.remove(removed);
			
	} //end deleteAdjacentEdge
	
	//This method compares two edge objects
	private boolean equals(Edge<O> e1, Edge<O> e2)
	{
		
		//check if both edges are equal
		if ((e1.getEndPoint1().equals(e2.getEndPoint1()) && e1.getEndPoint2().equals(e2.getEndPoint2()) && e1.getWeight()== e2.getWeight()) || (e1.getEndPoint1().equals(e2.getEndPoint2()) && e1.getEndPoint2().equals(e2.getEndPoint1()) && e1.getWeight()== e2.getWeight()))
		{
			
			return true;
			
		} //end if
		
		else
			
			return false;
		
	} //end equals
	
	//This method returns an iterator over all the incident edges of a vertex
	public Iterator<Edge<O>> incidentEdges()
	{
		
		return adjacencyList.listIterator(0);
		
	} //end incidentEdges
	
	//This method determines if two vertices are adjacent
	public boolean isAdjacent(Vertex<O> v)
	{
		
		Iterator<Edge<O>> it = this.incidentEdges();
		
		//Loop through all incident edges
		while (it.hasNext())
		{
			
			Edge<O> temp = it.next();
			Vertex<O> endPoint1 = temp.getEndPoint1();
			Vertex<O> endPoint2 = temp.getEndPoint2();
			
			//check if the two vertices are on the same edge in the adjacency list
			if ((this.equals(endPoint1) && v.equals(endPoint2)) || (this.equals(endPoint2) && v.equals(endPoint1)))
			{
				
				return true;
				
			} //end if
						
		} //end while
		
		return false;
		
	} //end isAdjacent
	
	//This method deletes the adjacency list for a vertex, it is public for the MST algorithm
	public void resetEdges()
	{
		
		adjacencyList.clear();
		
	} //end resetEdges
	
	//This method sets the label of a vertex, it is public for the traversal algorithms
	public void setLabel(boolean visit)
	{
		
		visited = visit;
		
	} //end setLabel
	
	//This method returns the label of a vertex, it is public for the traversal algorithms
	public boolean getLabel()
	{
		
		return visited;
		
	} //end getLabel
	
	//This method sets the distance to a vertex, it is public for the MST algorithm
	public void setDistance(Integer d)
	{
		
		distance = d;
		
	} //end setDistance
	
	//This method returns the distance to a vertex, it is public for the MST algorithm
	public Integer getDistance()
	{
		
		return distance;
		
	} //end getDistance
	
	//This method sets the parent edge of a vertex, it is public for the MST algorithm
	public void setParent(Edge<O> p)
	{
		
		parent = p;
		
	} //end setParent
	
	//This method returns the parent edge of a vertex, it is public for the MST algorithm
	public Edge<O> getParent()
	{
		
		return parent;
		
	} //end getParent
	
	//This method sets the position of the vertex in the heap, it is public for the MST algorithm
	public void setLocator(Position l)
	{
		
		locator = l;
		
	} //end setLocator
	
	//This method returns the position of the vertex in the heap, it is public for the MST algorithm
	public Position getLocator()
	{
		
		return locator;
		
	} //end getLocator
	
} //end Vertex