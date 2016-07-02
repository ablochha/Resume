package algorithms;

import flownetwork.FlowEdge;
import flownetwork.FlowNetwork;
import datastructures.*;

/**
 * ArbitraryStrategy.java
 * 
 * This algorithm chooses arbitrary augmenting path.
 * 
 * @author Andrew Bloch-Hansen
 *
 */
public class ArbitraryStrategy implements AlgorithmStrategy {
	
	private boolean []_marked;			// Marks used to check for augmenting paths
	private FlowEdge []_edgeTo;			// A list of edges in the augmenting path
	private int _iterations;			// The number of iterations needed to compute max flow
	private String _name = "Arbitrary";	// The name of the strategy

	@Override
	/**
	 * Checks if the flow network has an augmenting path by doing a breadth-first search
	 * and seeing if the sink is reachable from the source, creates a list of the path
	 * @param G the flow network
	 * @param s the source vertex
	 * @param t the sink vertex
	 * @param variation the variation of the algorithm to use
	 * @return true if there is an augmenting path, false otherwise
	 */
	public boolean hasAugmentingPath(FlowNetwork G, int s, int t) {
		
		_edgeTo = new FlowEdge[G.numVertices()];	// List of edges to make an augmenting path
		_marked = new boolean[G.numVertices()];		// Mark each vertex that we include in the path
		
		Bag<FlowEdge> paths = new Bag<FlowEdge>();
		
		Queue<Integer> queue = new Queue<Integer>();
		queue.enqueue(s);
		_marked[s] = true;
		
		// Breadth-first search
		while (!queue.isEmpty() && !_marked[t]) {
			
			int u = queue.dequeue();
			
			// Visit all an edges adjacent to u
			for (FlowEdge e: G.adj(u)) {
				
				int v = e.other(u);
				
				// Check if flow can be added on this edge
				if (e.residualCapacityTo(v) > 0) {
					
					// Check if it's already in the path
					if (!_marked[v]) {
						
						_edgeTo[v] = e;
						_marked[v] = true;
						queue.enqueue(v);
						
					} //end if
					
					// Keep track of paths that reach the sink
					if (v == t) {
						
						paths.add(e);
						
					} //end if
					
				} //end if
			
			} //end for
			
		} //end while
		
		// Choose an arbitrary path
		if (_marked[t]) {
			
			_iterations++;
			_edgeTo[t] = paths.iterator().next();
			
		} //end if
		
		// If we were able to mark the sink as reachable, there is an augmenting path
		return _marked[t];
		
	} //end hasAugmentingPath
	
	@Override
	/**
	 * Returns the edge in the path leading to a vertex
	 * @param v a vertex
	 * @return the edge leading to the vertex
	 */
	public FlowEdge getPathTo(int v) {
		
		return _edgeTo[v];
		
	} //end getPathTo
	
	@Override
	/**
	 * Returns the number of reachable vertices in the residual network
	 * @return the number of reachable vertices
	 */
	public int getNumMarked() {
		
		return _marked.length;
		
	} //end getMarkedSize
	
	@Override 
	/**
	 * Returns whether a vertex was reachable in the residual graph
	 * @param v a vertex
	 * @return true if the vertex is reachable, false otherwise
	 */
	public boolean getMarker(int v) {
		
		return _marked[v];
		
	} //end getMarker
	
	@Override
	/**
	 * Return the number of iterations needed to find the maximum flow
	 * @return the number of iterations
	 */
	public int getNumIterations() {
		
		return _iterations;
		
	} //end getNumIterations
	
	@Override
	/**
	 * Returns the name of the strategy
	 * @return the name of the strategy
	 */
	public String getName() {
		
		return _name;
		
	} //end getName
	
} //end ArbitraryStrategy
