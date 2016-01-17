addprocs(7)

# Question 1
# This method takes in an array of numbers, and returns a sorted array using a serial merge sort
function Merge_Sort_Serial(data)
	
	return Merge_Split_Serial(data, 1, length(data))
	
end

# Question 1
# This method recursively divides the array into smaller chunks
@everywhere function Merge_Split_Serial(data, istart, iend)
	
	# If the array is not empty 
	if (istart < iend)

		# Divide the array into sub-arrays to be sorted
		mid = floor((istart + iend)/2) 
		a = Merge_Split_Serial(data, istart, mid)
		b = Merge_Split_Serial(data, mid+1, iend)

		# Combine sorted sub-arrays together 
		temp = [0 for i=1:(length(a)+length(b))]
		return c = Merge_Serial(a, b, 1, length(a), 1, length(b), temp, 1)
		
	else

		# We have arrived at an array of size 1, it is sorted so we can send this array back
		return [data[istart]]  

	end

end

# Question 1
# This method combines two sorted arrays into one sorted array
@everywhere function Merge_Serial(a, b, astart, aend, bstart, bend, temp, tempstart)

	# Length of the two array sections combined
	n = ((aend-astart)+1) + ((bend-bstart)+1)
	
	# Loop through the length of the array
	for index=tempstart:tempstart+n-1
		
		# The element from the left side is added in
		if (astart <= aend && (bstart > bend || a[astart] <= b[bstart])) 

			temp[index] = a[astart]
			astart = astart + 1

		# The element from the right side is assed in
		else

			temp[index] = b[bstart]
			bstart = bstart + 1

		end

	end

	# Send back the array containing the merged sub arrays
	return temp

end

# Question 3
# This method takes in an array of numbers, and returns a sorted array using a parallel merge sort
function Merge_Sort_Parallel(data)
	
	depth = 1   
	return Merge_Split_Parallel(data, 1, length(data), depth)
	 
end

# Question 3
# This method recursively divides the array into smaller chunks
@everywhere function Merge_Split_Parallel(data, istart, iend, depth)

	# Change back to serial sort for small sub-arrays 
	if ((((iend - istart) + 1) <= 1024) || depth <= 0) 
		
		return Merge_Split_Serial(data, istart, iend)
		
	else	
		mid = floor((istart + iend)/2)
		
		# Offload half of the work to other processors
		depth = depth - 1
		a = @spawn Merge_Split_Parallel(data, istart, mid, depth)
		b = Merge_Split_Parallel(data, mid+1, iend, depth)

		# Combine the sorted array
		temp = [0 for i=1:(iend-istart+1)]
		return c = Merge_Parallel(fetch(a), b, 1, length(temp)-length(b), 1, length(b), 1, temp, depth)

	end

end

# Question 2
# This method combines two sorted arrays together by using binary search to determine the final position of an element
@everywhere function Merge_Parallel(a, b, astart, aend, bstart, bend, i, temp, depth)

	# Calculate the length of both sub arrays
	na = aend - astart + 1
	nb = bend - bstart + 1

	# If the subarrays are small enough or we have split enough times, send them to serial merge method
	if (((na + nb) <= 1024) || depth <= 0)
	
		return Merge_Serial(a, b, astart, aend, bstart, bend, temp, i)

	end

	# Make sure array a is not smaller than array b
	if (na < nb) 

		return Merge_Parallel(b, a, bstart, bend, astart, aend, i, temp, depth)

	else
		
		# Get the median of the left side, and the index of the strictly greater element from the right side
		ma = floor((astart + aend)/2)
		mb = Binary_Search(b, a[ma], bstart, bend)
		
		# The combination of these indexes produce the final position in the sorted array for the value of the median of the left side 
		finalIndex = ((ma - astart) + (mb-bstart)) + i
		temp[finalIndex] = a[ma]

		# Offload half the work to other processors
		depth = depth - 1
		leftTempRef = @spawn Merge_Parallel(a, b, astart, ma-1, bstart, mb-1, i, temp, depth)
		i+= ((ma-astart) + (mb-bstart)) + 1 
		rightTemp = Merge_Parallel(a, b, ma+1, aend, mb, bend, i, temp, depth)

		# Copy the sorted sub-sections into the final section
		leftTemp = fetch(leftTempRef)

		temp[1:finalIndex-1] = leftTemp[1:finalIndex-1]
		#temp[finalIndex+1:length(temp)] = rightTemp[finalIndex+1:length(temp)]
		return temp
		
	end

