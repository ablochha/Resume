/*
 * Author: Andrew Bloch-Hansen
 */

import java.util.LinkedList;
import java.util.Iterator;
import java.util.Vector;
import java.util.TreeSet;

public class Graph<O> implements GraphInterface<O>{
	
	private LinkedList<Vertex<O>> vertices;		//Linked list of all the vertices in the graph
	private int numVertices, numEdges;			//Counters for vertices and edges

	//Constructor that creates an empty graph and initializes it
	public Graph()
	{
		
		vertices = new LinkedList<Vertex<O>>();
		numVertices = 0;
		numEdges = 0;
		
	} //end Graph
	
	//This method inserts a new vertex into the graph.
	public Vertex<O> insertVertex(O o)
	{
		
		Vertex<O> vertex = new Vertex<O>(o);
		vertices.add(vertex);
		incrementNumVertices();
		return vertex;
		
	} //end insertVertex
	
	//This method returns the total number of vertices in the graph
	public int getNumVertices()
	{
		
		return numVertices;
		
	} //end getNumVertices
	
	//This method returns the total number of edges in the graph
	public int getNumEdges()
	{
		
		return numEdges;
		
	} //end getNumEdges
	
	//This method increases the number of vertices
	private void incrementNumVertices()
	{
		
		numVertices++;
		
	} //end incrementNumVertices
	
	//This method increases the number of edges
	private void incrementNumEdges()
	{
		
		numEdges++;
		
	} //end incrementNumEdges
	
	//This method decreases the number of edges
	private void decrementNumEdges()
	{
		
		numEdges--;
		
	} //end decrementNumEdges
	
	//This method resets the number of edges
	private void resetNumEdges()
	{
		
		numEdges = 0;
		
	} //end resetNumEdges
	
	//This method looks for an edge and returns it
	public Edge<O> findEdge(Vertex<O> u, Vertex<O> v) throws GraphException
	{
			
		
		//Make sure both vertices are not empty
		if (u == null || v == null)
			
			throw new GraphException("null pointers");
		
		//Get the adjacency list for the vertex
		Iterator<Edge<O>> it = u.incidentEdges();
			
		//Loop through the incident edges
		while (it.hasNext())
		{
			
			//get the endpoints
			Edge<O> temp = it.next();
			Vertex<O> endPoint1 = temp.getEndPoint1();
			Vertex<O> endPoint2 = temp.getEndPoint2();
			
			//check if our input vertices match the endpoints of the edge we are looking at
			if ((u.equals(endPoint1) && v.equals(endPoint2)) || (u.equals(endPoint2) && v.equals(endPoint1)))
			{
					
				return temp;
					
			} //end if				
				
		} //end while
		
		return null;		
		
	} //end findEdge
	
	//This method checks if two vertices are adjacent
	public boolean areAdjacent(Vertex<O> v, Vertex<O> u) throws GraphException
	{
		
		//make sure both vertices are not empty
		if (u == null || v == null)
			
			throw new GraphException("null pointers");
		
		else
			
			return v.isAdjacent(u);
		
	} //end areAdjacent
	
	//This method inserts an edge between vertices in both their adjacency lists
	public void insertEdge(Vertex<O> v, Vertex<O> u, int weight) throws GraphException
	{
		
		//Make sure they are not the same edge
		if (u.equals(v))
			
			throw new GraphException("u and v are the same");
		
		//Make sure this edge doesn't already exist
		if (findEdge(v, u) != null)
			
			throw new GraphException("this edge is already in the list");
		
		//The edge does not already exist
		else	
		{
			
			incrementNumEdges();
			v.addAdjacent(new Edge<O>(v, u, weight));
			u.addAdjacent(new Edge<O>(v, u, weight));
			
		} //end else
		
	} //end insertEdge
	
	//This method removes an edge from both vertices adjacency lists
	public void deleteEdge(Edge<O> e) throws GraphException
	{
		
		//If we found the edge
		if (findEdge(e.getEndPoint1(), e.getEndPoint2()) != null)
		{
			
			e.getEndPoint1().deleteAdjacentEdge(e);
			e.getEndPoint2().deleteAdjacentEdge(e);
			
			decrementNumEdges();
			
		} //end if	
		
		//We did not find the edge
		else
			
			throw new GraphException("This edge was not in the graph");
		
	} //end deleteEdge
	
	//This method returns an iterator over all the vertices in the graph
	public Iterator<Vertex<O>> vertices()
	{
		
		return vertices.listIterator(0);
		
	} //end vertices
	
	//This method returns the vertex opposite another vertex on an edge
	public Vertex<O> giveOpposite(Vertex<O> v, Edge<O> e)
	{
		
		//check which endpoint the vertex we are looking at is, return the other one
		if (e.getEndPoint1().equals(v))
			
			return e.getEndPoint2();
		
		else
			
			return e.getEndPoint1();
		
	} //end giveOpposite
	
