package algorithms;

import library.*;
import flownetwork.*;

/**
 * FordFulkerson.java
 * 
 * Course: CS4445
 * Author: Andrew Bloch-Hansen
 * 
 * Several variations of the Ford Fulkerson algorithm are implemented here. 
 *
 */
public class FordFulkerson {

	private static final double FLOATING_POINT_EPSILON = 1E-11;
	
	private AlgorithmStrategy _strategy;	// The augmenting path strategy to use
	private double _maxFlow;				// The maximum flow of the flow network
	
	/**
	 * Computes the maximum flow from s to t in G
	 * @param G the flow network
	 * @param s the source vertex
	 * @param t the sink vertex
	 * @exception IllegalArgumentException if the source=sink or the initial flow is infeasible
	 */
	public FordFulkerson(FlowNetwork G, int s, int t, AlgorithmStrategy strategy) {
		
		// Make sure the vertices are valid
		validate(s, G.numVertices());
		validate(t, G.numVertices());
		
		// Make sure the source and sink are different
		if (s == t) {
			
			throw new IllegalArgumentException("The source and the sink must be different vertices");
			
		} //end if
		
		// Make sure the flow is feasible
		if (!isFeasible(G, s, t)) {
			
			throw new IllegalArgumentException("The initial flow is infeasible");
			
		} //end if
		
		_maxFlow = excess(G, t);
		setStrategy(strategy);
		
		// Loop until there is no more augmenting paths
		while (_strategy.hasAugmentingPath(G, s, t)) {
							
			double bottle = Double.POSITIVE_INFINITY;
							
			// Find the edge with the minimum capacity
			for (int v = t; v != s; v = _strategy.getPathTo(v).other(v)) {
								
				bottle = Math.min(bottle,  _strategy.getPathTo(v).residualCapacityTo(v));
								
			} //end for
							
			// Increase flow along a path from source to sink by the bottleneck value
			for (int v = t; v != s; v = _strategy.getPathTo(v).other(v)) {
								
				_strategy.getPathTo(v).addResidualFlowTo(v, bottle);
								
			} //end for
							
			_maxFlow += bottle;
							
		} //end while
				
		// Make sure the flow is feasible
		if (!isFeasible(G, s, t)) {
							
			System.err.println("Flow is infeasible");
							
		} //end if
		
	} //end FordFulkerson
	
	/**
	 * Sets the augmenting path strategy to use
	 * @param strategy the augmenting path strategy to use
	 */
	public void setStrategy(AlgorithmStrategy strategy) {
		
		_strategy = strategy;
		
	} //end setStrategy
	
	/**
	 * Gets the current augmenting path strategy
	 * @return the current augmenting path strategy
	 */
	public AlgorithmStrategy getStrategy() {
		
		return _strategy;
		
	} //end getStrategy
	
	/**
	 * Returns the value of the maximum flow in the flow network
	 * @return the maximum flow
	 */
	public double value() {
		
		return _maxFlow;
		
	} //end value
	
	/**
	 * Resets the flow in the graph to 0
	 */
	public void resetFlow(FlowNetwork G) {
		
		G.drainNetwork();
		_maxFlow = 0;
		
	} //end resetFlow
	
	/**
	 * Make sure the vertex is valid
	 * @param v the vertex
	 * @param V the number of vertices in the flow network
	 * @exception IndexOutOfBoundsException if the vertex is invalid
	 */
	private void validate(int v, int V) {
		
		// Make sure the vertex is between 0 and V-1
		if (v < 0 || v >= V) {
			
			throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V-1));
			
		} //end if
		
	} //end validate	
	
	/**
	 * Returns the excess flow at a vertex
	 * @param v a vertex
	 * @return the excess flow at a vertex
	 */
	private double excess(FlowNetwork G, int v) {
		
		double excess = 0.0;	// The excess flow 
		
		// Determine the balance of incoming and outgoing flow to the vertex
		for (FlowEdge e : G.adj(v)) {
			
			// The vertex has outgoing flow on this edge
			if (v == e.from()) {
				
				excess -= e.flow();
				
			} //end if
			
			// The vertex has incoming flow on this edge
			else {
				
				excess += e.flow();
				
			} //end else
			
		} //end for
		
		return excess;
		
	} //end excess
	
	/**
	 * Checks if a flow is feasible for a given flow network
	 * @param G the flow network
	 * @param s the source vertex
	 * @param t the sink vertex
	 * @return true if the flow is feasible, false otherwise
	 */
	private boolean isFeasible(FlowNetwork G, int s, int t) {
		
		// Verify that the capacity constraint is satisfied
		// For every vertex
		for (int v = 0; v < G.numVertices(); v++) {
			
			// Check each edge incident on that vertex
			for (FlowEdge e : G.adj(v)) {
				
				// Make sure that the flow is not more than the capacity
				if (e.flow() < -FLOATING_POINT_EPSILON || e.flow() > e.capacity() + FLOATING_POINT_EPSILON) {
					
					System.err.println("Edge does not satisfy capacity constraints: " + e);
					return false;
					
				} //end if
				
			} //end for
			
		} //end for
		
		// Verify that the conservation of flow is satisfied
		// The outgoing flow from the source must equal the max flow
		if (Math.abs(_maxFlow + excess(G, s)) > FLOATING_POINT_EPSILON) {
			
			System.err.println("Excess at source = " + excess(G, s));
			System.err.println("Max flow         = " + _maxFlow);
			
		} //end if
		
		// The incoming flow to the sink must equal the max flow
		if (Math.abs(_maxFlow - excess(G, t)) > FLOATING_POINT_EPSILON) {
			
			System.err.println("Excess at source = " + excess(G, t));
			System.err.println("Max flow         = " + _maxFlow);
			
		} //end if
		
		// The net flow at each other vertex must be 0
		// Loop through all the vertices
		for (int v = 0; v < G.numVertices(); v++) {
			
			// Skip source and sink
			if (v == s || v == t) {
				
				continue;
				
			} //end if
			
			// The net flow must be 0
			else if (Math.abs(excess(G, v)) > FLOATING_POINT_EPSILON) {
				
				System.err.println("Net flow out of " + v + " doesn't equal zero");
				return false;
				
			} //end else if
			
		} //end for
		
		return true;
		
	} //end isFeasible
	
} //end FordFulkerson
