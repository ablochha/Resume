package algorithms;

/**
 * This interface represents a family of algorithms that are
 * performing a similar function. Each of these algorithms is based
 * on the Ford-Fulkerson algorithm for finding maximum flow. Each algorithm
 * uses a different strategy to find the augmenting path.
 * 
 * @author Andrew Bloch-Hansen
 *
 */
import flownetwork.FlowNetwork;
import flownetwork.FlowEdge;

public interface AlgorithmStrategy {

	public boolean hasAugmentingPath(FlowNetwork G, int s, int t);
	
	public FlowEdge getPathTo(int v);
	
	public int getNumMarked();
	
	public boolean getMarker(int v);
	
	public int getNumIterations();
	
	public String getName();
	
} //end AlgorithmStrategy
