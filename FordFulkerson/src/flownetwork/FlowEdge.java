package flownetwork;

/**
 * FlowEdge.java
 * 
 * Course: CS4445
 * Author: Andrew Bloch-Hansen
 * 
 * This FlowEdge is based on the implementation in the textbook 'Algorithms, 4th Edition'
 * by Robert Sedgewick and Kevin Wayne. A FlowEdge is a directed, weighted edge. Each edge
 * has a 'flow' and a 'capacity' associated with it. The flow cannot exceed the capacity, 
 * and the incoming flow must equal the outgoing flow for all nodes except the first and last.
 *
 */
public class FlowEdge {
	
	private final int _u;				// The first vertex of the edge
	private final int _v;				// The second vertex of the edge
	private final double _capacity;		// The capacity of the edge
	private double _flow;				// The flow of the edge
	
	/**
	 * Declares a new FlowEdge with 0 flow
	 * @param u the first vertex of the edge
	 * @param v the second vertex of the edge
	 * @param capacity the capacity of the edge
	 * @exception IndexOutOfBoundsException if either vertex is negative
	 * @exception IllegalArgumentException if the edge capacity is negative
	 */
	public FlowEdge(int u, int v, double capacity) {
		
		// Make sure positive integers are used to identify vertices
		if (u < 0) {
			
			throw new IndexOutOfBoundsException("Choose positive integers for vertices");
			
		} //end if
		
		// Make sure positive integers are used to identify vertices
		if (v < 0) {
			
			throw new IndexOutOfBoundsException("Choose positive integers for vertices");
			
		} //end if
		
		// Make sure the edge capacity is positive
		if (!(capacity >= 0.0)) {
			
			throw new IllegalArgumentException("Choose positive number for edge capacity");
			
		} //end if
		
		// Initialize the FlowEdge with 0 flow
		_u = u;
		_v = v;
		_capacity = capacity;
		_flow = 0.0;
		
	} //end FlowEdge
	
	/**
	 * Copy another edge
	 * @param e the edge to copy
	 */
	public FlowEdge(FlowEdge e) {
		
		_u = e._u;
		_v = e._v;
		_capacity = e._capacity;
		_flow = e._flow;
		
	} //end FlowEdge
	
	/**
	 * Returns the first vertex of an edge
	 * @return the first vertex
	 */
	public int from() {
		
		return _u;
		
	} //end from
	
	/**
	 * Returns the second vertex of an edge
	 * @return the second vertex
	 */
	public int to() {
		
		return _v;
		
	} //end to
	
	/**
	 * Returns the capacity of an edge
	 * @return the capacity
	 */
	public double capacity() {
		
		return _capacity;
		
	} //end capacity
	
	/**
	 * Returns the flow of an edge
	 * @return the flow
	 */
	public double flow() {
		
		return _flow;
		
	} //end flow
	
	/**
	 * Resets the flow of an edge to 0
	 */
	public void drainEdge() {
		
		_flow = 0;
		
	} //end
	
	/**
	 * Returns the other vertex on an edge
	 * @param vertex one end of the edge
	 * @exception IllegalArgumentException if the vertex given is not on the edge
	 * @return the other end of the edge
	 */
	public int other(int vertex) {
		
		// Return the other vertex
		if (vertex == _u) {
			
			return _v;
			
		} //end if
		
		// Return the other vertex
		else if (vertex == _v) {
			
			return _u;
			
		} //end else if
		
		// The vertex is not on the edge
		else {
			
			throw new IllegalArgumentException("Not a valid vertex");
			
		} //end else
		
	} //end other
	
	/**
	 * Returns the residual capacity of the edge towards the vertex
	 * @param vertex the vertex to check
	 * @exception IllegalArgumentException if the vertex is invalid
	 * @return the residual capacity
	 */
	public double residualCapacityTo(int vertex) {
		
		// Returns the backward edge
		if (vertex == _u) {
			
			return _flow;
			
		} //end if
		
		// Returns the forward edge
		else if (vertex == _v) {
			
			return _capacity - _flow;
			
		} //end else if
		
		else {
			
			throw new IllegalArgumentException("Not a valid vertex");
			
		} //end else
		
	} //end residualCapacityTo
	
	/**
	 * Increases the flow to a vertex
	 * @param vertex the vertex to add flow to
	 * @param flow the flow to add
	 * @exception IllegalArgumentException if the vertex or flow are invalid, 
	 * or if the flow is negative or exceeds the capacity
	 */
	public void addResidualFlowTo(int vertex, double flow) {
		
		// Adds flow to the backward edge
		if (vertex == _u) {
			
			_flow -= flow;
			
		} //end if
		 
		// Adds flow to the forward edge
		else if (vertex == _v) {
			
			_flow += flow;
			
		} //end else if
		
		else {
			
			throw new IllegalArgumentException("Not a valid vertex");
			
		} //end else
		
		// Make sure the increase in flow is a valid number
		if (Double.isNaN(flow)) {
			
			throw new IllegalArgumentException("Not a valid change in flow: Nan");
			
		} //end if
		
		// Make sure the flow does not exceed the capacity
		if (!(_flow <= _capacity)) {
					
			throw new IllegalArgumentException("Flow must not exceed capacity for an edge");
					
		} //end if
				
		// Make sure the flow is positive
		if (!(_flow >= 0.0)) {
					
			throw new IllegalArgumentException("Flow must be positive");
					
		} //end if
				
	} //end addResidualFlowTo
	
	/**
	 * Outputs the edge as a string
	 * @return the string representation of the edge
	 */
	public String toString() {
		
		return _u + "->" + _v + " " + _flow + "/" + _capacity;
		
	} //end toString
	
} //end FlowEdge
