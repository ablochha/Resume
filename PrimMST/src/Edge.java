package asn3;

/**
 * This class represents an edge between two vertices.
 * 
 * @author Andrew Bloch-Hansen
 */
public class Edge<O> {

	/**
	 * endPoints contain references to the vertices of the edge.
	 */
	private Vertex<O> endPoint1, endPoint2;		
	
	/**
	 * edgeWeight contains the weight of the edge.
	 */
	private int edgeWeight;						
	
	/**
	 * This constructor creates an edge between two vertices with a given weight.
	 * @param u the first vertex
	 * @param v the second vertex
	 * @param weight the weight of the edge
	 */
	public Edge(Vertex<O> u, Vertex<O> v, int weight) {
		
		endPoint1 = u;
		endPoint2 = v;
		edgeWeight = weight;
		
	} //end Edge
	
	/**
	 * Returns the first vertex of the edge.
	 * @return the first vertex
	 */
	public Vertex<O> getEndPoint1() {
		
		return endPoint1;
		
	} //end getEndPoint1
	
	/**
	 * Returns the second vertex of the edge.
	 * @return the second vertex
	 */
	public Vertex<O> getEndPoint2() {
		
		return endPoint2;
		
	} //end getEndPoint2
	
	/**
	 * Returns the weight of an edge.
	 * @return the weight of the edge
	 */
	public int getWeight() {
		
		return edgeWeight;
		
	} //end getWeight
	
} //end Edge