package flownetwork;

/**
 * This interface represents a flow network.
 * 
 * Course: CS4445
 * @author Andrew Bloch-Hansen
 *
 */
public interface FlowNetworkInterface {
	
	public int numVertices();
	
	public int numEdges();
	
	public void insertEdge(FlowEdge e);
	
	public Iterable<FlowEdge> adj(int vertex);
	
	public Iterable<FlowEdge> edges();
	
	public void drainNetwork();
  
} //end FlowNetworkInterface