	//This method returns an iterator over all segments and associated vertices
	public Iterator<Iterator<Vertex<O>>> ConnectedComponents()
	{
		
		//Create the lists for segments and vertices
		LinkedList<Iterator<Vertex<O>>> connectedComponents = new LinkedList<Iterator<Vertex<O>>>();
		LinkedList<Vertex<O>> component;
		
		//Grab all the vertices in the graph
		Iterator<Vertex<O>> verticesIT = vertices();
		
		//Loop through all the vertices
		while(verticesIT.hasNext())
		{
			
			//Set all vertices to unvisited
			Vertex<O> v = verticesIT.next();
			v.setLabel(false);
			
			//Grab all incident edges of a vertex
			Iterator<Edge<O>> edgesIT = v.incidentEdges();
			
			//Loop through all incident edges
			while (edgesIT.hasNext())
			{
				
				//Set all edges to unexplored
				Edge<O> e = edgesIT.next();
				e.setLabel("UNEXPLORED");
				
			} //end while
			
		} //end while
		
		//Get all the vertices again
		verticesIT = vertices();
		
		//Loop through all the vertices
		while(verticesIT.hasNext())
		{
			
			Vertex<O> v = verticesIT.next();
			
			//If a vertex is not already visited
			if (!v.getLabel())
			{
				
				//Create a linked list for the vertices in the segment about to be explored
				component = new LinkedList<Vertex<O>>();
				component = DFS(v, component);
				
				//Add this segment to a linked list of segments
				connectedComponents.add(component.listIterator(0));
				
			} //end if
			
		} //end while
		
		Iterator<Iterator<Vertex<O>>> connectedIT = connectedComponents.listIterator(0);
		return connectedIT;
		
	} //end DFS
	
	//This method explores a segment of a graph
	private LinkedList<Vertex<O>> DFS(Vertex<O> v, LinkedList<Vertex<O>> component)
	{
		
		//Visit a node and add it to the list of visited vertices
		v.setLabel(true);
		component.add(v);
		
		//Grab all the incident edges
		Iterator<Edge<O>> adjList = v.incidentEdges();
		
		//Loop through all incident edges of a vertex
		while (adjList.hasNext())
		{
			
			//Get the vertex on the other side of the edge
			Edge<O> e = adjList.next();
			Vertex<O> w = giveOpposite(v, e);
			
			//If the vertex has not been visited yet
			if (!w.getLabel())
			{
				
				//Call DFS on this vertex to visit it
				e.setLabel("DISCOVERY");
				component = DFS(w, component);
				
			} //end if
			
			//If the vertex has been visited but not using this edge
			else if (e.getLabel().equals("UNEXPLORED"))
			
					e.setLabel("BACK");
						
		} //end while	
		
		return component;
		
	} //end DFS
	
	//This method computes a minimum spanning tree over the graph
	public Iterator<Edge<O>> MST()
	{
		
		//Create data structures to build MST
		HeapPQ<Integer, Vertex<O>> heap = new HeapPQ<Integer, Vertex<O>>(getNumVertices(), new IntegerComparator());
		LinkedList<Edge<O>> MSTEdges = new LinkedList<Edge<O>>();
		
		//Variables for MST
		Position locator;
		Vertex<O> u;
		int r;
		
		//Grab all the vertices and the starting point for the MST
		Iterator<Vertex<O>> verticesIT = vertices();
		Vertex<O> s = vertices.getFirst();
		
		//Loop through all vertices
		while (verticesIT.hasNext())
		{
			
			Vertex<O> v = verticesIT.next();
			
			if (v.equals(s))
				
				v.setDistance(0);
			
			else
				
				v.setDistance(Integer.MAX_VALUE);
			
			//Initialize variables
			v.setParent(null);
			v.setLabel(false);
			
			//Put all the vertices in the heap
			locator = heap.insert(v.getDistance(), v);
			v.setLocator(locator);
			
		} //end while
			
		//Loop while there are still vertices that are not apart of the MST
		while (!heap.isEmpty())
		{
				
			//Visit the next smallest vertex
			u = heap.removeMin();
			u.setLabel(true);;				
				
			//Grab its incident edges
			Iterator<Edge<O>> adjList = u.incidentEdges();
				
			//Loop through its incident edges
			while (adjList.hasNext())
			{
					
				Edge<O> e = adjList.next();
				Vertex<O> z = giveOpposite(u, e);
					
				//if an adjacent vertex is not visited
				if (!z.getLabel())
				{
						
					//calculate the weight to this adjacent vertex
					r = e.getWeight();
						
					//If this weight is the new lowest weight
					if (r < z.getDistance())
					{
							
						//update the distance and set the MST parent edge
						z.setDistance(r);
						z.setParent(new Edge<O>(z, u, r));
						heap.decreaseKey(z.getLocator(), r);
							
					} //end if
						
				} //end if
								
			} //end while							
			
		} //end while
		
		resetNumEdges();
		verticesIT = vertices();
		
		//Remove all the edges in the graph 
		while (verticesIT.hasNext())
			
			verticesIT.next().resetEdges();
		
		verticesIT = vertices();
		
		//add the MST edges
		while (verticesIT.hasNext())
		{
			
			Vertex<O> v = verticesIT.next();
			Edge<O> parentEdge = v.getParent();
			
			//If were not at the start vertex
			if (parentEdge != null)
			{
				
				//Try to insert the MST parent edge of a vertex
				try
				{
					
					insertEdge(parentEdge.getEndPoint1(), parentEdge.getEndPoint2(), parentEdge.getWeight());
					MSTEdges.add(parentEdge);
					
				} //end try
				
				catch (GraphException e)
				{
					
					System.out.println(e);
					
				} //end catch
				
			} //end if			
			 
		} //end while
		
		return MSTEdges.listIterator(0);		
		
	} //end MST
	
} //end Graph