addprocs(3)
@everywhere w = 1000
@everywhere h = 500

# This function allows a processor to work on part of the shared arraay
@everywhere function rowByRow(ystart, yend, sWidth, sHeight, S::SharedArray)

	# Each processor will do several rows of the array
	for y=ystart:yend, x=1:sWidth
	
		c = complex((x-sWidth/2)/(sHeight/2), (y-sHeight/2)/(sHeight/2))
		S[y,x] = julia(c, 256)

	end

	return S

end

# This function accepts a shared array and splits up the work 
@everywhere function parJuliaInit(S::SharedArray)

	sHeight = size(S, 1)
	sWidth = size(S, 2) 
	sectionHeight = floor(sHeight / 3)
	
	# Uses 4 processors to test out shared array 
	a = @spawn rowByRow((0*sectionHeight)+1, sectionHeight, sWidth, sHeight, S)
	b = @spawn rowByRow((1*sectionHeight)+1, 2*sectionHeight, sWidth, sHeight, S)
	c = @spawn rowByRow((2*sectionHeight)+1, 3*sectionHeight, sWidth, sHeight, S)
	rowByRow((3*sectionHeight)+1, sHeight, sWidth, sHeight, S)

	fetch(a)
	fetch(b)
	fetch(c)
	return S

end

## The original julia iteration function from marthemartician
@everywhere function julia(z, maxiter::Int64)

	c = -0.8 + 0.156im
	
	for n=1:maxiter

		if (abs(z) > 2)

			return n - 1
		
		end
	
		z = z^2 + c

	end

	return maxiter

end

# Adapted write function to write to a .tsv file
function mygnuwrite(img, file::String)

	s = open(file, "w")
	n, m = size(img)
	
	# Loop and write in three columns the x y value for gnuplot
	for i=1:n, j=1:m

		p = img[i, j]
		write(s, "$j $i $p\n")
		
	end

	close(s)

end

print("starting...\n")
tStart = time()
S = SharedArray(Int, (h, w))
parJuliaInit(S)
tStop = time()
mygnuwrite(sdata(S), "xyz.tsv")
print("done. took ", tStop-tStart, " seconds\n")
