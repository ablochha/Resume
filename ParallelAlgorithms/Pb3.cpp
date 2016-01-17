//Andrew Bloch-Hansen
//250473076
//CS3101B
//Assignment 3
//Problem 3

#include <iostream>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include <limits>
#include <cilk/cilk.h>
#include <cilk/cilk_api.h>
#include <cilktools/cilkview.h>

using namespace std;

//code from course website
//generate a random float value between 0 and RAND_MAX (both positive and negative )
float randvalue() {

	return static_cast<float>(random()) - static_cast<float>(RAND_MAX/2);

} //end randValue

//code from course website
//generate a random float value between 0 and RAND_MAX which is not zero(both positive and negative )
float randvalue_nonzero() {

	float res = randvalue();
	while(res < 10 * std::numeric_limits<float>::epsilon())	{

		res = randvalue();

        } //end while

	return res;

} //end randValue_nonzero

//code from metafork
//parallel matrix multiplication
void parallel_dandc(int i0, int i1, int j0, int j1, int k0, int k1, float* A, int lda, float* B, int ldb, float* C, int ldc, int X) {

    int di = i1 - i0;
    int dj = j1 - j0;
    int dk = k1 - k0;

    if (di >= dj && di >= dk && di >= X) {

        int mi = i0 + di / 2;
        cilk_spawn parallel_dandc(i0, mi, j0, j1, k0, k1, A, lda, B, ldb, C, ldc,X);
        parallel_dandc(mi, i1, j0, j1, k0, k1, A, lda, B, ldb, C, ldc,X);
        cilk_sync;

    } //end if

    else if (dj >= dk && dj >= X) {

        int mj = j0 + dj / 2;
        cilk_spawn   parallel_dandc(i0, i1, j0, mj, k0, k1, A, lda, B, ldb, C, ldc,X);
        parallel_dandc(i0, i1, mj, j1, k0, k1, A, lda, B, ldb, C, ldc,X);
         cilk_sync;

    } //end else if

    else if (dk >= X) {

        int mk = k0 + dk / 2;
        parallel_dandc(i0, i1, j0, j1, k0, mk, A, lda, B, ldb, C, ldc,X);
        parallel_dandc(i0, i1, j0, j1, mk, k1, A, lda, B, ldb, C, ldc,X);

    } //end else if

    else {

        for (int i = i0; i < i1; ++i)
            for (int j = j0; j < j1; ++j)
                for (int k = k0; k < k1; ++k)
                    C[i * ldc + j] += A[i * lda + k] * B[k * ldb + j];

    } //end else

} //end parallel_dandc

//this method copies the matrix partitions back into the original matrix
void combinePartitions(float *A, float *A1, float *A2, float *A3, int n) {

    //copy partition A1 into A
    for (int i = 0; i < n/2; i++) {

        for (int j = 0; j < n/2; j++) {

            A[i*n + j] = A1[i*(n/2) + j];

        } //end for

    } //end for

    //copy partition A2 into A
    for (int i = 0; i < n/2; i++) {

        for (int j = 0; j < n/2; j++) {

            A[((n/2)+i)*n + j] = A2[i*(n/2) + j];

        } //end for

    } //end for

    //copy partition A3 into A
    for (int i = 0; i < n/2; i++) {

        for (int j = 0; j < n/2; j++) {

            A[((n/2)+i)*n + (n/2)+j] = A3[i*(n/2) + j];

        } //end for

    } //end for

    //fill the top right corner with 0's
    for (int i = 0; i < n/2; i++) {

        for (int j = 0; j < n/2; j++) {

            A[i*n + (n/2)+j] = 0.0;

        } //end for

    } //end for

} //end combinePartitions

