package flownetwork;

/**
 * This interface represents an edge in a flow network. An 
 * edge has two vertices u and v, a capacity, and a flow.
 * 
 * @author Andrew Bloch-Hansen
 *
 */
public interface FlowEdgeInterface {

	public int from();
	
	public int to();
	
	public double capacity();
	
	public double flow();
	
	public void drainEdge();
	
	public int other(int vertex);
	
	public double residualCapacityTo(int vertex);
	
	public void addResidualFlowTo(int vertex, double delta);
	
} //end FlowEdgeInterface
