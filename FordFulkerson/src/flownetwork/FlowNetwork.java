package flownetwork;

import library.*;
import datastructures.*;

/**
 * FlowNetwork.java
 * 
 * Course: CS4445
 * Author: Andrew Bloch-Hansen
 * 
 * This FlowNetwork is based on the implementation in the textbook 'Algorithms, 4th Edition'
 * by Robert Sedgewick and Kevin Wayne. A FlowNetwork is a directed, weighted graph. Each edge
 * has a 'flow' and a 'capacity' associated with it. The flow cannot exceed the capacity, 
 * and the incoming flow must equal the outgoing flow for all nodes except the first and last.
 *
 */
public class FlowNetwork implements FlowNetworkInterface {
	
	private static final String NEWLINE = System.getProperty("line.separator");
	
	private final int _vertices;				// The number of vertices in the flow network
	private int _edges;							// The number of edges in the flow network
	private Bag<FlowEdge> []_adjacencyList;		// The list of incident edges for each vertex

	/**
	 * Declare a FlowNetwork with the number of vertices given
	 * @param vertices the number of vertices in the flow network
	 * @exception IllegalArgumentException if a negative number of vertices is given
	 */
	public FlowNetwork(int vertices) {
		
		// Make sure the number of vertices isn't negative
		if (vertices < 0) {
			
			throw new IllegalArgumentException("Choose a nonnegative number of vertices");
			 
		} //end if
		
		_vertices = vertices;
		_edges = 0;
		_adjacencyList = (Bag<FlowEdge>[]) new Bag[vertices];
		
		// Initialize the adjacency list
		for (int i = 0; i < vertices; i++) {
			
			_adjacencyList[i] = new Bag<FlowEdge>();
			
		} //end for
		
	} //end FlowNetwork
	
	/**
	 * Declare a FlowNetwork with the number of vertices and edges given
	 * @param vertices the number of vertices in the flow network
	 * @param edges the number of edges in the flow network
	 * @exception IllegalArgumentException if a negative number of edges is given
	 */
	public FlowNetwork(int vertices, int edges) {
		
		// Call the previous constructor to initializes the vertices and adjacency list
		this(vertices);		
		
		// Make sure the number of edges isn't negative
		if (edges < 0) {
			
			throw new IllegalArgumentException("Choose a nonnegative number of edges");
			
		} //end if
		
		// Initialize a random flow network by inserting randomly chosen edges
		for (int i = 0; i < edges; i++) {
			
			int u = StdRandom.uniform(vertices);
			int v = StdRandom.uniform(vertices);
			double capacity = StdRandom.uniform(100);
			insertEdge(new FlowEdge(u, v, capacity));
			
		} //end for
		
	} //end FlowNetwork
	
	/**
	 * Declare a FlowNetwork from a file with the following format
	 * #vertices #edges vertex vertex capacity vertex vertex capacity
	 * @param in the input stream
	 * @exception IllegalArgumentException if a negative number of edges is given
	 */
	public FlowNetwork(In in) {
		
		this(in.readInt());			// Initialize the vertices and adjacency list
		int edges = in.readInt();	// Read the number of edges
		
		// Make sure the number of edges isn't negative
		if (edges < 0) {
			
			throw new IllegalArgumentException("Choose a nonnegative number of edges");
			
		} //end if
		
		// Read each edge from the input
		for (int i = 0; i < edges; i++) {
			
			int u = in.readInt();
			int v = in.readInt();
			
			// Make sure the vertices are valid
			validateVertex(u);
			validateVertex(v);
			
			// Insert the edge
			double capacity = in.readDouble();
			insertEdge(new FlowEdge(u, v, capacity));
			
		} //end for
		
	} //end FlowNetwork
	
	/**
	 * Returns the number of vertices in the flow network
	 * @return the number of vertices
	 */
	public int numVertices() {
		
		return _vertices;
		
	} //end numVertices
	
	/**
	 * Returns the number of edges in the flow network
	 * @return the number of edges
	 */
	public int numEdges() {
		
		return _edges;
		
	} //end numEdges
	
	/**
	 * Checks to see if the given vertex is valid
	 * @param vertex the input vertex
	 * @exception IndexOutOfBoundsException if the given vertex is not in the correct range
	 */ 
	private void validateVertex(int vertex) {
		
		// Make sure the vertex is between 0 and V-1
		if (vertex < 0 || vertex >= _vertices) {
			
			throw new IndexOutOfBoundsException("vertex" + vertex + " is not between 0 and " + (_vertices-1));
			
		} //end if
		
	} //end validateVertex
	
	/**
	 * Inserts a new edge into the flow network
	 * @param e the edge to be inserted
	 */
	public void insertEdge(FlowEdge e) {
		
		int u = e.from();	// Get the first vertex of the edge
		int v = e.to();		// Get the second vertex of the edge
		
		// Make sure the vertices are valid
		validateVertex(u);
		validateVertex(v);
		
		_adjacencyList[u].add(e);
		_adjacencyList[v].add(e);
		
		_edges++;
		
	} //end insertEdge
	
	/**
	 * Returns an iterator over the incident edges of a vertex
	 * @param vertex the vertex to get the incident edges of
	 * @return an iterator over the incident edges
	 */
	public Iterable<FlowEdge> adj(int vertex) {
		
		validateVertex(vertex);
		return _adjacencyList[vertex];
		
	} //end adj
	
	/**
	 * Returns an iterator over all of the edges in the flow network
	 * @return an iterator over all of the edges
	 */
	public Iterable<FlowEdge> edges() {
		
		Bag<FlowEdge> list = new Bag<FlowEdge>();	// The list of edges
			
		// Loop through all the vertices
		for (int i = 0; i < _vertices; i++) {
			
			// Loop through all of the edges incident on that vertex
			for (FlowEdge e : adj(i)) {
				
				// Add edges that have an outgoing edge from that vertex
				if (e.to() != i) {
					
					list.add(e);
					
				} //end if
				
			} //end for
			
		} //end for
		
		return list;
		
	} //end edges
	
	/**
	 * Drains the flow from the network
	 */
	public void drainNetwork() {
		
		// Drain every edge
		for (FlowEdge e : edges()) {
			
			e.drainEdge();
			
		} //end for
		
	} //end drainNetwork
	
	/**
	 * Output the flow network as a string
	 * @return the string format of the flow network
	 */
	public String toString() {
		
		StringBuilder s = new StringBuilder();	// The string form of the flow network
		
		s.append(_vertices + " " + _edges + NEWLINE);
		
		// Loop through all of the vertices in the flow network
		for (int i = 0; i < _vertices; i++) {
			
			s.append(i + ": ");
			
			// Loop through all of the edges incident on that vertex
			for (FlowEdge e : _adjacencyList[i]) {
				
				// Output edges that have an outgoing edge from that vertex
				if (e.to() != i) {
					
					s.append(e + " ");
					
				} //end if
				
			} //end for
			
			s.append(NEWLINE);
			
		} //end for
		
		return s.toString();
		
	} //end toString
	
} //end FlowNetwork
