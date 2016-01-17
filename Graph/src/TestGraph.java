/*
 * by olga veksler
 */

//package imagesegment;

import java.util.*; 

public class TestGraph  {
   public static void main(String[] args) throws GraphException {
       
       /////////////////////Test 1, insert some nodes
       {
           int size = 10;
           Graph<Integer> g = new Graph<Integer>();
           Vector<Vertex<Integer>> lookup  = new Vector<Vertex<Integer>>(10);
           
           for (int i = 0;i < size; i++ )
                lookup.add(i,g.insertVertex(new Integer(i)));
            
            
            if ( g.getNumVertices() == size )
                System.out.println("Test 1 passed");
            else System.out.println("**********Test 1 failed");
       }
       
  
     /////////////////////Test 2, insert some nodes and edges
       try{
           int size = 10;
           Graph<Integer> g = new Graph<Integer>();
           Vector<Vertex<Integer>> lookup  = new Vector<Vertex<Integer>>(10);
           
           for (int i = 0;i < size; i++ )
                lookup.add(i,g.insertVertex(new Integer(i)));
            
            for (int i = 0;i < size; i++ )
                 for (int j = i+1; j < size; j++ )
                     g.insertEdge(lookup.elementAt(i),lookup.elementAt(j),i);
            System.out.println("Test 2 passed");
       }
       catch(GraphException e){
           System.out.println("**********Test 2 failed");
       }
  
       
     /////////////////////Test 3, try to insert  self loops and multiple edges
       try{
           int size = 10;
           Graph<Integer> g = new Graph<Integer>();
           Vector<Vertex<Integer>> lookup  = new Vector<Vertex<Integer>>(10);
           
           for (int i = 0;i < size; i++ )
                lookup.add(i,g.insertVertex(new Integer(i)));
            
            for (int i = 0;i < size; i++ )
                 for (int j = 0; j < size; j++ )
                     g.insertEdge(lookup.elementAt(i),lookup.elementAt(j),i);
            System.out.println("**********Test 3 failed");
       }
       catch(GraphException e){
           System.out.println("Test 3 passed");
       }
  
      
       
        /////////////////////Test 4, test for adjacency
       try{
           int size = 10;
           Graph<Integer> g = new Graph<Integer>();
           Vector<Vertex<Integer>> lookup  = new Vector<Vertex<Integer>>(10);
           
           for (int i = 0;i < size; i++ )
                lookup.add(i,g.insertVertex(new Integer(i)));
            
            g.insertEdge(lookup.elementAt(3),lookup.elementAt(5),0);
           
            if ( g.areAdjacent(lookup.elementAt(5),lookup.elementAt(3))  )
                System.out.println("Test 4 passed");
            else  System.out.println("**********Test 4 failed");
            
       }
       catch(GraphException e){
           System.out.println("**********Test 4 failed");
       }      
       
      
        /////////////////////Test 5, test for non-existant adjacency
       try{
           
           int size = 10;
           Graph<Integer> g = new Graph<Integer>();
           Vector<Vertex<Integer>> lookup  = new Vector<Vertex<Integer>>(10);
           
           for (int i = 0;i < size; i++ )
                lookup.add(i,g.insertVertex(new Integer(i)));
                     
            g.insertEdge(lookup.elementAt(3),lookup.elementAt(5),0);
            g.insertEdge(lookup.elementAt(2),lookup.elementAt(5),0);
            
            boolean fail = false;
            
            for (int i = 0; i < size; i++ )
                 for (int j = i+1; j < size; j++ )
                     if   ( g.areAdjacent(lookup.elementAt(i),lookup.elementAt(j)) && 
                             ! ( (i == 3 && j == 5) || (i == 5 && j == 3)|| (i == 2 && j == 5) || (i == 5 && j == 2) ) 
                             ) 
                         fail = true;
           
           if ( !fail )
                System.out.println("Test 5 passed");
            else  System.out.println("**********Test 5 failed");
            
       }
       catch(GraphException e){
           System.out.println("**********Test 5 failed");
       }      
       
       
       
       /////////////////////Test 6, delete an edge
       try{
           int size = 10;
           Graph<Integer> g = new Graph<Integer>();
           Vector<Vertex<Integer>> lookup  = new Vector<Vertex<Integer>>(10);
           
           for (int i = 0;i < size; i++ )
                lookup.add(i,g.insertVertex(new Integer(i)));
            
            for (int i = 0;i < size; i++ )
                 for (int j = i+1; j < size; j++ )
                     g.insertEdge(lookup.elementAt(i),lookup.elementAt(j),i);
           Edge<Integer> e = g.findEdge(lookup.elementAt(3),lookup.elementAt(5));
           g.deleteEdge(e);
            System.out.println("Test 6 passed");
       }
       catch(GraphException e){
           System.out.println("**********Test 6 failed");
       }

       
       
      
       /////////////////////Test 7, try to delete a non-existant edge
       try{
           int size = 10;
           Graph<Integer> g = new Graph<Integer>();
           Vector<Vertex<Integer>> lookup  = new Vector<Vertex<Integer>>(10);
           
           for (int i = 0;i < size; i++ )
                lookup.add(i,g.insertVertex(new Integer(i)));
            
           g.insertEdge(lookup.elementAt(3),lookup.elementAt(7),1);
           boolean fail = false;
                     
            for (int i = 0;i < size; i++ )
                 for (int j = i+1; j < size; j++ )
                     if ( i != 3 && j != 7 )
                     {
                         Edge<Integer> e = g.findEdge(lookup.elementAt(3),lookup.elementAt(5));
                        if (e != null ){
                             g.deleteEdge(e);
                             fail = true;
                        }
                     }
            if ( fail) System.out.println("**********Test 7 failed");
            else System.out.println("Test 7 passed");
       }
       catch(GraphException e){
           System.out.println("Test 7 passed");
       }
       
       
       
       ///////////////////// Test 8: get incident edges
       try{
           
            int size = 10;
           Graph<Integer> g = new Graph<Integer>();
           Vector<Vertex<Integer>> lookup  = new Vector<Vertex<Integer>>(10);
           
           for (int i = 0;i < size; i++ )
                lookup.add(i,g.insertVertex(new Integer(i)));
            
            for (int i = 0;i < size; i++ ){
                 if ( i != 1) g.insertEdge(lookup.elementAt(1),lookup.elementAt(i),i);
                 if ( i != 7 && i != 0 && i != 1) g.insertEdge(lookup.elementAt(7),lookup.elementAt(i),i);                 
            }
                    
           
            Iterator<Edge<Integer>> it = lookup.elementAt(1).incidentEdges();
            int count = 0;
            while (it.hasNext()) 
            {
                count++;
                it.next();
            }
            if ( count != 9 ) System.out.println("**********Test 8 failed " );
            else{
                it = lookup.elementAt(7).incidentEdges();
                count = 0;
                while (it.hasNext()) 
                {
                    count++;
                    it.next();
                }
                if ( count != 8 ) System.out.println("**********Test 8 failed " );
                else System.out.println("Test 8 passed");
            }
       }
       catch(GraphException e){
           System.out.println("**********Test 8 failed");
       }      

        
        ///////////////////// Test 9: check findEdge
       try{
           
           int size = 10;
           Graph<Integer> g = new Graph<Integer>();
           Vector<Vertex<Integer>> lookup  = new Vector<Vertex<Integer>>(10);
           
           for (int i = 0;i < size; i++ )
                lookup.add(i,g.insertVertex(new Integer(i)));
            
            for (int i = 0;i < size; i++ ){
                 if ( i != 8) g.insertEdge(lookup.elementAt(8),lookup.elementAt(i),i);
            }
           Edge<Integer> e = g.findEdge(lookup.elementAt(3),lookup.elementAt(8));
           if ( e == null) System.out.println("**********Test 8 failed " );
           else  if ( e.getEndPoint1() == lookup.elementAt(3) &&  e.getEndPoint2() == lookup.elementAt(8) || 
                      e.getEndPoint1() == lookup.elementAt(8) &&  e.getEndPoint2() == lookup.elementAt(3))
                System.out.println("Test 9 passed");
           else System.out.println("**********Test 9 failed");
       }
       catch(GraphException e){
           System.out.println("**********Test 9 failed");
       }
   
   
   ///////////////////// Test 10: check connected components
   try{
           int size = 12;
           Graph<Integer> g = new Graph<Integer>();
           Vector<Vertex<Integer>> lookup  = new Vector<Vertex<Integer>>(size);
           
           
           
          for (int i = 0;i < size; i++ )
              lookup.add(i,g.insertVertex(new Integer(i)));
           
           
           // next create 3 connected components, groups {0,1,2}, {3,4,5,6}, {7,8,9,10,11}
           for (int i = 0;i < 3; i++ )
                 for (int j = i+1; j < 3; j++ )
                     g.insertEdge(lookup.elementAt(i),lookup.elementAt(j),i);

           for (int i = 3; i < 7; i++ )
                 for (int j = i+1; j < 7; j++ )
                     g.insertEdge(lookup.elementAt(i),lookup.elementAt(j),i);
           
           for (int i = 7; i < size; i++ )
                 for (int j = i+1; j < size; j++ )
                     g.insertEdge(lookup.elementAt(i),lookup.elementAt(j),i);
           
           Iterator<Iterator<Vertex<Integer>>> it = g.ConnectedComponents();
           int iteration = 0;
           boolean fail = false;
          
           int[] compFound = {0,0,0};
                      
           while (it.hasNext()){
                Iterator<Vertex<Integer>> comp = it.next();
                LinkedList<Integer> t = new LinkedList<Integer>();
                while (comp.hasNext()){
                    t.add(comp.next().getObject());
                }
                if (t.size() == 3)
                {
                    if (!( t.contains(new Integer(0)) &&  t.contains(new Integer(1)) && t.contains(new Integer(2)) ))
                        fail = true;
                    else compFound[0] = 1;
                }
                else if ( t.size() == 4)
                {
                        if (!( t.contains(new Integer(3)) &&  t.contains(new Integer(4)) 
                              && t.contains(new Integer(5)) ) && t.contains(new Integer(6)))
                            fail = true;
                        else compFound[1] = 1;
                }
                else if ( t.size() == 5)
                {
                    if (!( t.contains(new Integer(7)) &&  t.contains(new Integer(8)) && t.contains(new Integer(9))
                           && t.contains(new Integer(10))  && t.contains(new Integer(11))  ) )
                        fail = true;
                    else compFound[2] = 1;
                }
                iteration++;
            }
         
           
           if (iteration != 3) fail = true;

           
           if ( compFound[0] == 0 ||  compFound[0] == 0 || compFound[2] == 0  ) fail = true;
           
           
           if ( fail ) System.out.println("**********Test 10 failed");
           else System.out.println("Test 10 passed");
   }
   catch(GraphException e){
        System.out.println("**********Test 10 failed");
   }
       
   ///////////////////// Test 11: check MST
   try{
           int size = 5;
           Graph<Integer> g = new Graph<Integer>();
           Vector<Vertex<Integer>> lookup  = new Vector<Vertex<Integer>>(size);
           
           for (int i = 0;i < size; i++ )
                lookup.add(i,g.insertVertex(new Integer(i)));
           
          g.insertEdge(lookup.elementAt(0),lookup.elementAt(1),1);
          g.insertEdge(lookup.elementAt(0),lookup.elementAt(2),95);
          g.insertEdge(lookup.elementAt(0),lookup.elementAt(4),2);
          
          g.insertEdge(lookup.elementAt(1),lookup.elementAt(3),22);
          g.insertEdge(lookup.elementAt(1),lookup.elementAt(2),7);
          
          g.insertEdge(lookup.elementAt(2),lookup.elementAt(3),3);
          g.insertEdge(lookup.elementAt(2),lookup.elementAt(4),5);
          
          g.insertEdge(lookup.elementAt(3),lookup.elementAt(4),25);
          
          Iterator<Edge<Integer>> mst = g.MST();
          
          int weight = 0;
          int count = 0;
          while (mst.hasNext()){
              count++;
              Edge<Integer> e = mst.next();
              weight = weight + e.getWeight();
          }
          if ( weight == 11 ) System.out.println("Test 11 passed");
          else System.out.println("**********Test 11 failed");

   }       
   catch(GraphException e){
        System.out.println("**********Test 11 failed");
   }
       
       
   }
}
  

   