//this method takes a nxn invertible lower triangular matrix and uses divide
//and conquer strategy to compute the inverse
//@param *A the nxn matrix
//@param n the order of the matrix
void inverse(float* A, int n, int basecase) {

    //the basecase occurs when the matrix partition has been reduced to size 1
    if (n == 1) {

        A[0] = 1/A[0];

    } //end if

    //recursively find the inverse to the input array A
    else {

        //create matrix partition A1, the top left of matrix A
        float *A1 = new float [(n/2) * (n/2)];
        for (int i = 0; i < n/2; i++) {

            for (int j = 0; j < n/2; j++) {
		
                A1[i*(n/2) + j] = A[i*n + j];

            } //end for

        } //end for

        //create matrix partition A2, the bottom left of matrix A
        float *A2 = new float [(n/2) * (n/2)];
        for (int i = 0; i < n/2; i++) {

            for (int j = 0; j < n/2; j++) {

                A2[i*(n/2) + j] = A[((n/2)+i)*n + j];

            } //end for

        } //end for

        //create matrix partition A3, the bottom right of matrix A
        float *A3 = new float [(n/2) * (n/2)];
        for (int i = 0; i < n/2; i++) {

            for (int j = 0; j < n/2; j++) {

                A3[i*(n/2) + j] = A[((n/2)+i)*n + (n/2)+j];

            } //end for

        } //end for

        //create a temporary matrix holding -A3
        float *A3Negative = new float [(n/2) * (n/2)];
        for (int i = 0; i < n/2; i++) {

            for (int j = 0; j < n/2; j++) {

                A3Negative[i*(n/2) + j] = 0.0;

            } //end for

        } //end for

        //create a temporary matrix for multiplication involving A2
        float *A2Temp = new float [(n/2) * (n/2)];
        for (int i = 0; i < n/2; i++) {

            for (int j = 0; j < n/2; j++) {

                A2Temp[i*(n/2) + j] = 0.0;

            } //end for

        } //end for

        //recursively call inverse in parallel
        cilk_spawn inverse(A1, n/2, basecase);
        inverse(A3, n/2, basecase);
        cilk_sync;

        //compute -A3
        for (int i = 0; i < n/2; i++) {

            for (int j = 0; j < n/2; j++) {

                A3Negative[i*(n/2) + j] = A3[i*(n/2) + j] * -1;

            } //end for

        } //end for

        //A2 = A2 * A1
        parallel_dandc(0,n/2,0,n/2,0,n/2,A2,n/2,A1,n/2,A2Temp,n/2,basecase);

        //A2 = -A3 * A2
        parallel_dandc(0,n/2,0,n/2,0,n/2,A3Negative,n/2,A2Temp,n/2,A2,n/2,basecase);

        //copy the partitions back into the matrix A
        combinePartitions(A, A1, A2, A3, n);

        //clear the memory of the partitions
        delete [] A1;
        delete [] A2;
        delete [] A3;
        delete [] A2Temp;
        delete [] A3Negative;

    } //end else

} //end inverse

//This program computes the inverses of lower triangular matrices using
//a parallel divide and conquer algorithm. Matrices of order 2^i, where
//i = 8, 9, 10, 11, 12 are tested.
int main(int argc, char **argv) {

    //initialize time seed
    //srand(time(NULL));
    int basecase;
    double start,end,run_time;

    if (argc == 1) {

            cout<<"Usage: ./Pb3 workers basecase"<<endl;
            exit(0);
    } //end if

    else if ( argc > 2 ) {

            __cilkrts_set_param("nworkers", argv[1]);
            basecase = atoi(argv[2]);

    } //end else if

    cout<<"Using "<<__cilkrts_get_nworkers()<<" workers"<<endl;
    
    //test on matrices of order 2^i, i = 8, 9, 10, 11, 12
    for (int x = 8; x <= 12; x++) {

        int order = pow(2,x);
        
        //allocate a one-dimension lower-triangular matrix
        float *A  = new float [order * order];

        //assign value to matrix
        for(int i = 0; i<order; i++) {

            for(int j = 0; j<order; j++) {

                if(j == i )

                    A[i * order + j] = randvalue_nonzero();

                else if(j < i )

                    A[i * order + j] = randvalue();

                else

                    A[i * order + j] = 0.0;

            } //end for

        } //end for

        //compute the inverse of the matrix A
        cout<<"order: "<<order<<endl;
        start = __cilkview_getticks();
        inverse(A, order, basecase);
        end = __cilkview_getticks();
        run_time = (end - start) / 1.f;
        cout << "inversion took " << run_time << " mseconds."<<endl;

        //de-allocate array
        delete [] A ;

    } //end for

    return 0;

} //end main
