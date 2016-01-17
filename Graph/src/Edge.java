/*
 * Author: Andrew Bloch-Hansen
 */

public class Edge<O> {

	private Vertex<O> endPoint1, endPoint2;		//Holds the vertex's at either end of an edge
	private int edgeWeight;						//Holds the weight between two vertices
	private String label;						//Holds the label of an edge
	
	//Constructor to create an edge between two vertices with a given weight
	public Edge(Vertex<O> u, Vertex<O> v, int weight)
	{
		
		endPoint1 = u;
		endPoint2 = v;
		edgeWeight = weight;
		
	} //end Edge
	
	//Returns the first vertex of the edge
	public Vertex<O> getEndPoint1()
	{
		
		return endPoint1;
		
	} //end getEndPoint1
	
	//Returns the second vertex of the edge
	public Vertex<O> getEndPoint2()
	{
		
		return endPoint2;
		
	} //end getEndPoint2
	
	//Returns the weight of an edge
	public int getWeight()
	{
		
		return edgeWeight;
		
	} //end getWeight
	
	//Returns the label of an edge, this is public so that the connectedComponents method can get labels
	public String getLabel()
	{
		
		return label;
		
	} //end getLabel
	
	//Sets the label of an edge, this is public so that the connectedComponents method can set labels
	public void setLabel(String l)
	{
		
		label = l;
		
	} //end setLabel
	
} //end Edge