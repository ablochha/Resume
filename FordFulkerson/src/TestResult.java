import library.*;

/**
 * TestResult.java
 * 
 * Course: CS4445
 * Author: Andrew Bloch-Hansen
 * 
 * A TestResult stores the number of iterations, the max flow, and the time taken 
 * for each Ford-Fulkerson algorithm. A TestResult can also write its contents to a file. 
 *
 */
public class TestResult {
	
	private int _iterations[][];	// The number of iterations to compute the maximum flow
	private double _flow[][];		// The value of the maximum flow
	private long _time[][];			// The time taken to compute the maximum flow
	
	private int _trials;			// The number of trials done
	private int _strategies;		// The number of algorithms used
	

	/**
	 * Initializes the arrays for the results
	 * @param trials the number of trials to do
	 * @param strategies the number of algorithms to use
	 */
	public TestResult(int trials, int strategies) {
		
		_iterations = new int[trials][strategies];
		_flow = new double[trials][strategies];
		_time = new long[trials][strategies];
		
		_trials = trials;
		_strategies = strategies;
		
	} //end TestResult
	
	/**
	 * Returns the number of iterations for a particular trial
	 * @param i the row number
	 * @param j the column number
	 * @return the number of iterations
	 */
	public int getIterations(int i, int j) {
		
		return _iterations[i][j];
		
	} //end getIterations
	
	/**
	 * Sets the number of iterations for a particular trial
	 * @param i the row number
	 * @param j the column number
	 * @param the number of iterations
	 */
	public void setIterations(int i, int j, int value) {
		
		_iterations[i][j] = value;
		
	} //end setIterations
	
	/**
	 * Returns the maximum flow for a particular trial
	 * @param i the row number
	 * @param j the column number
	 * @return the maximum flow
	 */
	public double getFlow(int i, int j) {
		
		return _flow[i][j];
		
	} //end getFlow
	
	/**
	 * Sets the maximum flow for a particular trial
	 * @param i the row number
	 * @param j the column number
	 * @param the maximum flow
	 */
	public void setFlow(int i, int j, double value) {
		
		_flow[i][j] = value;
		
	} //end setFlow

	/**
	 * Returns the time taken for a particular trial
	 * @param i the row number
	 * @param j the column number
	 * @return the time taken
	 */
	public long getTime(int i, int j) {
	
		return _time[i][j];
	
	} //end getTime
	
	/**
	 * Sets the time taken for a particular trial
	 * @param i the row number
	 * @param j the column number
	 * @param the time taken
	 */
	public void setTime(int i, int j, long value) {
		
		_time[i][j] = value;
		
	} //end setTime
	
	/**
	 * Writes the number of iterations results to a file
	 * @param out the file stream
	 * @param numVertices the number of vertices each trial
	 * @param numEdges the number of edges each trial
	 */
	private void outputIterations(Out out, int numVertices[], int numEdges[]) {
		
		out.println("Number of iterations to find maximum flow");
		out.println("Trial\tVertices\tEdges\t\tArbitrary\tFattest\t\tMinimum");
		
		// Loop through the trials
		for (int i = 0; i < _trials; i++) {
			
			out.print(i+1 + "\t" + numVertices[i] + "\t\t" + numEdges[i] + "\t\t");
			
			// Loop through the strategies
			for (int j = 0; j < _strategies; j++) {
				
				out.print(_iterations[i][j] + "\t\t");
				
			} //end for
			
			out.println();
			
		} //end for
		
	} //end outputIterations
	
	/**
	 * Writes the maximum flow results to a file
	 * @param out the file stream
	 * @param numVertices the number of vertices each trial
	 * @param numEdges the number of edges each trial
	 */
	private void outputFlow(Out out, int numVertices[], int numEdges[]) {
		
		out.println("Max flow");
		out.println("Trial\tVertices\tEdges\t\tArbitrary\tFattest\t\tMinimum");
		
		// Loop through the trials
		for (int i = 0; i < _trials; i++) {
			
			out.print(i+1 + "\t" + numVertices[i] + "\t\t" + numEdges[i] + "\t\t");
			
			// Loop through the strategies
			for (int j = 0; j < _strategies; j++) {
				
				out.print(_flow[i][j] + "\t\t");
				
			} //end for
			
			out.println();
			
		} //end for
		
	} //end outputFlow
	
	/**
	 * Writes the time taken results to a file
	 * @param out the file stream
	 * @param numVertices the number of vertices each trial
	 * @param numEdges the number of edges each trial
	 */
	private void outputTime(Out out, int numVertices[], int numEdges[]) {
		
		out.println("Time (ms)");
		out.println("Trial\tVertices\tEdges\t\tArbitrary\tFattest\t\tMinimum");
		
		// Loop through the trials
		for (int i = 0; i < _trials; i++) {
			
			out.print(i+1 + "\t" + numVertices[i] + "\t\t" + numEdges[i] + "\t\t");
			
			// Loop through the strategies
			for (int j = 0; j < _strategies; j++) {
				
				out.print(_time[i][j] + "\t\t");
				
			} //end for
			
			out.println();
			
		} //end for
		
	} //end outputFlow
	
	/**
	 * Outputs the results in a format that easily creates graphs
	 * @param numVertices the number of vertices each trial
	 * @param numEdges the number of edges each trial
	 */
	public void outputCSV(int numVertices[], int numEdges[]) {
		
		// Create the graphs for number of iterations
		for (int i = 0; i < 3; i++) {
			
			String outFile = "csv" + System.getProperty("file.separator") + "iterations" + (i+1) + ".csv";
			Out out = new Out(outFile);
			
			out.println(",1,2,3");
			out.println("Arbitrary," + _iterations[i*3][0] + "," + _iterations[(i*3)+1][0] + "," + _iterations[(i*3)+2][0]);
			out.println("Fattest," + _iterations[i*3][1] + "," + _iterations[(i*3)+1][1] + "," + _iterations[(i*3)+2][1]);
			out.println("Minimum," + _iterations[i*3][2] + "," + _iterations[(i*3)+1][2] + "," + _iterations[(i*3)+2][2]);
			
			out.close();
			
		} //end for
		
		// Create the graphs for running time
		for (int i = 0; i < 3; i++) {
					
			String outFile = "csv" + System.getProperty("file.separator") + "time" + (i+1) + ".csv";
			Out out = new Out(outFile);
					
			out.println(",1,2,3");
			out.println("Arbitrary," + _time[i*3][0] + "," + _time[(i*3)+1][0] + "," + _time[(i*3)+2][0]);
			out.println("Fattest," + _time[i*3][1] + "," + _time[(i*3)+1][1] + "," + _time[(i*3)+2][1]);
			out.println("Minimum," + _time[i*3][2] + "," + _time[(i*3)+1][2] + "," + _time[(i*3)+2][2]);
			
			out.close();
					
		} //end for
		
	} //end outputCSV
	
	/**
	 * Outputs the results of the random trials to a file
	 * @param numVertices the number of vertices each trial
	 * @param numEdges the number of edges each trial
	 * @param filename the file to write to
	 */
	public void outputResults(int numVertices[], int numEdges[], String filename) {
		
		Out out = new Out(filename);
		
		outputIterations(out, numVertices, numEdges);
		outputFlow(out, numVertices, numEdges);
		outputTime(out, numVertices, numEdges);
		
		out.close();
		
	} //end outputResults

} //end TestResult
