/*
 * by olga veksler
 */

//package imagesegment;

import java.util.LinkedList;
import java.util.Iterator;
import java.util.Vector;
import java.util.TreeSet;

public interface GraphInterface<VertexObject> {
    public Vertex<VertexObject> insertVertex( VertexObject o );
    public int getNumVertices();
    public int getNumEdges();
    public Edge<VertexObject> findEdge(Vertex<VertexObject> u, Vertex<VertexObject> v) throws GraphException;
    public boolean areAdjacent(Vertex<VertexObject> v, Vertex<VertexObject> u) throws GraphException;
    public void insertEdge(Vertex<VertexObject> v, Vertex<VertexObject> u, int weight)  throws GraphException;
    public void deleteEdge(Edge<VertexObject> e)  throws GraphException;
    public Iterator<Vertex<VertexObject>> vertices(); 
    public Vertex<VertexObject> giveOpposite(Vertex<VertexObject> v, Edge<VertexObject> e);
    // returns iterator over iterators of connected components of the graph
    public Iterator<Iterator<Vertex<VertexObject>>> ConnectedComponents();
    // returns iterator over edges in the Minimum spanning tree of the graph 
    public Iterator<Edge<VertexObject>> MST();
}