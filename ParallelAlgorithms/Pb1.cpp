// CS3101B Assignment 2
// Problem 1
// Andrew Bloch-Hansen
// 250473076


// Question 5
// Fill applies the algorithm from the problem set to the sub-array sent in as parameters.
// @param T[][] the 2-d array representing the tableau
// @param x1 the starting x coordinate
// @param x2 the ending x coordinate
// @param y1 the starting y coordinate
// @param y2 the ending y coordinate
void fill(int T[][], int x1, int x2, int y1, int y2) {
	
	// Traverse one row at a time
	for (int i = y1; i <= y2; i++) {
		
		// For each row, visit every element in the block
		for (int j = x1; j <= x2; j++) {
		
			// Base case of the algorithm is T[i,j] = 1
			if (i == 0 || j == 0) {

				T[i,j] = 1;
			
			} //end if
		
			else {

				T[i,j] = T[i-1,j] + T[i,j-1];

			} //end else
		
		} //end for
	
	} //end for

} //end fill

// Question 5
// This program first builds a tableau using the divide and conquer method
// Then it builds the same tableau using the blocking algorithm
int main(int argc, char* argv[]) {

	int n = 1024;			// The length n of the n*n tableau
	int numBlocks = 32;		// The number of blocks in a row
	int blockSize = n/numBlocks;	// The number of elements in a block
	int T[n][n];			// The 2-d array representing the tableau

	// Divide and Conquer 
	// Fills top left block
	// Fills top right and bottom left blocks in parallel
	// Fills bottom right block
	fill(T, 0, (n/2)-1, 0, (n/2)-1);
	cilk_spawn fill(T, n/2, n-1, 0, (n/2)-1);
	fill(T, 0, (n/2)-1, n/2, n-1);
	cilk_sync;
	fill(T, n/2, n-1, n/2, n-1);

	// Blocking Algorithm
	// Starts by building the top left block
	// Generates blocks on the diagonal from top right to bottom left
	// This loop ends at the halfway point, just after doing the median diagonal
	int T2[n][n];
	for (int a = 1; a <= numBlocks; a++) {

		int x1 = blockSize * (a-1);	// The starting x coordinate of a block
		int x2 = (blockSize * a) - 1;	// The ending x coordinate of a block
		int y1 = 0;			// The starting y coordinate of a block
		int y2 = blockSize - 1;		// The ending y coordinate of a block
		int count = 1;			// Counts the number of blocks built on the diagonal

		// Each iteration of this loop will fill a block on the diagonal in parallel
		while (count < a) {

			// Fill a block and adjust the coordinates for the next block to be filled
			cilk_spawn fill(T, x1, x2, y1, y2);
			x1 -= blockSize;
			x2 -= blockSize;
			y1 += blockSize;
			y2 += blockSize;
			count++;
		
		} //end while

		// The last block is built by the main processor
		fill(T, x1, x2, y1, y2);
		cilk_sync;

	} //end for

	// Blocking Algorithm
	// Picks up after the first for loop, and fills blocks in the bottom right portion of the tableau
	// Finishes with the block in the bottom right corner
	for (int a = 2; a <= numBlocks; a++) {

		int x1 = (n - 1) - blockSize;	// The starting x coordinate of a block
		int x2 = n - 1;			// The ending x coordinate of a block
		int y1 = blockSize * (a - 1);	// The starting y coordinate of a block
		int y2 = (blockSize * a) - 1;	// The ending y coordinate of a block
		int count = numBlocks - a;	// Counts the number of blocks left to build on the diagonal

		// Each iteration of this loop will fill a block on the diagonal in parallel
		while (count > 0) {
	
			// Fill a block and adjust the coordinate for the next block to be filled
			cilk_spawn fill(T, x1, x2, y1, y2);
			x1 -= blockSize;
			x2 -= blockSize;
			y1 += blockSize;
			y2 += blockSize;
			count--;

		} //end while

		// The last block is built by the main processor
		fill(T, x1, x2, y1, y2);
		cilk_sync;

	} //end for

} //end main
