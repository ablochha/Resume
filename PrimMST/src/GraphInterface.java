package asn3;

import java.util.Iterator;

/**
 * This interface represents a weighted, undirected graph data structure.
 * 
 * @author Andrew Bloch-Hansen
 *
 * @param <VertexObject> the object type being stored
 */
public interface GraphInterface<VertexObject> {
	
  public Vertex<VertexObject> insertVertex( VertexObject o );
  
  public int getNumVertices();
  
  public int getNumEdges();
  
  public Edge<VertexObject> findEdge(Vertex<VertexObject> u, Vertex<VertexObject> v) throws GraphException;
  
  public boolean areAdjacent(Vertex<VertexObject> v, Vertex<VertexObject> u) throws GraphException;
  
  public void insertEdge(Vertex<VertexObject> v, Vertex<VertexObject> u, int weight)  throws GraphException;
  
  public Iterator<Vertex<VertexObject>> vertices();

  public Vertex<VertexObject> giveOpposite(Vertex<VertexObject> v, Edge<VertexObject> e);
  
  public Iterator<Edge<VertexObject>> MST();
  
} //end GraphInterface