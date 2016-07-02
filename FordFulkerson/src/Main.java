import algorithms.*;
import flownetwork.*;
import library.*;

/**
 * Main.java
 * 
 * Course: CS4445
 * Author: Andrew Bloch-Hansen
 * 
 * Experimentally compares several strategies for choosing an
 * augmenting path during the Ford-Fulkerson algorithm for finding
 * maximum flow in a flow network. 
 *
 */
public class Main {
	
	/**
	 * TRIALS is the number of randomly generated flow networks
	 * STRATEGIES is the number of algorithms I implemented
	 */
	public static final int TRIALS = 9;
	public static final int STRATEGIES = 3;
	
	public static final String RANDOMOUTFILE = "RandomResults.txt";
	public static final String EXAMPLEOUTFILE = "ExampleResults.txt";
	public static final String INPUTOUTFILE = "UserInputResults.txt";
	public static final String INPUTGRAPH = "UserInputGraph.txt";
	public static final String EXAMPLE1 = "data" + System.getProperty("file.separator") + "testnetwork1.txt";
	public static final String EXAMPLE2 = "data" + System.getProperty("file.separator") + "testnetwork2.txt";
	public static final String EXAMPLE3 = "data" + System.getProperty("file.separator") + "testnetwork3.txt";
	
	/**
	 * Outputs the maximum flow as a string to a file
	 * @param G the flow network
	 * @param s the source vertex
	 * @param t the sink vertex
	 * @param out the file to write to
	 */
	private static void outputMaxFlow(FlowNetwork G, int s, int t, Out out) {
		
		out.println("Max flow from " + s + " to " + t);
		
		// Loop through all the vertices
		for (int v = 0; v < G.numVertices(); v++) {
							
			// Check each edge incident on every vertex
			for (FlowEdge e : G.adj(v)) {
								
				// Only print out edges with positive outgoing flow from that vertex 
				if ((v == e.from()) && (e.flow() > 0)) {
									
					out.println("   " + e);
									
				} //end if
								
			} //end for
							
		} //end for
		
		out.println();
		
	} //end outputMaxFlow
	
	/**
	 * Builds the first selected example
	 */
	public static void buildExample1() {
		
		Out out = new Out(EXAMPLE1);
		
		out.print("1004 2003 0 1 1000 1 1002 1000 1002 1003 1000");
		
		// Add all the edges from the source to the middle
		for (int i = 2; i < 1002; i++) {
			
			out.print(" 0 " + i + " 1");
			
		} //end for
		
		// Add all the edges from the middle to the sink'
		for (int i = 2; i < 1002; i++) {
			
			out.print(" " + i + " 1002 " + "1");
			
		} //end for
		
	} //end buildExample1
	
	/**
	 * Builds the second selected example
	 */
	public static void buildExample2() {
		
		Out out = new Out(EXAMPLE2);
		
		out.print("1002 2000");
		
		// Add all the edges from the source to the middle
		for (int i = 1; i < 1001; i++) {
			
			out.print(" 0 " + i + " 1");
			
		} //end for
		
		// Add all the edges from the middle to the sink
		for (int i = 1; i < 1001; i++) {
			
			out.print(" " + i + " 1001 " + "1");
			
		} //end for
		
	} //end buildExample2
	
	/**
	 * Computes maximum flow on a flow network uses a specific strategy and records the results
	 * @param G the flow network
	 * @param s the source vertex
	 * @param t the sink vertex
	 * @param results the number of iterations, the time, and the max flow
	 * @param i the row in the result table
	 * @param j the column in the result table
	 * @param strategy the algorithm to use
	 */
	public static void runTest(FlowNetwork G, int s, int t, TestResult results, int i, int j, AlgorithmStrategy strategy) {
		
		FordFulkerson maxFlow; 	// The maximum flow algorithm
		
		long start;				// The start time
		long end;				// The end end
		
		// Time the algorithm
		start = System.currentTimeMillis();
		maxFlow = new FordFulkerson(G, s, t, strategy);
		end = System.currentTimeMillis();
		
		results.setIterations(i, j, maxFlow.getStrategy().getNumIterations());
		results.setFlow(i, j, maxFlow.value());
		results.setTime(i, j, ((end - start) % 1000));
				
		maxFlow.resetFlow(G);
		
	} //end runTests
	