end	  

# Question 2
# This method returns the index that matches the key it was looking for
@everywhere function Binary_Search(b, key, imin, imax)

	# If array b is empty, sending imin back will mean nothing in b is smaller than the key
	if (imax < imin)
	
		return imin

	end

	# These are temporary so we dont change the real indexes  
	tempMin = imin
	tempMax = imax	
	
	# Perform a binary search while there is still an array to search
	while (tempMin <= tempMax)

		imid = floor((tempMin+tempMax)/2)
	
		if (b[imid] > key)

			tempMax = imid - 1

		elseif (b[imid] < key)

			tempMin = imid + 1

		else

			# The key was in array b, and we can return that index 
			return imid + 1

		end
	end

	# The key was greater than everything in array b 
	if (tempMin >= imax+1)

		return imax+1

	# The key was smaller than everything in array b
	elseif (tempMax <= imin)

		return imin

	# The key was not in array b, and tempMin represents the first strictly greater value 
	else

		return tempMin

	end

end	  

# Question 4
# This method records the time it takes to run some function f on some input n		
function runningtime(f, n)

	tic()
	n = f(n)
	t = toc()
	t
	
end

# This method tests Merge_Sort_Serial for input sizes 2^10:2^24 
function testSerial(T_1)

	# Loop to test different sizes of input on the serial merge sort
	for j=10:24
			
		A = [abs(rem(rand(Int32),100000)) for i=1:2^j]
		print("Trial ", j-9, ", Input Size is 2^", j, ": ")
		T_1[j-9] = runningtime(Merge_Sort_Serial, A)
		
	end

end

# This method tests Merge_Sort_Parallel for input sizes 2^10:2^24, and accros 1:8 cores
function testParallel(T_1_8)

	# Loop to test different amount of processors on the parallel merge sort
	lastproc = 8
	for k=1:8
		println()
		println("Processors: ", nprocs(), ", Processor ID's: ", procs())
		# Loop to test different sizes of input on the parallel merge sort
		for j=10:24
			
			# Create random arrays and record the time it takes to sort them
			A = [abs(rem(rand(Int32),100000)) for i=1:2^j]
			print("Trial ", j-9, ", Input Size is 2^", j, ": ")
			T_1_8[k, j-9] = runningtime(Merge_Sort_Parallel, A)
			
		end

		# remove a new worker
		if (lastproc > 1)

			println("Removing Processor ID: ", lastproc)
			rmprocs(lastproc)

			while (nprocs() == lastproc)
			
				print("")
					
			end
			
			lastproc = lastproc - 1
			println("Updated Processor ID's: ", procs())

		end

	end

end

# Initializing the arrays to time the merge sorts 
T_1 = [0.0 for i=1:15]
T_1_8 = [0.0 for i=1:8, j=1:15] 

# Print out some output 
println()
println("Starting Serial . . .")
println()
testSerial(T_1)
println()
println("Serial Finished, Starting Parallel . . .")
testParallel(T_1_8)
S = [T_1[j] / T_1_8[k, j] for j=1:15, k=1:8]

using Winston

Sizes = [2^i for i=10:24]
C1 = Winston.Curve(Sizes, [S[1,j] for j=1:15])
C2 = Winston.Curve(Sizes, [S[2,j] for j=1:15])
C3 = Winston.Curve(Sizes, [S[3,j] for j=1:15])
C4 = Winston.Curve(Sizes, [S[4,j] for j=1:15])
p = FramedPlot()
add(p, C1, C2)
add(p, C3, C4)
C5 = Winston.Curve(Sizes, [S[5,j] for j=1:15])
C6 = Winston.Curve(Sizes, [S[6,j] for j=1:15])
C7 = Winston.Curve(Sizes, [S[7,j] for j=1:15])
C8 = Winston.Curve(Sizes, [S[8,j] for j=1:15])
p2 = FramedPlot()
add(p2, C5, C6)
add(p2, C7, C8)
