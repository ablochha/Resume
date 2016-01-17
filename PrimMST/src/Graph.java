package asn3;

import java.util.LinkedList;
import java.util.Iterator;

/**
 * This class represents a weighted, undirected graph data structure.
 * 
 * @author Andrew Bloch-Hansen
 *
 * @param <O> the object type being stored
 */
public class Graph<O> implements GraphInterface<O> {
	
	/**
	 * vertices contains a linkedlist of all the vertices in the graph.
	 */
	private LinkedList<Vertex<O>> vertices;	
	
	/**
	 * numVertices, numEdges contain the number of vertices, edges respectively.
	 */
	private int numVertices, numEdges;			

	/**
	 * Constructs a new graph with 0 edges, 0 vertices, and an empty linkedlist.
	 */
	public Graph() {
		
		vertices = new LinkedList<Vertex<O>>();
		numVertices = 0;
		numEdges = 0;
		
	} //end Graph
	
	/**
	 * This method inserts a new vertex into the graph.
	 * @param o the object being inserted
	 */
	public Vertex<O> insertVertex(O o) {
		
		Vertex<O> vertex = new Vertex<O>(o);
		Iterator<Vertex<O>> verticesIt = vertices();
		boolean found = false;
		
		while (verticesIt.hasNext()) {
			
			Vertex<O> v = verticesIt.next();
			
			if (vertex.getObject().equals(v.getObject())) {
				
				vertex = v;
				found = true;
				
			} //end if		
					
		} //end while
		
		if (!found) {
			
			vertices.add(vertex);
			incrementNumVertices();	
			
		} //end if		
		
		return vertex;
		
	} //end insertVertex
	
	/**
	 * This method returns the total number of vertices in the graph.
	 * @return the number of vertices in the graph
	 */
	public int getNumVertices() {
		
		return numVertices;
		
	} //end getNumVertices
	
	/**
	 * This method returns the number of edges in the graph.
	 * @return the number of edges in the graph
	 */
	public int getNumEdges() {
		
		return numEdges;
		
	} //end getNumEdges
	
	/**
	 * This method increase the number of vertices by one.
	 */
	private void incrementNumVertices() {
		
		numVertices++;
		
	} //end incrementNumVertices
	
	/**
	 * This method increases the number of edges by one.
	 */
	private void incrementNumEdges() {
		
		numEdges++;
		
	} //end incrementNumEdges
	
	/**
	 * This method decreases the number of edges by one.
	 */
	private void decrementNumEdges() {
		
		numEdges--;
		
	} //end decrementNumEdges
	
	/**
	 * This method resets the number of edges.
	 */
	private void resetNumEdges() {
		
		numEdges = 0;
		
	} //end resetNumEdges
	
	/**
	 * This method looks for an edge, and returns it.
	 * @param u the first vertex
	 * @param v the second vertex
	 * @return the edge
	 */
	public Edge<O> findEdge(Vertex<O> u, Vertex<O> v) throws GraphException {
			
		
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
	
	/**
	 * This method checks if two vertices are adjacent.
	 * @param v the first vertex
	 * @param u the second vertex
	 * @return boolean indicating if the vertices are adjacent
	 */
	public boolean areAdjacent(Vertex<O> v, Vertex<O> u) throws GraphException {
		
		//make sure both vertices are not empty
		if (u == null || v == null)
			
			throw new GraphException("null pointers");
		
		else
			
			return v.isAdjacent(u);
		
	} //end areAdjacent
	
	/**
	 * This method inserts an edge between vertices in both their adjacency lists
	 * @param v the first vertex
	 * @param u the second vertex
	 * @param weight the weight of the edge
	 */
	public void insertEdge(Vertex<O> v, Vertex<O> u, int weight) throws GraphException {
		
		//Make sure they are not the same edge
		if (u.equals(v))
			
			throw new GraphException("u and v are the same");
		
		//Make sure this edge doesn't already exist
		if (findEdge(v, u) != null)
			
			throw new GraphException("this edge is already in the list");
		
		//The edge does not already exist
		else {
			
			incrementNumEdges();
			v.addAdjacent(new Edge<O>(v, u, weight));
			u.addAdjacent(new Edge<O>(v, u, weight));
			
		} //end else
		
	} //end insertEdge
	
	/**
	 * This method retuns an iterator over all the vertices in the graph.
	 * @return iterator of vertices
	 */
	public Iterator<Vertex<O>> vertices() {
		
		return vertices.listIterator(0);
		
	} //end vertices
	
	/**
	 * This method returns the vertex opposite another vertex on an edge.
	 * @param v the first vertex
	 * @param e the edge
	 * @return the second vertex
	 */
	public Vertex<O> giveOpposite(Vertex<O> v, Edge<O> e) {
		
		//check which endpoint the vertex we are looking at is, return the other one
		if (e.getEndPoint1().getObject().equals(v.getObject()))
			
			return e.getEndPoint2();
		
		else
			
			return e.getEndPoint1();
		
	} //end giveOpposite
	
	//This method computes a minimum spanning tree over the graph
	/**
	 * This method uses Prim's algorithm to compute a minimum spanning tree.
	 * @return an iterator over the MST
	 */
	public Iterator<Edge<O>> MST() {
		
		//Create data structures to build MST		
		Heap<Integer, Vertex<O>> heap = new Heap<Integer, Vertex<O>>(getNumVertices());
		LinkedList<Edge<O>> MSTEdges = new LinkedList<Edge<O>>();
		
		//Variables for MST
		Vertex<O> u;
		Position locator;
		int r;
		
		//Grab all the vertices and the starting point for the MST
		Iterator<Vertex<O>> verticesIT = vertices();
		Vertex<O> s = vertices.getFirst();
		
		//Loop through all vertices
		while (verticesIT.hasNext()) {
			
			Vertex<O> v = verticesIT.next();
			
			if (v.getObject().equals(s.getObject()))
				
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
		while (!heap.isEmpty()) {
			
			//Visit the next smallest vertex
			u = heap.deleteMin();
			u.setLabel(true);;				
		
			//Grab its incident edges
			Iterator<Edge<O>> adjList = u.incidentEdges();
				
			//Loop through its incident edges
			while (adjList.hasNext()) {
				
				Edge<O> e = adjList.next();
				Vertex<O> z = giveOpposite(u, e);
					
				//if an adjacent vertex is not visited
				if (!z.getLabel()) {
					
					//calculate the weight to this adjacent vertex
					r = e.getWeight();
						
					//If this weight is the new lowest weight
					if (r < z.getDistance()) {
						
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
		while (verticesIT.hasNext()) {
			
			Vertex<O> v = verticesIT.next();
			Edge<O> parentEdge = v.getParent();
			
			//If were not at the start vertex
			if (parentEdge != null) {
				
				//Try to insert the MST parent edge of a vertex
				try {
					
					insertEdge(parentEdge.getEndPoint1(), parentEdge.getEndPoint2(), parentEdge.getWeight());
					MSTEdges.add(parentEdge);
					
				} //end try
				
				catch (GraphException e) {
					
					System.out.println(e);
					
				} //end catch
				
			} //end if			
			 
		} //end while
		
		return MSTEdges.listIterator(0);		
		
	} //end MST
	
} //end Graph