	/**
	 * Run a series of tests on randomly generated flow networks 
	 */
	private static void runRandomTests() {
		
		TestResult results = new TestResult(TRIALS, STRATEGIES);
		
		// For each vertex amount, try increasing the edge density
		int numVertices[] = {10, 10, 10, 100, 100, 100, 250, 250, 250};
		int numEdges[] = {25, 50, 100, 1000, 5000, 10000, 5000, 20000, 62500};
		
		StdOut.println("Starting tests of randomly created flow networks");
		
		// Loop through the trials and test each algorithm
		for (int i = 0; i < TRIALS; i++) {
			
			StdOut.print("Running Trial" + (i+1) + "...");
			
			FlowNetwork G = new FlowNetwork(numVertices[i],numEdges[i]);
			
			int s = 0;
			int t = G.numVertices()-1;
			
			runTest(G, s, t, results, i, 0, new ArbitraryStrategy());
			runTest(G, s, t, results, i, 1, new FattestStrategy());
			runTest(G, s, t, results, i, 2, new MinimumStrategy());
			
			StdOut.println("Done");
			
		} //end for
		
		results.outputResults(numVertices,  numEdges, RANDOMOUTFILE);
		results.outputCSV(numVertices, numEdges);
		
		StdOut.println("Tests of randomly created flow networks finished");
		
	} //end runRandomTests
	
	/**
	 * Run the specific examples that I created
	 */
	private static void runExampleTest() {
		
		TestResult results = new TestResult(2, STRATEGIES);
		
		int numVertices[] = {1004, 1002};	// Number of vertices in my examples
		int numEdges[] = {2003, 2000};		// Number of edges in my examples
		int s;								// The source vertex
		int t;								// The sink vertex
		
		// Build example 1 and create the flow network
		buildExample1();
		In in = new In(EXAMPLE1);
		FlowNetwork G = new FlowNetwork(in);
		in.close();
		
		s = 0;
		t = G.numVertices() - 1;
		
		StdOut.println("\nStarting tests of selected examples of flow networks");
		StdOut.print("Running Trial1...");
		
		runTest(G, s, t, results, 0, 0, new ArbitraryStrategy());
		runTest(G, s, t, results, 0, 1, new FattestStrategy());
		runTest(G, s, t, results, 0, 2, new MinimumStrategy());
		
		StdOut.println("Done");
		
		// Build example 2 and create the flow network
		buildExample2();
		in = new In(EXAMPLE2);
		G = new FlowNetwork(in);
		in.close();
		
		s = 0;
		t = G.numVertices() - 1;
		
		StdOut.print("Running Trial2...");
		
		runTest(G, s, t, results, 1, 0, new ArbitraryStrategy());
		runTest(G, s, t, results, 1, 1, new FattestStrategy());
		runTest(G, s, t, results, 1, 2, new MinimumStrategy());
		
		StdOut.println("Done");
		
		StdOut.println("Tests of selected examples of flow networks finished");
		
		results.outputResults(numVertices,  numEdges, EXAMPLEOUTFILE);
		
	} //end runExampleTest
	
	/**
	 * If the user wants to try a graph, solve their flow network and output the results
	 * @param args the filename of a flow network
	 */
	private static void runUserInput(String[] args) {
		
		// Try and use their file
		try {
						
			In in = new In(args[0]);
						
			FlowNetwork G = new FlowNetwork(in);
			in.close();
						
			TestResult results = new TestResult(1, STRATEGIES);
						
			int numVertices[] = {G.numVertices()};
			int numEdges[] = {G.numEdges()};
						
			int s = 0;
			int t = G.numVertices() - 1;
						
			StdOut.print("Running input problem...");
						
			runTest(G, s, t, results, 0, 0, new ArbitraryStrategy());
			runTest(G, s, t, results, 0, 1, new FattestStrategy());
			runTest(G, s, t, results, 0, 2, new MinimumStrategy());
						
			FordFulkerson maxFlow = new FordFulkerson(G, s, t, new FattestStrategy());
						
			Out out = new Out(INPUTGRAPH);
			out.println("User's Input Flow Network Maximum Flow using the Fattest path");
			out.println(G);
			outputMaxFlow(G, s, t, out);
			out.close();
			maxFlow.resetFlow(G);
						
			StdOut.println("Done\n");
			StdOut.println("User's input problem running time saved to '" + INPUTOUTFILE + "'");
			StdOut.println("User's input problem solution saved to '" + INPUTGRAPH + "'");
						
			results.outputResults(numVertices,  numEdges, INPUTOUTFILE);
						
		} //end try
					
		// The file didn't work
		catch (Exception e) {
						
			StdOut.println("The user tried to input a graph but it failed, reason: " + e);
						
		} //end catch
		
	} //end runUserInput

	/**
	 * Runs Ford-Fulkerson variations on randomly generated flow networks, selected networks, and the user's input
	 * @param args the filename of a flow network
	 */
	public static void main(String[] args) {
		
		// Check if the user input a filename
		if (args.length > 0) {
			
			runUserInput(args);
			
		} //end if
		
		// Otherwise run the tests
		else {
			
			runRandomTests();
			runExampleTest();
			
			
			StdOut.println("\nCSV results saved to 'csv' directory");
			StdOut.println("Results for the random tests have been saved to '" + RANDOMOUTFILE + "'");
			StdOut.println("Results for the selected examples have been saved to '" + EXAMPLEOUTFILE + "'");
			
		} //end else
		
	} //end main

} //end Main
