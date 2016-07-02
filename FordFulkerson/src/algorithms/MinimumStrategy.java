package algorithms;

import flownetwork.FlowEdge;
import flownetwork.FlowNetwork;
import datastructures.*;

/**
 * MinimumStrategy.java
 * 
 * This algorithm chooses the augmenting path with the minimum sum of capacities.
 * 
 * @author Andrew Bloch-Hansen
 *
 */
public class MinimumStrategy implements AlgorithmStrategy {
	
	private boolean []_marked;			// Marks used to check for augmenting paths
	private FlowEdge []_edgeTo;			// A list of edges in the augmenting path
	private int _iterations;			// The number of iterations needed to compute max flow
	private String _name = "Minimum";	// The name of the strategy
	
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
		
		HeapPQ<Double, Integer> heap = new HeapPQ<Double, Integer>(G.numVertices(), new DoubleComparator());
		
		_edgeTo = new FlowEdge[G.numVertices()];	// List of edges to make an augmenting path
		_marked = new boolean[G.numVertices()];		// Mark each vertex that we include in the path
		double totalPathResidualCapacity[] = new double[G.numVertices()];
		Position locator[] = new Position[G.numVertices()];
		
		int u;
		int v;
		
		totalPathResidualCapacity[s] = 0;
		_marked[s] = true;
		
		// Set all the totalPathCapacity values to 0
		for (int i = 0; i < G.numVertices(); i++) {
		
			if (i > 0)
				totalPathResidualCapacity[i] = Double.POSITIVE_INFINITY;
			
			locator[i] = heap.insert(totalPathResidualCapacity[i], i);
				
		} //end for
		
		// Loop while there are still vertices that are in the heap
		while (!heap.isEmpty()) {
					
			u = heap.removeMin();
					
			// Grab the adjacent edges
			for (FlowEdge e : G.adj(u)) {
						
				v = e.other(u);
						
				// Check if flow can be added on this edge
				if (e.residualCapacityTo(v) > 0) {
					//StdOut.println(maxPathResidualCapacity[v] + " " + maxPathResidualCapacity[u] + " " + e.residualCapacityTo(v));
					// Choose paths with a minimum sum of residual capacities
					if (totalPathResidualCapacity[v] > totalPathResidualCapacity[u] + e.residualCapacityTo(v)) {
								
						totalPathResidualCapacity[v] = totalPathResidualCapacity[u] + e.residualCapacityTo(v);
						_edgeTo[v] = e;
						_marked[v] = true;
						heap.decreaseKey(locator[v], totalPathResidualCapacity[v]);
							
					} //end if
						
				} //end if
						
			} //end for
					
		} //end while
	
		/*Queue<Integer> queue = new Queue<Integer>();
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
				
					// Choose paths with a minimum sum of residual capacities
					if ((totalPathResidualCapacity[v] == 0) ||
							(_marked[v] && (totalPathResidualCapacity[v] > totalPathResidualCapacity[u] + e.residualCapacityTo(v)))) {
					
						totalPathResidualCapacity[v] = totalPathResidualCapacity[u] + e.residualCapacityTo(v);
						_edgeTo[v] = e;
						_marked[v] = true;
						queue.enqueue(v);
					
					} //end if
				
				} //end if
		
			} //end for
		
		} //end while*/
		
		// Count how many paths it took
		if (_marked[t]) {
					
			_iterations++;
					
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

} //end MinimumStrategy
